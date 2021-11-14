package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.tools.AbstractAnnotationProcessor;
import io.toolisticon.aptk.tools.FilerUtils;
import io.toolisticon.aptk.tools.TypeMirrorWrapper;
import io.toolisticon.aptk.tools.generators.SimpleJavaWriter;
import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.spiap.api.Service;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Annotation Processor for {@link io.toolisticon.fluapigen.api.FluentApi}.
 * <p>
 * This demo processor does some validations and creates a class.
 */

@Service(Processor.class)
public class FluentApiProcessor extends AbstractAnnotationProcessor {

    private final static Set<String> SUPPORTED_ANNOTATIONS = createSupportedAnnotationSet(FluentApi.class);

    static class AnnotationMirrorValueWrapper<ANNOTATION_TYPE extends Annotation> {
        final AnnotationMirror annotationMirror;
        final ANNOTATION_TYPE annotation;

        public AnnotationMirrorValueWrapper(AnnotationMirror annotationMirror, ANNOTATION_TYPE annotation) {
            this.annotationMirror = annotationMirror;
            this.annotation = annotation;
        }
    }

    static class State {

        private final BackingBeanWrapper[] backingBeans;
        /**
         * Look up table for getting a backing bean for an interface fqn.
         */
        private final Map<String, BackingBeanWrapper> interfaceFqnToBackingBeanMap = new HashMap<>();
        private boolean errorDetected = false;

        State(TypeElement typeElement, BackingBeanWrapper[] backingBeans) {

            this.backingBeans = backingBeans;

            /*-
            for (BackingBeanWrapper backingBean : backingBeans) {
                for (String interfaceFqn : backingBean.associatedInterfacesAsFqn()) {
                    // interface must be bound uniquely to a backing bean
                    // need to throw error otherwise
                    if (interfaceFqnToBackingBeanMap.containsKey(interfaceFqn)) {
                        MessagerUtils.error(typeElement, FluApiGenProcessorMessages.ERROR_INTERFACE_MUST_BE_BOUND_WITH_ONE_BACKING_BEAN, interfaceFqn, interfaceFqnToBackingBeanMap.get(interfaceFqn).id());
                        continue;
                    }
                    interfaceFqnToBackingBeanMap.put(interfaceFqn, backingBean);
                }
            }
            */


            this.errorDetected = true;
        }

        BackingBeanWrapper getBackingBean(String interfaceFqn) {
            return interfaceFqnToBackingBeanMap.get(interfaceFqn);
        }

        List<BackingBeanWrapper> getBackingBeans() {
            return Arrays.asList(backingBeans);
        }

    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return SUPPORTED_ANNOTATIONS;
    }

    @Override
    public boolean processAnnotations(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {


        // process Services annotation
        for (Element element : roundEnv.getElementsAnnotatedWith(FluentApi.class)) {

            // cast to TypeElement
            TypeElement typeElement = (TypeElement) element;


            // first get the root config
            FluentApiWrapper rootConfig = FluentApiWrapper.wrap(typeElement);

            createBuilderClass(typeElement, rootConfig);

            // Setup state object
            // -- interface to backing bean LUT
            // -- backing bean id to backing bean LUT
            //State state = new State(typeElement, rootConfig.backingBeans());

            // Now determine all fields of backing bean by iterating over all interfaces
            //getFieldsFromInterfaces(typeElement, state);

            // validate the configuration
            // need to check:
            // -- interfaces must be associated to one backing bean
            // -- return values of interface methods must be related with a backing bean.

            // create builder class
            //createBuilderClass(typeElement, );

            // create backing bean class

        }

        return false;

    }

    /*-
    void getFieldsFromInterfaces(Element typeElement, State state) {

        // iterate over backingBean interfaces
        for (BackingBeanWrapper backingBean : state.getBackingBeans()) {

            for (String bbInterface : backingBean.associatedInterfacesAsFqn()) {
                TypeElement interfaceElement = TypeUtils.TypeRetrieval.getTypeElement(bbInterface);

                // now loop over methods
                List<ExecutableElement> methods = ElementUtils.AccessEnclosedElements.<ExecutableElement>getEnclosedElementsOfKind(interfaceElement, ElementKind.METHOD);

                for (ExecutableElement method : methods) {

                    // determine return type to check for:
                    // -- validation:
                    // ---- must be an known and associated interface
                    // ---- otherwise must be annotated as command
                    // -- follow up interface : transition to other backing bean or not
                    String returnType = TypeUtils.TypeConversion.convertToFqn(method.getReturnType());


                    // check for associated interface
                    BackingBeanWrapper returnTypeRelatedBackingBean = state.getBackingBean(returnType);
                    if (returnTypeRelatedBackingBean == null) {
                        // TODO: must add validation for Command here

                        MessagerUtils.error(method, FluApiGenProcessorMessages.ERROR_INTERFACE_METHODS_RETURN_TYPE_MUST_BE_BOUND_WITH_A_BACKING_BEAN, bbInterface, returnType);
                    }

                    // Now determine Field name
                    // -- by annotation
                    // -- naming convention

                    // There are two cases

                }
            }

        }

    }
    */

    private void validateConfig() {

    }

    /**
     * Generates a class.
     * <p>
     * Example how to use the templating engine.
     * <p>
     */
    public static void createBuilderClass(TypeElement element, FluentApiWrapper fluentApiWrapper) {

        String packageName = TypeMirrorWrapper.wrap(element.asType()).getPackage();

        // Fill Model
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("fluentApi", fluentApiWrapper);
        model.put("package", packageName);

        model.put("imports", fluentApiWrapper.getImports());//root.getImports());
        model.put("interfaces", new HashSet<>());

        // create the class
        String filePath = packageName + "." + fluentApiWrapper.builderClassName();
        try {
            SimpleJavaWriter javaWriter = FilerUtils.createSourceFile(filePath, element);
            javaWriter.writeTemplate("/FluentApiRoot.tpl", model);
            javaWriter.close();
        } catch (IOException e) {
            //MessagerUtils.error(element, FluApiGenProcessorMessages.ERROR_COULD_NOT_CREATE_CLASS, filePath, e.getMessage());
        }
    }



    /*-
     * Generates a class.
     * <p>
     * Example how to use the templating engine.
     * <p>
     *
     * @param packageElement The TypeElement representing the annotated class
     * /

    private void createClass(PackageElement packageElement, List<String> imports, TargetClassWrapper rootClassWrapper, List<TargetClassWrapper> embeddedClassWrappers) {


        // Now create class
        String packageName = packageElement.getQualifiedName().toString();

        // Fill Model
        Map<String, Object> model = new HashMap<String, Object>();


        // create the class
        String filePath = packageName + "." + rootClassWrapper.getShortTypeName();
        try {
            SimpleJavaWriter javaWriter = FilerUtils.createSourceFile(filePath, packageElement);
            javaWriter.writeTemplate("/FluApiGen.tpl", model);
            javaWriter.close();
        } catch (IOException e) {
            MessagerUtils.error(packageElement, FluApiGenProcessorMessages.ERROR_COULD_NOT_CREATE_CLASS, filePath, e.getMessage());
        }
    }
 */
}

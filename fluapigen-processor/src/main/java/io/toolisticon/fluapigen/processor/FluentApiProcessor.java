package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.compilermessage.api.DeclareCompilerMessage;
import io.toolisticon.aptk.compilermessage.api.DeclareCompilerMessageCodePrefix;
import io.toolisticon.aptk.tools.AbstractAnnotationProcessor;
import io.toolisticon.aptk.tools.FilerUtils;
import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.aptk.tools.generators.SimpleJavaWriter;
import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.spiap.api.SpiService;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Annotation Processor for {@link io.toolisticon.fluapigen.api.FluentApi}.
 * <p>
 * This demo processor does some validations and creates a class.
 */

@DeclareCompilerMessageCodePrefix("FLUAPIGEN")
@SpiService(Processor.class)
public class FluentApiProcessor extends AbstractAnnotationProcessor {

    private final static Set<String> SUPPORTED_ANNOTATIONS = createSupportedAnnotationSet(FluentApi.class);

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return SUPPORTED_ANNOTATIONS;
    }

    @Override
    public boolean processAnnotations(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        // process Services annotation
        for (Element element : roundEnv.getElementsAnnotatedWith(FluentApi.class)) {

            // validate Fluent api element
            FluentApiWrapper fluentApiWrapper = FluentApiWrapper.wrap(element);


            // Now get all attributes
            try (RenderStateHelper renderStateHelper = RenderStateHelper.create()) {
                createClass(element, fluentApiWrapper);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        return false;

    }

    @DeclareCompilerMessage(code = "021", enumValueName = "ERROR_COULD_NOT_CREATE_CLASS", message = "Could not create class ${0} : ${1}")
    private void createClass(Element element, FluentApiWrapper fluentApiWrapper) {

        FluentApiState state = fluentApiWrapper.getState();

        ModelRoot modelRoot = new ModelRoot(state);

        // Now create class


        // Fill Model
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("model", modelRoot);

        // create the class
        String filePath = modelRoot.getPackageName() + "." + modelRoot.getClassName();
        try {
            SimpleJavaWriter javaWriter = FilerUtils.createSourceFile(filePath, element);
            javaWriter.writeTemplate("/FluentApi.tpl", model);
            javaWriter.close();
        } catch (IOException e) {
            MessagerUtils.error(element, FluentApiProcessorCompilerMessages.ERROR_COULD_NOT_CREATE_CLASS, filePath, e.getMessage());
        }
    }

}

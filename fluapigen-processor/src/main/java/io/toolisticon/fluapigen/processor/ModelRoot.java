package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.tools.ElementUtils;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModelRoot implements Validatable{

    private final String packageName;
    private final String className;

    private final List<ModelInterface> fluentInterfaces = new ArrayList<>();

    private final List<ModelBackingBean> fluentBackingBeanInterfaces = new ArrayList<>();

    ModelRoot(FluentApiState state) {
        packageName = ((PackageElement) ElementUtils.AccessEnclosingElements.getFirstEnclosingElementOfKind(state.getFluentApiWrapper()._annotatedElement(), ElementKind.PACKAGE)).getQualifiedName().toString();
        className = state.getFluentApiWrapper().value();

        // create backingBeanInterfaces model - must be done first to be able to reference it
        for (FluentApiBackingBeanWrapper fluentApiBackingBeanWrapper : state.getFluentApiBackingBeanWrappers()) {
            fluentBackingBeanInterfaces.add(new ModelBackingBean(fluentApiBackingBeanWrapper));
        }

        // create fluentInterface model
        for (FluentApiInterfaceWrapper interfaceWrapper : state.getFluentApiInterfaceWrappers()) {
            fluentInterfaces.add(new ModelInterface(interfaceWrapper, RenderStateHelper.getBackingBeanModelForBackingBeanInterfaceSimpleName(interfaceWrapper.valueAsTypeMirrorWrapper().getSimpleName())));
        }

        RenderStateHelper.init();


    }

    public String getPackageName() {
        return this.packageName;
    }

    public String getClassName() {
        return this.className;
    }

    public List<ModelInterface> getFluentInterfaces() {
        return fluentInterfaces;
    }

    public ModelInterface getRootInterface() {
        for (ModelInterface modelInterface : fluentInterfaces) {
            if(modelInterface.isRootInterface()) {
                return modelInterface;
            }
        }

        // should never happen
        return null;
    }

    public List<ModelBackingBean> getBackingBeans() {
        return fluentBackingBeanInterfaces;
    }

    public List<String> getImports() {
        Set<String> imports = new HashSet<>();

        for (FetchImports modelInterfaceMethod : this.fluentInterfaces) {
            imports.addAll(modelInterfaceMethod.fetchImports());
        }

        for (FetchImports modelInterfaceMethod : this.fluentBackingBeanInterfaces) {
            imports.addAll(modelInterfaceMethod.fetchImports());
        }

        List<String> sortedImports = new ArrayList<>(imports);
        Collections.sort(sortedImports);
        return sortedImports;
    }

    @Override
    public boolean validate() {
        boolean outcome = true;

        for (ModelBackingBean backingBean: fluentBackingBeanInterfaces) {
            outcome = outcome & backingBean.validate();
        }

        for (ModelInterface interfaceModel: fluentInterfaces) {
            outcome = outcome & interfaceModel.validate();
        }

        return outcome;
    }
}

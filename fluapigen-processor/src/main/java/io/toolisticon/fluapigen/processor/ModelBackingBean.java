package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.tools.corematcher.AptkCoreMatchers;
import io.toolisticon.aptk.tools.wrapper.ElementWrapper;
import io.toolisticon.aptk.tools.wrapper.ExecutableElementWrapper;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ModelBackingBean implements FetchImports {

    final FluentApiBackingBeanWrapper wrapper;
    final List<ModelBackingBeanField> fields;


    ModelBackingBean(FluentApiBackingBeanWrapper wrapper) {
        this.wrapper = wrapper;

        fields = ElementWrapper.wrap(wrapper._annotatedElement())
                .filterEnclosedElements().applyFilter(AptkCoreMatchers.IS_METHOD).getResult()
                .stream()
                .map(ExecutableElementWrapper::wrap)
                .map(ModelBackingBeanField::new)
                .collect(Collectors.toList());

        // init render state helper
        RenderStateHelper.addBackingBeanModel(this);


    }

    public String getBackingBeanInterfaceSimpleName() {
        return wrapper._annotatedElement().getSimpleName().toString();
    }

    public String getClassName() {
        return wrapper._annotatedElement().getSimpleName().toString() + "Impl";
    }

    public String interfaceClassName() {
        return wrapper._annotatedElement().getEnclosingElement().getSimpleName().toString() + "." + wrapper._annotatedElement().getSimpleName().toString();
    }

    public List<ModelBackingBeanField> getFields() {
        return this.fields;
    }

    /**
     * Get backing bean field by field name.
     * @param id the name of the field
     * @return the field or null if field doesn't exist
     */
    public Optional<ModelBackingBeanField> getFieldById(String id) {
        if (id != null) {
            for (ModelBackingBeanField field : fields) {
                if (id.equals(field.getFieldId())) {
                    return Optional.of(field);
                }
            }
        }

        return Optional.empty();
    }

    public boolean hasParent () {
        return RenderStateHelper.getParentBB(this) != null;
    }

    public ModelBackingBean getParent() {
        return RenderStateHelper.getParentBB(this);
    }

    @Override
    public Set<String> fetchImports() {
        Set<String> result = new HashSet<>();
        getFields().stream().map(ModelBackingBeanField::fetchImports).forEach(result::addAll);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ModelBackingBean that = (ModelBackingBean) o;

        return Objects.equals(getBackingBeanInterfaceSimpleName(), that.getBackingBeanInterfaceSimpleName());
    }

    @Override
    public int hashCode() {
        return  getBackingBeanInterfaceSimpleName() != null ? getBackingBeanInterfaceSimpleName().hashCode() : 0;
    }
}

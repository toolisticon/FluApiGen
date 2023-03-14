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

    /**
     * Gets the simple name of the backing bean interface
     * @return the simple name of the backing bean interface
     */
    public String getBackingBeanInterfaceSimpleName() {
        return wrapper._annotatedElement().getSimpleName().toString();
    }

    /**
     * Gets the class name used by the implementation of the backing bean.
     * Corresponds to simple interface name extended by "Impl".
     * @return the simple class name
     */
    public String getClassName() {
        return wrapper._annotatedElement().getSimpleName().toString() + "Impl";
    }

    /**
     * Gets the fluent api class relative name of the backing bean interface.
     * Corresponds to fluent api class simple name + "." + backing bean interface simple name.
     * @return  The qualified name of the interface
     */
    public String interfaceClassName() {
        return wrapper._annotatedElement().getEnclosingElement().getSimpleName().toString() + "." + wrapper._annotatedElement().getSimpleName().toString();
    }

    /**
     * Gets all fields that belong to the backing bean.
     * Fields correspond to methods in backing bean interface.
     * @return A list containing all fields of the backing bean
     */
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

    /**
     * Checks whether the backing bean has a parent or not.
     * @return true if the backing bean has a parent, otherwise false.
     */
    public boolean hasParent () {
        return RenderStateHelper.getParentBB(this) != null;
    }

    /**
     * Gets the parent backing bean.
     * @return The parent backing bean instance, or null if the backing bean has no parent
     */
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

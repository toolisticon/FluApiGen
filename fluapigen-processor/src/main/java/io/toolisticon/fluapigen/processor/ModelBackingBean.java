package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.compilermessage.api.DeclareCompilerMessage;
import io.toolisticon.aptk.tools.InterfaceUtils;
import io.toolisticon.aptk.tools.wrapper.TypeElementWrapper;

import javax.lang.model.element.TypeElement;
import java.util.*;
import java.util.stream.Collectors;

public class ModelBackingBean implements FetchImports, Validatable{

    final FluentApiBackingBeanWrapper wrapper;
    final List<ModelBackingBeanField> fields;


    ModelBackingBean(FluentApiBackingBeanWrapper wrapper) {
        this.wrapper = wrapper;

        fields = InterfaceUtils.getMethodsToImplement(TypeElementWrapper.wrap((TypeElement) wrapper._annotatedElement()))
                .stream().filter(e -> !e.isDefault())
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
        Set<String> result = getFields().stream().flatMap(field ->field.fetchImports().stream()).collect(Collectors.toSet());
        result.add("java.util.stream.StreamSupport");
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


    @Override
    @DeclareCompilerMessage(code = "22", enumValueName = "ERROR_BACKING_BEAN_FIELD_ID_MUST_BE_UNIQUE_IN_BB", message = "Backing bean field id '${0}' must be unique in backing bean ${1}", processorClass = FluentApiProcessor.class)
    public boolean validate() {
        boolean outcome = true;

        Map<String, List<ModelBackingBeanField>> existingBBFieldIds = new HashMap<>();
        for (ModelBackingBeanField field : fields) {
            outcome = outcome & field.validate();

            // collect fields by id
            existingBBFieldIds.computeIfAbsent(field.getFieldId(),e-> new ArrayList<>()).add(field);

        }

        // Must check if field ids are unique
        Set<Map.Entry<String, List<ModelBackingBeanField>>> idDoublets = existingBBFieldIds.entrySet().stream().filter(e -> e.getValue().size() != 1).collect(Collectors.toSet());
        if (!idDoublets.isEmpty()) {
            idDoublets.forEach(
                    e -> e.getValue().forEach(
                            f -> f.getCompilerMessageWriter().asError().write(FluentApiProcessorCompilerMessages.ERROR_BACKING_BEAN_FIELD_ID_MUST_BE_UNIQUE_IN_BB, e.getKey(), interfaceClassName())
                    )
            );
            outcome = false;
        }

        return outcome;
    }
}

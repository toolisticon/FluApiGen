// --------------------------
// Fields
// --------------------------

!{if model.fieldKind == FieldKind.SINGLE_VALUE}
    private ${model.shortFieldType} ${model.fieldName};
!{/if}!{if model.fieldKind == FieldKind.SET}
    private Set<${model.shortFieldType}> ${model.fieldName} = new HashSet<>();
!{/if}!{if model.fieldKind == FieldKind.LIST}
    private List<${model.shortFieldType}> ${model.fieldName} = new ArrayList<>();
!{/if}
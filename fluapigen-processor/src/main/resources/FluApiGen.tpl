package ${ packageName };

!{for import : imports}
import ${import};
!{/for}

/**
 * An empty class.
 */
public class ${ rootType.shortTypeName } {

!{for field:rootType.fields}
!{include resource:'/addFieldDeclaration.tpl', model:'field'}!{/include}
!{/for}

    /**
     * Constructor.
     */
    ${ rootType.shortTypeName }() {

    }

!{for field:rootType.fields}
    public void set${field.methodFieldName}(${field.shortFieldType} ${field.fieldName}) {
        this.${field.fieldName} = ${field.fieldName};
    }
!{/for}

}

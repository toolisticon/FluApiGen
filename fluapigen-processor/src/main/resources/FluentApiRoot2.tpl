package ${package};

!{for import : imports}
import ${import};
!{/for}

/**
 * Builder class.
 */
public class ${fluentApi.builderClassName} {

    // ------------------------------------------
    // -- Backing Beans
    // ------------------------------------------

!{for backingBean : fluentApi.backingBeans}
    public static class ${backingBean.simpleName} implements ${backingBean.interfaceTypeMirror.simpleName} {

!{for backingBeanField : backingBean.backingBeanFields}
        private ${backingBeanField.fieldType.simpleName} ${backingBeanField.fieldName};
!{/for}

!{for backingBeanField : backingBean.backingBeanFields}
        public void ${backingBeanField.setterName}(${backingBeanField.fieldType.simpleName} ${backingBeanField.fieldName}) {
            this.${backingBeanField.fieldName} = ${backingBeanField.fieldName};
        }

        public ${backingBeanField.fieldType.simpleName} ${backingBeanField.getterName}() {
            return this.${backingBeanField.fieldName};
        }
!{/for}
    }
!{/for}

    // ------------------------------------------
    // -- Fluent Interface Implementations
    // ------------------------------------------

!{for interface : interfaces}
    static class ${interface.className} implements ${interface.interfaceName} {
!{for method : interface.methods}
        public ${method.returnType.typeDeclaration} ${method.name} (${method.getParameterDeclarationString}) {
            return null;
        }
!{/for}
    }
!{/for}


    // ------------------------------------------
    // -- Static Builders Functions for Root
    // ------------------------------------------
!{for method : root.methods}
    public static ${method.returnType.typeDeclaration} ${method.name} (${method.parameterDeclarationString}){
        return new ${root.getClassName}().${method.name}(${method.parameterNames});
    }
!{/for}

}

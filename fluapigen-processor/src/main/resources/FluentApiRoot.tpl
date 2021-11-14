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
    public static class ${backingBean.simpleName}Impl implements ${backingBean.interfaceTypeMirror.simpleName} {

        /**
         * Initializer noarg constructor.
         */
        ${backingBean.simpleName}Impl(){

        }

        /**
         * Clone constructor.
         */
         ${backingBean.simpleName}Impl(${backingBean.interfaceTypeMirror.simpleName} toClone){
             // this won't work for managed collections and other encapsulated backing beans
!{for backingBeanField : backingBean.backingBeanFields}
             this.${backingBeanField.fieldName} = toClone.${backingBeanField.getGetterName}();
!{/for}
         }

!{for backingBeanField : backingBean.backingBeanFields}
        private ${backingBeanField.fieldType.getTypeDeclaration} ${backingBeanField.fieldName};
!{/for}

!{for backingBeanField : backingBean.backingBeanFields}
        public void ${backingBeanField.setterName}(${backingBeanField.fieldType.getTypeDeclaration} ${backingBeanField.fieldName}) {
            this.${backingBeanField.fieldName} = ${backingBeanField.fieldName};
        }

        public ${backingBeanField.fieldType.getTypeDeclaration} ${backingBeanField.getterName}() {
            return this.${backingBeanField.fieldName};
        }
!{/for}
    }
!{/for}

    // ------------------------------------------
    // -- Fluent Interface Implementations
    // ------------------------------------------

!{for interface : fluentApi.getFluentApiInterfaces}
    static class ${interface.className} implements ${interface.interfaceName} {

        private final ${interface.valueAsTypeMirrorWrapper.getTypeDeclaration} backingBean;

        private ${interface.className}() {
            this.backingBean = new ${interface.valueAsTypeMirrorWrapper.getTypeDeclaration}Impl();
        }

        private ${interface.className}( ${interface.valueAsTypeMirrorWrapper.getTypeDeclaration} backingBean ) {
            this.backingBean = new ${interface.valueAsTypeMirrorWrapper.getTypeDeclaration}Impl(backingBean);
        }

!{for method : interface.methods}
        public ${method.returnType.typeDeclaration} ${method.name} (${method.getParameterDeclarationString}) {

!{if method.isFinishingCommand}
            return ${method.getFinishingCommand.getFinishingCommandCall}(this.backingBean);
!{/if}!{if !method.isFinishingCommand}
            return null;
!{/if}
        }
!{/for}
    }
!{/for}


    // ------------------------------------------
    // -- Static Builders Functions for Root
    // ------------------------------------------
!{for method : fluentApi.rootFluentApiInterface.methods}
    public static ${method.returnType.typeDeclaration} ${method.name} (${method.parameterDeclarationString}){
        return new ${fluentApi.rootFluentApiInterface.getClassName}().${method.name}(${method.parameterNames});
    }
!{/for}

}

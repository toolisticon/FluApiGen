package ${ model.packageName };
!{for import : model.imports}
import ${import};
!{/for}

import java.util.Arrays;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * An empty class.
 */
public class ${ model.className } {

    // ----------------------------------------------------------------------
    // Backing Beans
    // ----------------------------------------------------------------------
!{for backingBean : model.backingBeans}

    private static class ${backingBean.className} implements ${backingBean.interfaceClassName} {

        // fields
!{for backingBeanField : backingBean.fields}
        ${backingBeanField.fieldType.typeDeclaration} ${backingBeanField.fieldName}${backingBeanField.initValueString};
!{/for}

        private ${backingBean.className} cloneBackingBean() {
            ${backingBean.className} thisClone = new ${backingBean.className}();
!{for backingBeanField : backingBean.fields}
!{if backingBeanField.isBackingBeanReference}!{if backingBeanField.isCollection}
            thisClone.${backingBeanField.fieldName} = this.${backingBeanField.fieldName} != null ? this.${backingBeanField.fieldName}.stream().map(e -> ((${backingBeanField.backingBeanReference.get})((${backingBeanField.backingBeanReferenceBackingBeanModel.className}) e).cloneBackingBean())).collect(Collectors.to${backingBeanField.fieldType.simpleName}()) : null;
!{else}
            thisClone.${backingBeanField.fieldName} = this.${backingBeanField.fieldName} != null ? this.${backingBeanField.fieldName}.cloneBackingBean() : null;
!{/if}!{elseif backingBeanField.isCloneable}
!{if backingBeanField.isCollection}
            thisClone.${backingBeanField.fieldName} = this.${backingBeanField.fieldName} != null ? this.${backingBeanField.fieldName}.stream().map(e -> ((${backingBeanField.backingBeanReference.get})((${backingBeanField.backingBeanReferenceBackingBeanModel.className}) e).clone())).collect(Collectors.to${backingBeanField.fieldType.simpleName}()) : null;
!{else}
            thisClone.${backingBeanField.fieldName} = this.${backingBeanField.fieldName} != null ? this.${backingBeanField.fieldName}.clone() : null;
!{/if}!{else}
!{if backingBeanField.isCollection}
            thisClone.${backingBeanField.fieldName} = this.${backingBeanField.fieldName} != null ? new ${backingBeanField.collectionImplType}(this.${backingBeanField.fieldName}) : null;
!{else}
            thisClone.${backingBeanField.fieldName} = this.${backingBeanField.fieldName};
!{/if}!{/if}!{/for}
            return thisClone;
        }

        //  add fields getters
!{for backingBeanField : backingBean.fields}
        public ${backingBeanField.getGetterMethodSignature} {
            return ${backingBeanField.fieldName};
        }
!{/for}
    }

!{/for}

    // ----------------------------------------------------------------------
    // Fluent Interfaces
    // ----------------------------------------------------------------------

!{for interface : model.fluentInterfaces}
    private static class ${interface.className} implements ${interface.interfaceClassName} {

        final ${interface.backingBeanModel.className} backingBean;

        final Deque<Object> parentStack;

        /**
         * constructor that initializes backing bean. (First call...)
         */
        private ${interface.className}(Deque parentStack) {
            this.backingBean = new ${interface.backingBeanModel.className}();
            this.parentStack = new ArrayDeque(parentStack);
        }


        private ${interface.className}(${interface.backingBeanModel.className} backingBean, Deque parentStack) {
            this.backingBean = backingBean.cloneBackingBean();
            this.parentStack = new ArrayDeque(parentStack);
        }

        // ----------------------------------------------------------------------
        // Methods
        // ----------------------------------------------------------------------

!{for method : interface.methods}
        @Override
        public ${method.methodSignature} {

!{if method.getHasSameTargetBackingBean}

            ${interface.backingBeanModel.className} nextBackingBean = this.backingBean.cloneBackingBean();

            // set values (both implicit and explicit) - parent stack stays untouched
!{for parameter : method.getAllParameters}
            nextBackingBean.${parameter.backingBeanField.get.fieldName} = ${parameter.assignmentString};
!{/for}
!{for implicitValue : method.implicitValuesBoundToCurrentBB}
            nextBackingBean.${implicitValue.backingBeanFieldName} = ${implicitValue.valueAssignmentString};
!{/for}
            return new ${method.nextModelInterface.className}(nextBackingBean, parentStack);

!{elseif method.isParentCall}

            // first get parent bb from stack (clone stack and pop)
            Deque newStack = new ArrayDeque(parentStack);

            // Must set fields to current backing bean (clone and set)
            ${interface.backingBeanModel.className} currentBackingBean = this.backingBean.cloneBackingBean();

!{for parameter : method.parametersBoundToCurrentBB}
            currentBackingBean.${parameter.backingBeanField.get.fieldName} = ${parameter.parameterName};
!{/for}
!{for implicitValue : method.implicitValuesBoundToCurrentBB}
            currentBackingBean.${implicitValue.backingBeanFieldName} = ${implicitValue.valueAssignmentString};
!{/for}

            // Must set fields to next backing bean (clone and set)
            ${method.nextModelInterface.backingBeanModel.className} nextBackingBean = ((${method.nextModelInterface.backingBeanModel.className}) newStack.pop()).cloneBackingBean();

!{for parameter : method.parametersBoundToNextBB}
            nextBackingBean.${parameter.backingBeanField.get.fieldName} = ${parameter.parameterName};
!{/for}
!{for implicitValue : method.implicitValuesBoundToNextBB}
            nextBackingBean.${implicitValue.backingBeanFieldName} = ${implicitValue.valueAssignmentString};
!{/for}

            // must set/add current backing bean to parent backing bean
!{if method.getParentsBackingBeanField.isCollection}
            if (nextBackingBean.${method.getParentsBackingBeanField.fieldName} == null) {
                nextBackingBean.${method.getParentsBackingBeanField.fieldName} = new ${method.parentsBackingBeanFieldCollectionImplType}<>();
            }
            nextBackingBean.${method.getParentsBackingBeanField.fieldName}.add(currentBackingBean);
!{else}
            nextBackingBean.${method.getParentsBackingBeanField.fieldName} = currentBackingBean;
!{/if}
            return new ${method.nextModelInterface.className}(nextBackingBean, newStack);

!{elseif method.isCreatingChildConfigCall}

            // must clone current backing bean and put it on the cloned stack ()
            Deque newStack = new ArrayDeque(parentStack);

            ${interface.backingBeanModel.className} currentBackingBean = this.backingBean.cloneBackingBean();

            // Must set fields to current backing bean (clone and set)
!{for parameter : method.parametersBoundToCurrentBB}
            currentBackingBean.${parameter.backingBeanField.get.fieldName} = ${parameter.parameterName};
!{/for}
!{for implicitValue : method.implicitValuesBoundToCurrentBB}
            currentBackingBean.${implicitValue.backingBeanFieldName} = ${implicitValue.valueAssignmentString};
!{/for}

            newStack.push(currentBackingBean);

            // create next backing bean
            ${method.nextModelInterface.backingBeanModel.className} nextBackingBean = new ${method.nextModelInterface.backingBeanModel.className}();

            // Must set fields to next backing bean (clone and set)
!{for parameter : method.parametersBoundToNextBB}
            nextBackingBean.${parameter.backingBeanField.get.fieldName} = ${parameter.parameterName};
!{/for}
!{for implicitValue : method.implicitValuesBoundToNextBB}
            nextBackingBean.${implicitValue.backingBeanFieldName} = ${implicitValue.valueAssignmentString};
!{/for}
            return new ${method.nextModelInterface.className}(nextBackingBean, newStack);
!{else}
            // THIS SHOULDN'T HAPPEN
            return null;
!{/if}
        }
!{/for}
        // ----------------------------------------------------------------------
        // Commands
        // ----------------------------------------------------------------------

!{for command : interface.commands}
        @Override
        public ${command.methodSignature} {

            ${interface.backingBeanModel.className} nextBackingBean = this.backingBean.cloneBackingBean();

!{for parameter : command.method.getAllParameters}
            nextBackingBean.${parameter.backingBeanField.get.fieldName} = ${parameter.parameterName};
!{/for}
!{for implicitValue : command.method.implicitValuesBoundToCurrentBB}
            nextBackingBean.${implicitValue.backingBeanFieldName} = ${implicitValue.valueAssignmentString};
!{/for}
            // call command
            !{if command.hasReturnType}return !{/if} ${command.commandMethod}(nextBackingBean);
        }
!{/for}
    }
!{/for}

    // static root functions
!{for method : model.rootInterface.methods}
    public static ${method.methodSignature} {
        return new ${model.rootInterface.className}(new ArrayDeque()).${method.methodCall};
    }
!{/for}
}


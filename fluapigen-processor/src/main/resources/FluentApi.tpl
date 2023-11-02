package ${ model.packageName };
!{for import : model.imports}
import ${import};
!{/for}

import java.util.Arrays;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Collections;
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
            thisClone.${backingBeanField.fieldName} = ${backingBeanField.backingBeanCloneValueAssignmentString}
!{/for}
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
    private static class ${interface.className} ${interface.typeParametersString} implements ${interface.interfaceClassName}${interface.typeParameterNamesString} {

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
!{for parameter : method.allParameters}
!{if parameter.hasValidators}
!{for validator : parameter.validators}
            if(!${validator.validatorExpression}.validate(${parameter.parameterName})) {
                throw new RuntimeException("Parameter ${parameter.parameterName} failed validation");
            }
!{/for}
!{/if}
!{/for}
!{if method.hasInlineBackingBeanMapping}
            ${method.inlineBackingBean.className} inlineBackingBean = new ${method.inlineBackingBean.className}();
!{for parameter : method.parametersBoundToInlineBB}
            inlineBackingBean.${parameter.backingBeanField.get.fieldName}${parameter.assignmentString}
!{/for}!{for implicitValue : method.implicitValuesBoundToInlineBB}
            inlineBackingBean.${implicitValue.backingBeanFieldName}${implicitValue.valueAssignmentString};
!{/for}
!{/if}
!{if method.isParentCall}
            // handle parent traversal
            // first clone stack
            Deque newStack = new ArrayDeque(parentStack);

            // clone and update values of backing bean
            ${interface.backingBeanModel.className} currentBackingBean = this.backingBean.cloneBackingBean();
!{for parameter : method.parametersBoundToCurrentBB}
            currentBackingBean.${parameter.backingBeanField.get.fieldName}${parameter.assignmentString}
!{/for}
!{for implicitValue : method.implicitValuesBoundToCurrentBB}
            currentBackingBean.${implicitValue.backingBeanFieldName}${implicitValue.valueAssignmentString};
!{/for}
!{if method.hasInlineBackingBeanMapping}
!{if method.inlineBackingBeanField.isCollection}
            currentBackingBean.${method.getInlineBackingBeanField.fieldName}.add(inlineBackingBean);
!{else}
            currentBackingBean.${method.getInlineBackingBeanField.fieldName} = inlineBackingBean;
!{/if}
!{/if}

            // prepare next backing bean and set values
!{for backingBeanField : method.getParentsBackingBeanFields}
            ${backingBeanField.nextBBTypeName}  ${backingBeanField.nextBBVariableName} = ((${backingBeanField.nextBBTypeName}) newStack.pop()).cloneBackingBean();
!{if backingBeanField.isFirst}
!{for parameter : method.parametersBoundToNextBB}
            ${backingBeanField.nextBBVariableName}.${parameter.backingBeanField.get.fieldName}${parameter.assignmentString}
!{/for}
!{for implicitValue : method.implicitValuesBoundToNextBB}
            ${backingBeanField.nextBBVariableName}.${implicitValue.backingBeanFieldName}${implicitValue.valueAssignmentString};
!{/for}
!{/if}

!{if backingBeanField.isCollection}
            ${backingBeanField.nextBBVariableName}.${backingBeanField.fieldName}.add(${backingBeanField.currentBBVariableName});
!{else}
            ${backingBeanField.nextBBVariableName}.${backingBeanField.fieldName} = ${backingBeanField.currentBBVariableName}.cloneBackingBean();
!{/if}

!{if backingBeanField.isLast}
!{if method.isCommandMethod}
            // call command
            !{if method.command.hasReturnType}return !{/if} ${method.command.commandMethod}(${backingBeanField.nextBBVariableName});
!{else}
            // init BB impl
            return new ${method.nextModelInterface.className}(${backingBeanField.nextBBVariableName}, newStack);
!{/if}
!{/if}
!{/for}

!{elseif method.getHasSameTargetBackingBean}
            // handle same bb traversal
            // clone and update values of backing bean
            ${interface.backingBeanModel.className} nextBackingBean = this.backingBean.cloneBackingBean();
!{if method.hasInlineBackingBeanMapping}
!{if method.inlineBackingBeanField.isCollection}
            nextBackingBean.${method.getInlineBackingBeanField.fieldName}.add(inlineBackingBean);
!{else}
            nextBackingBean.${method.getInlineBackingBeanField.fieldName} = inlineBackingBean;
!{/if}
!{/if}
!{for parameter : method.getNotInlineMappedParameters}
            nextBackingBean.${parameter.backingBeanField.get.fieldName}${parameter.assignmentString};
!{/for}
!{for implicitValue : method.implicitValuesBoundToCurrentBB}
            nextBackingBean.${implicitValue.backingBeanFieldName}${implicitValue.valueAssignmentString};
!{/for}
!{if method.isCommandMethod}
            // call command
            !{if method.command.hasReturnType}return !{/if} ${method.command.commandMethod}(nextBackingBean);
!{else}
            // init BB impl
            return new ${method.nextModelInterface.className}(nextBackingBean, parentStack);
!{/if}

!{elseif method.isCreatingChildConfigCall}
            // handle child traversal
            // update current backing bean and push it ontop of the stack
            Deque newStack = new ArrayDeque(parentStack);
            ${interface.backingBeanModel.className} currentBackingBean = this.backingBean.cloneBackingBean();
!{for parameter : method.parametersBoundToCurrentBB}
            currentBackingBean.${parameter.backingBeanField.get.fieldName}${parameter.assignmentString}
!{/for}
!{for implicitValue : method.implicitValuesBoundToCurrentBB}
            currentBackingBean.${implicitValue.backingBeanFieldName}${implicitValue.valueAssignmentString};
!{/for}
!{if method.hasInlineBackingBeanMapping}
!{if method.inlineBackingBeanField.isCollection}
            currentBackingBean.${method.getInlineBackingBeanField.fieldName}.add(inlineBackingBean);
!{else}
            currentBackingBean.${method.getInlineBackingBeanField.fieldName} = inlineBackingBean;
!{/if}
!{/if}
            newStack.push(currentBackingBean);

            // create next backing bean and apply values
            ${method.nextModelInterface.backingBeanModel.className} nextBackingBean = new ${method.nextModelInterface.backingBeanModel.className}();
!{for parameter : method.parametersBoundToNextBB}
            nextBackingBean.${parameter.backingBeanField.get.fieldName}${parameter.assignmentString}
!{/for}
!{for implicitValue : method.implicitValuesBoundToNextBB}
            nextBackingBean.${implicitValue.backingBeanFieldName}${implicitValue.valueAssignmentString};
!{/for}
            return new ${method.nextModelInterface.className}(nextBackingBean, newStack);
!{else}
            // THIS SHOULDN'T HAPPEN
            return null;
!{/if}
        }
!{/for}
    }
!{/for}

    // static root functions
!{for method : model.rootInterface.methods}
    public static ${method.methodSignature} {
        !{if !method.isCommandMethod || method.command.hasReturnType}return!{/if} new ${model.rootInterface.className}(new ArrayDeque()).${method.methodCall};
    }
!{/for}
}


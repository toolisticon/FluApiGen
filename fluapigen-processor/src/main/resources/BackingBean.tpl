package ${backingBean.packageName};
!{for import:backingBean.imports}
import ${import};
!{/for}

public class ${backingBean.className} implements ${interface} {

!{for field:backingBean.fields}
    private ${field.typeDeclaration} ${field.name};
!{/for}

    /**
     * No arg constructor.
     */
    public ${backingBean.className} (){

    }

    /**
     * Clone constructor.
     */
    public ${backingBean.className} (${backingBean.className} toClone){

!{for field:backingBean.fields}
!{if !field.isCollection && !field.isArray}
        !{if !field.isBackingBeanReference}this.${field.name} = toClone.${field.name};!{/if}
        !{if field.isBackingBeanReference}this.${field.name} = new ${field.typeDeclaration}(toClone.${field.name});!{/if}
!{/if}!{if field.isCollection}
        try{
            this.${field.name} = toClone.${field.name}.getClass().newInstance();
            for (${field.collectionType} element : toClone.${field.name}) {
                !{if !field.isBackingBeanReference}this.${field.name}.add(element);!{/if}
                !{if field.isBackingBeanReference}this.${field.name}.add([i] = )new ${field.collectionType}(element));!{/if}
            }
        } catch (Exception e) {
            // shouldn't happen
        }
!{/if}!{if field.isArray}
        this.${field.name} = new ${field.simpleType}[toClone.${field.name}.length];
        for (int i=0 ; i < toClone.${field.name}.length ; i++) {
            !{if !field.isBackingBeanReference}this.${field.name}[i] = toClone.${field.name}[i];!{/if}
            !{if field.isBackingBeanReference}this.${field.name}[i] = new ${field.typeDeclaration}(toClone.${field.name}[i]);!{/if}
        }
!{/if}!{/for}
    }

    // add getters and setters
!{for field:backingBean.fields}
    public ${field.typeDeclaration} ${field.getterName}(){
        return this.${field.name};
    }

    public void ${field.setterName}(${field.typeDeclaration} ${field.name}){
        this.${field.name} = ${field.name};
    }

!{/for}

}
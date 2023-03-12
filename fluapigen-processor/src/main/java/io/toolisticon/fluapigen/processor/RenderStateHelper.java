package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.compilermessage.api.DeclareCompilerMessage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class RenderStateHelper implements AutoCloseable {

    private static ThreadLocal<RenderStateHelper> renderStateHelperThreadLocal = new ThreadLocal<>();

    private Map<String, ModelBackingBean> backingBeanAccessMap = new HashMap<>();

    private Map<String, ModelInterface> modelInterfaceMap = new HashMap<>();

    private Map<ModelBackingBean, Set<ModelBackingBean>> backingBeanRelationMap;

    private Map<ModelBackingBean, ModelBackingBean> backingBeanParents = new HashMap<>();



    @Override
    public void close() throws Exception {
        renderStateHelperThreadLocal.remove();
    }

    public static RenderStateHelper create() {
        renderStateHelperThreadLocal.set(new RenderStateHelper());
        return renderStateHelperThreadLocal.get();
    }

    public static RenderStateHelper get() {
        return renderStateHelperThreadLocal.get();
    }





    public static void addBackingBeanModel(ModelBackingBean backingBeanModel) {
        get().backingBeanAccessMap.put(backingBeanModel.getBackingBeanInterfaceSimpleName(), backingBeanModel);
    }

    public static void addInterfaceModel(ModelInterface interfaceModel) {
        get().modelInterfaceMap.put(interfaceModel.interfaceClassSimpleName(), interfaceModel);
    }


    @DeclareCompilerMessage(code = "020", enumValueName = "FOUND_MULTIPLE_PARENTS_OF_BB", message = "Backing Bean ${} has multiple parent backing beans!", processorClass = FluentApiProcessor.class)
    public static void init () {
        // create backing bean relationship map (children)
        get().backingBeanRelationMap = getBackingBeanRelationshipMap();

        // create backing bean relationship map (parent)
        get().backingBeanParents = new HashMap<>();
        for (Map.Entry<ModelBackingBean, Set<ModelBackingBean>> children : get().backingBeanRelationMap.entrySet()) {
            for (ModelBackingBean child : children.getValue()) {
                if (get().backingBeanParents.containsKey(child)) {
                    if (!get().backingBeanParents.get(child).equals(children.getKey())) {
                        // Multiple parents !!!
                        child.wrapper.compilerMessage().asError().write(FluentApiProcessorCompilerMessages.FOUND_MULTIPLE_PARENTS_OF_BB, child.getBackingBeanInterfaceSimpleName());
                    }
                } else {
                    get().backingBeanParents.put(child, children.getKey());
                }
            }
        }

    }

    public static ModelBackingBean getBackingBeanModelForBackingBeanInterfaceSimpleName(String name){
        return get().backingBeanAccessMap.get(name);
    }



    public static ModelInterface getInterfaceModelForInterfaceSimpleClassName(String name){
        return get().modelInterfaceMap.get(name);
    }

    public static Optional<ModelInterface> getRootInterface() {
        for (ModelInterface interfaceModel : get().modelInterfaceMap.values()) {
            if (interfaceModel.isRootInterface()) {
                return Optional.of(interfaceModel);
            }
        }
        return Optional.empty();
    }

    private static Map<ModelBackingBean, Set<ModelBackingBean>> getBackingBeanRelationshipMap(){
        Map<ModelBackingBean, Set<ModelBackingBean>> map = new HashMap<>();

        // Lets start with root interfaces backing bean
        Optional<ModelInterface> rootInterface = getRootInterface();
        if(rootInterface.isPresent() && !map.containsKey(rootInterface.get().getBackingBeanModel())) {

            getBackingBeanRelationshipMapRecursively(map, rootInterface.get().getBackingBeanModel());

        }


        return map;
    }


    private static void getBackingBeanRelationshipMapRecursively(Map<ModelBackingBean, Set<ModelBackingBean>> map, ModelBackingBean backingBeanModel) {

        Set<ModelBackingBean> childrenBackingBeans = map.computeIfAbsent(backingBeanModel, e -> new HashSet<>());

        for (ModelBackingBeanField field : backingBeanModel.fields) {
            Optional<String> backingBeanReference = field.getBackingBeanReference();
            if (backingBeanReference.isPresent()) {
                ModelBackingBean referencedBackingBeanModel = getBackingBeanModelForBackingBeanInterfaceSimpleName(backingBeanReference.get());
                childrenBackingBeans.add(referencedBackingBeanModel);

                // break possible cycles
                if(!map.containsKey(referencedBackingBeanModel)) {

                    getBackingBeanRelationshipMapRecursively(map, referencedBackingBeanModel);

                }


            }
        }

    }


    public static ModelBackingBean getParentBB(ModelBackingBean modelBackingBean) {
        return get().backingBeanParents.get(modelBackingBean);
    }

}

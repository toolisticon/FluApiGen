package io.toolisticon.fluapigen.integrationtest;

import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.fluapigen.api.FluentApiBackingBean;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiCommand;
import io.toolisticon.fluapigen.api.FluentApiImplicitValue;
import io.toolisticon.fluapigen.api.FluentApiInlineBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiInterface;
import io.toolisticon.fluapigen.api.FluentApiParentBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiRoot;
import io.toolisticon.fluapigen.api.TargetBackingBean;

@FluentApi("InlineMappingIntegrationTestStarter")
public class InlineMappingIntegrationTest {

    @FluentApiBackingBean
    public interface InlineBB{

        String value1();

        String value2();

    }

    @FluentApiBackingBean
    public interface RootBB{

        MidBB midBB();




    }

    @FluentApiBackingBean
    public interface MidBB{

        InlineBB inlineBB();

        LowestBB lowestBB();

    }

    @FluentApiBackingBean
    public interface LowestBB{

        String lowValue();

    }


    @FluentApiInterface(RootBB.class)
    @FluentApiRoot
    public interface RootInterface{

        MidInterface goTo();

        @FluentApiCommand(MyCommand.class)
        RootBB close();

    }

    @FluentApiInterface(MidBB.class)

    public interface MidInterface{


        @FluentApiInlineBackingBeanMapping(value="inlineBB")
        @FluentApiImplicitValue(id="value1", value="!!!", target = TargetBackingBean.INLINE)
        MidInterface addInline(@FluentApiBackingBeanMapping(value="value2",target = TargetBackingBean.INLINE) String value);


        @FluentApiParentBackingBeanMapping(value = "midBB")
        @FluentApiInlineBackingBeanMapping(value="inlineBB")
        @FluentApiImplicitValue(id="value1", value="!!!", target = TargetBackingBean.INLINE)
        RootInterface addInlineToParent(@FluentApiBackingBeanMapping(value="value2",target = TargetBackingBean.INLINE) String value);

        @FluentApiInlineBackingBeanMapping(value="inlineBB")
        @FluentApiImplicitValue(id="value1", value="!!!", target = TargetBackingBean.INLINE)
        LowestInterface addInlineToChild(@FluentApiBackingBeanMapping(value="value2",target = TargetBackingBean.INLINE) String value);


        @FluentApiParentBackingBeanMapping(value = "midBB")
        RootInterface add();
    }

    @FluentApiInterface(LowestBB.class)
    public interface LowestInterface{

        @FluentApiParentBackingBeanMapping(value = "lowestBB")
        MidInterface add(@FluentApiBackingBeanMapping("lowValue") String value);

    }


    @FluentApiCommand
    static class MyCommand{

        static RootBB myCommand(RootBB rootBB){
            return rootBB;
        }

    }


}

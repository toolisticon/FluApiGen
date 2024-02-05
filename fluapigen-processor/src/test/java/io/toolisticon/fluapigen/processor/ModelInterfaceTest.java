package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.common.ToolingProvider;
import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.aptk.tools.TypeUtils;
import io.toolisticon.cute.Cute;
import io.toolisticon.cute.UnitTestWithoutPassIn;
import io.toolisticon.fluapigen.api.FluentApiBackingBean;
import io.toolisticon.fluapigen.api.FluentApiCommand;
import io.toolisticon.fluapigen.api.FluentApiInterface;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Unit tests for {@link ModelInterface}
 */
public class ModelInterfaceTest {


    @Before
    public void init() {
        MessagerUtils.setPrintMessageCodes(true);
    }


    @FluentApiBackingBean
    interface MyBB {

    }

    @FluentApiInterface(MyBB.class)
    interface MyFluentInterface<V, C extends Collection<V>> {

    }

    @FluentApiInterface(MyBB.class)
    interface MyFluentInterfaceWithMultipleExtendsBounds<V, C extends Collection<V> & Serializable> {

        MyFluentInterfaceWithMultipleExtendsBounds setValue(InputStream inputStream);

        @FluentApiCommand(MyCommand.class)
        List<String> myCommand();


    }

    @FluentApiCommand
    static class MyCommand {
        public static List<String> myCommand(MyBB myBB) {
            return new ArrayList<>();
        }
    }

    @Test
    public void getTypeParameterNamesString() {

        Cute.unitTest().when(new UnitTestWithoutPassIn() {
                    @Override
                    public void unitTest(ProcessingEnvironment processingEnvironment) {

                        ToolingProvider.setTooling(processingEnvironment);
                        try {

                            RenderStateHelper.create();

                            TypeElement backingBeanElement = TypeUtils.TypeRetrieval.getTypeElement(MyBB.class);
                            TypeElement fluentApiElement = TypeUtils.TypeRetrieval.getTypeElement(MyFluentInterface.class);


                            ModelInterface unit = new ModelInterface(FluentApiInterfaceWrapper.wrap(fluentApiElement), new ModelBackingBean(FluentApiBackingBeanWrapper.wrap(backingBeanElement)));
                            MatcherAssert.assertThat(unit.getTypeParameterNamesString(), Matchers.is("<V, C>"));
                        } finally {
                            ToolingProvider.clearTooling();
                        }

                    }
                })
                .thenExpectThat().compilationSucceeds()
                .executeTest();


    }

    @Test
    public void getTypeParametersString() {

        Cute.unitTest().<TypeElement>when(new UnitTestWithoutPassIn() {
                    @Override
                    public void unitTest(ProcessingEnvironment processingEnvironment) {

                        ToolingProvider.setTooling(processingEnvironment);
                        try {
                            RenderStateHelper.create();

                            TypeElement backingBeanElement = TypeUtils.TypeRetrieval.getTypeElement(MyBB.class);
                            TypeElement fluentApiElement = TypeUtils.TypeRetrieval.getTypeElement(MyFluentInterface.class);


                            ModelInterface unit = new ModelInterface(FluentApiInterfaceWrapper.wrap(fluentApiElement), new ModelBackingBean(FluentApiBackingBeanWrapper.wrap(backingBeanElement)));
                            MatcherAssert.assertThat(unit.getTypeParametersString(), Matchers.is("<V, C extends Collection<V>>"));
                        } finally {
                            ToolingProvider.clearTooling();
                        }

                    }
                })
                .thenExpectThat().compilationSucceeds()
                .executeTest();


    }

    @Test
    public void getTypeParametersString_WithMultipleExtendsBounds() {
        Cute.unitTest().<TypeElement>when(new UnitTestWithoutPassIn() {
                    @Override
                    public void unitTest(ProcessingEnvironment processingEnvironment) {

                        ToolingProvider.setTooling(processingEnvironment);
                        try {
                            RenderStateHelper.create();

                            TypeElement backingBeanElement = TypeUtils.TypeRetrieval.getTypeElement(MyBB.class);
                            TypeElement fluentApiElement = TypeUtils.TypeRetrieval.getTypeElement(MyFluentInterfaceWithMultipleExtendsBounds.class);

                            ModelInterface unit = new ModelInterface(FluentApiInterfaceWrapper.wrap(fluentApiElement), new ModelBackingBean(FluentApiBackingBeanWrapper.wrap(backingBeanElement)));
                            MatcherAssert.assertThat(unit.getTypeParametersString(), Matchers.is("<V, C extends Collection<V> & Serializable>"));
                        } finally {
                            ToolingProvider.clearTooling();
                        }
                    }
                })
                .thenExpectThat().compilationSucceeds()
                .executeTest();

    }

    @Test
    public void getImports() {

        Cute.unitTest().<TypeElement>when(new UnitTestWithoutPassIn() {
                    @Override
                    public void unitTest(ProcessingEnvironment processingEnvironment) {

                        ToolingProvider.setTooling(processingEnvironment);
                        try {
                            RenderStateHelper.create();

                            TypeElement backingBeanElement = TypeUtils.TypeRetrieval.getTypeElement(MyBB.class);
                            TypeElement fluentApiElement = TypeUtils.TypeRetrieval.getTypeElement(MyFluentInterfaceWithMultipleExtendsBounds.class);

                            ModelInterface unit = new ModelInterface(FluentApiInterfaceWrapper.wrap(fluentApiElement), new ModelBackingBean(FluentApiBackingBeanWrapper.wrap(backingBeanElement)));
                            MatcherAssert.assertThat(unit.fetchImports(), Matchers.containsInAnyOrder(List.class.getCanonicalName(), MyFluentInterfaceWithMultipleExtendsBounds.class.getCanonicalName(), MyCommand.class.getCanonicalName(), InputStream.class.getCanonicalName(), Collection.class.getCanonicalName(), Serializable.class.getCanonicalName()));
                        } finally {
                            ToolingProvider.clearTooling();
                        }
                    }
                }).thenExpectThat().compilationSucceeds()
                .executeTest();
    }

}

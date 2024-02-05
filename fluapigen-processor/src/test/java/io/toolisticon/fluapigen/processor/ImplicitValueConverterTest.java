package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.common.ToolingProvider;
import io.toolisticon.aptk.tools.InterfaceUtils;
import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.aptk.tools.TypeMirrorWrapper;
import io.toolisticon.aptk.tools.wrapper.TypeElementWrapper;
import io.toolisticon.cute.Cute;
import io.toolisticon.cute.CuteApi;
import io.toolisticon.cute.UnitTestWithoutPassIn;
import io.toolisticon.fluapigen.api.FluentApiConverter;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.processing.ProcessingEnvironment;
import java.util.List;

public class ImplicitValueConverterTest {

    CuteApi.UnitTestRootInterface unitTestBuilder;


    public static class TargetType {

        private final String value;

        public TargetType (String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    public static class MyConverter implements FluentApiConverter<String,TargetType> {

        @Override
        public TargetType convert(String o) {
            return new TargetType(o);
        }

    }

    @Before
    public void init() {
        MessagerUtils.setPrintMessageCodes(true);

        unitTestBuilder = Cute
                .unitTest();
    }

    @Test
    public void test() {
        unitTestBuilder.when(new UnitTestWithoutPassIn() {
            @Override
            public void unitTest(ProcessingEnvironment processingEnvironment) {
                try {
                    ToolingProvider.setTooling(processingEnvironment);

                    List<TypeMirrorWrapper> typeParameters = InterfaceUtils.getResolvedTypeArgumentOfSuperTypeOrInterface(TypeElementWrapper.getByClass(MyConverter.class).get(), TypeMirrorWrapper.wrap(FluentApiConverter.class));

                    System.out.println("XX");

                } finally {
                    ToolingProvider.clearTooling();
                }


            }
        }).executeTest();
    }


}

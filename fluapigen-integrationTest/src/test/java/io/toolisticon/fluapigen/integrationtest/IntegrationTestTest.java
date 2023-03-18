package io.toolisticon.fluapigen.integrationtest;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

public class IntegrationTestTest {

    @Test
    public void test_fluentApi_onlyRootLevel() {

        IntegrationTest.MyRootLevelBackingBean backingBean = IntegrationTestStarter.setName("NAME").myCommand();
        MatcherAssert.assertThat(backingBean.getName(), Matchers.is("NAME"));
        MatcherAssert.assertThat(backingBean.midLevelBB(), Matchers.nullValue());

    }

    @Test
    public void test_fluentApi_onlyRootLevel_withAlternativeInterface() {

        IntegrationTest.MyRootLevelBackingBean backingBean = IntegrationTestStarter
                .setName("NAME")
                .gotoAlternativeInterface()
                .closingCommand();
        MatcherAssert.assertThat(backingBean.getName(), Matchers.is("NAME"));

    }

    @Test
    public void test_fluentApi_onlyRootLevel_withAlternativeInterface_and_implicitValue() {

        IntegrationTest.MyRootLevelBackingBean backingBean = IntegrationTestStarter
                .setName("NAME")
                .myCommandWithImplicitValue();
        MatcherAssert.assertThat(backingBean.getName(), Matchers.is("IT WORKS!!!"));

    }

    @Test
    public void test_fluentApi_onlyRootLevel_withAlternativeInterface_and_closingCommandWithParameter() {

        IntegrationTest.MyRootLevelBackingBean backingBean = IntegrationTestStarter
                .setName("NAME").myCommandWithParameter("YES");
        MatcherAssert.assertThat(backingBean.getName(), Matchers.is("YES"));

    }

    @Test
    public void test_fluentApi_gotoLowerLevels_dontTouchLowLevels() {

        IntegrationTest.MyRootLevelBackingBean backingBean = IntegrationTestStarter
                .setName("NAME")
                .gotoMidLevel()
                .gotoParent()
                .myCommand();
        MatcherAssert.assertThat(backingBean.getName(), Matchers.is("NAME"));
        MatcherAssert.assertThat(backingBean.midLevelBB(), Matchers.notNullValue());
        MatcherAssert.assertThat(backingBean.midLevelBB(), Matchers.hasSize(1));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB(), Matchers.nullValue());
    }

    @Test
    public void test_fluentApi_gotoLowerLevels_withImplicitelyPassingStringValue() {

        IntegrationTest.MyRootLevelBackingBean backingBean = IntegrationTestStarter
                .setName("NAME")
                .gotoMidLevel()
                .addConfigWithImplicitStringValue()
                .closeWithoutValues()
                .gotoParent()
                .myCommand();
        MatcherAssert.assertThat(backingBean.getName(), Matchers.is("NAME"));
        MatcherAssert.assertThat(backingBean.midLevelBB(), Matchers.notNullValue());
        MatcherAssert.assertThat(backingBean.midLevelBB(), Matchers.hasSize(1));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB(), Matchers.notNullValue());
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getStringValue(), Matchers.is("IMPLICIT_PASSED"));
    }

    @Test
    public void test_fluentApi_gotoLowerLevels_withExplicitelyPassingStringValue() {

        IntegrationTest.MyRootLevelBackingBean backingBean = IntegrationTestStarter
                .setName("NAME")
                .gotoMidLevel()
                .addConfigWithPassingStringValue("DONE")
                .closeWithoutValues()
                .gotoParent()
                .myCommand();
        MatcherAssert.assertThat(backingBean.getName(), Matchers.is("NAME"));
        MatcherAssert.assertThat(backingBean.midLevelBB(), Matchers.notNullValue());
        MatcherAssert.assertThat(backingBean.midLevelBB(), Matchers.hasSize(1));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB(), Matchers.notNullValue());
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getStringValue(), Matchers.is("DONE"));
    }

    @Test
    public void test_fluentApi_gotoLowerLevels_withImplicitClose() {

        IntegrationTest.MyRootLevelBackingBean backingBean = IntegrationTestStarter
                .setName("NAME")
                .gotoMidLevel()
                .addConfig()
                .closeWithImplicit()
                .gotoParent()
                .myCommand();

        MatcherAssert.assertThat(backingBean.getName(), Matchers.is("NAME"));
        MatcherAssert.assertThat(backingBean.midLevelBB(), Matchers.notNullValue());
        MatcherAssert.assertThat(backingBean.midLevelBB(), Matchers.hasSize(1));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB(), Matchers.notNullValue());

        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getStringValue(), Matchers.is("a"));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getBooleanValue(), Matchers.is(true));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getPrimitiveBooleanValue(), Matchers.is(true));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getIntValue(), Matchers.is(2));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getPrimitiveIntValue(), Matchers.is(1));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getLongValue(), Matchers.is(4L));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getPrimitiveLongValue(), Matchers.is(3L));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getFloatValue(), Matchers.is(2.0f));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getPrimitiveFloatValue(), Matchers.is(1.0f));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getDoubleValue(), Matchers.is(4.0));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getPrimitiveDoubleValue(), Matchers.is(3.0));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getEnum(), Matchers.is(IntegrationTest.TestEnum.TWO));

    }

    @Test
    public void test_fluentApi_gotoLowerLevels_withCloseWithParameters() {

        IntegrationTest.MyRootLevelBackingBean backingBean = IntegrationTestStarter
                .setName("NAME")
                .gotoMidLevel()
                .addConfig()
                .closeWithParameters(
                        "ABC",
                        true,
                        true,
                        1,
                        2,
                        3,
                        4,
                        5.0f,
                        6.0f,
                        7.0,
                        8.0,
                        IntegrationTest.TestEnum.ONE
                )
                .gotoParent()
                .myCommand();

        MatcherAssert.assertThat(backingBean.getName(), Matchers.is("NAME"));
        MatcherAssert.assertThat(backingBean.midLevelBB(), Matchers.notNullValue());
        MatcherAssert.assertThat(backingBean.midLevelBB(), Matchers.hasSize(1));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB(), Matchers.notNullValue());

        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getStringValue(), Matchers.is("ABC"));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getBooleanValue(), Matchers.is(true));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getPrimitiveBooleanValue(), Matchers.is(true));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getIntValue(), Matchers.is(2));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getPrimitiveIntValue(), Matchers.is(1));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getLongValue(), Matchers.is(4L));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getPrimitiveLongValue(), Matchers.is(3L));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getFloatValue(), Matchers.is(6.0f));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getPrimitiveFloatValue(), Matchers.is(5.0f));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getDoubleValue(), Matchers.is(8.0));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getPrimitiveDoubleValue(), Matchers.is(7.0));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getEnum(), Matchers.is(IntegrationTest.TestEnum.ONE));

    }

    @Test
    public void test_fluentApi_gotoLowerLevels_withExplicitelySettingValues() {

        IntegrationTest.MyRootLevelBackingBean backingBean = IntegrationTestStarter
                .setName("NAME")
                .gotoMidLevel()
                .addConfig()
                .getStringValue("ABC")
                .setBooleanValue(true)
                .setPrimitiveBooleanValue(true)
                .setPrimitiveIntValue(1)
                .setIntValue(2)
                .setPrimitiveLongValue(3L)
                .setLongValue(4L)
                .setPrimitiveFloatValue(5.0f)
                .setFloatValue(6.0f)
                .setPrimitiveDoubleValue(7.0)
                .setDoubleValue(8.0)
                .setEnum(IntegrationTest.TestEnum.ONE)
                .closeWithoutValues()
                .gotoParent()
                .myCommand();

        MatcherAssert.assertThat(backingBean.getName(), Matchers.is("NAME"));
        MatcherAssert.assertThat(backingBean.midLevelBB(), Matchers.notNullValue());
        MatcherAssert.assertThat(backingBean.midLevelBB(), Matchers.hasSize(1));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB(), Matchers.notNullValue());

        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getStringValue(), Matchers.is("ABC"));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getBooleanValue(), Matchers.is(true));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getPrimitiveBooleanValue(), Matchers.is(true));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getIntValue(), Matchers.is(2));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getPrimitiveIntValue(), Matchers.is(1));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getLongValue(), Matchers.is(4L));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getPrimitiveLongValue(), Matchers.is(3L));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getFloatValue(), Matchers.is(6.0f));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getPrimitiveFloatValue(), Matchers.is(5.0f));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getDoubleValue(), Matchers.is(8.0));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getPrimitiveDoubleValue(), Matchers.is(7.0));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getEnum(), Matchers.is(IntegrationTest.TestEnum.ONE));

    }

    @Test
    public void test_fluentApi_gotoLowerLevels_withOutSettingValues() {

        IntegrationTest.MyRootLevelBackingBean backingBean = IntegrationTestStarter
                .setName("NAME")
                .gotoMidLevel()
                .addConfig()
                .closeWithoutValues()
                .gotoParent()
                .myCommand();

        MatcherAssert.assertThat(backingBean.getName(), Matchers.is("NAME"));
        MatcherAssert.assertThat(backingBean.midLevelBB(), Matchers.notNullValue());
        MatcherAssert.assertThat(backingBean.midLevelBB(), Matchers.hasSize(1));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB(), Matchers.notNullValue());

        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getStringValue(), Matchers.nullValue());
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getBooleanValue(), Matchers.nullValue());
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getPrimitiveBooleanValue(), Matchers.is(false));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getIntValue(), Matchers.nullValue());
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getPrimitiveIntValue(), Matchers.is(0));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getLongValue(), Matchers.nullValue());
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getPrimitiveLongValue(), Matchers.is(0L));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getFloatValue(), Matchers.nullValue());
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getPrimitiveFloatValue(), Matchers.is(0.0f));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getDoubleValue(), Matchers.nullValue());
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getPrimitiveDoubleValue(), Matchers.is(0.0));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).getLowLevelBB().get(0).getEnum(), Matchers.nullValue());

    }

    @Test
    public void test_fluentApi_gotoMidLevel_setStringArrayViaVarargs() {

        IntegrationTest.MyRootLevelBackingBean backingBean = IntegrationTestStarter
                .setName("NAME")
                .gotoMidLevel()
                .setStringArray("XXX")
                .setStringArray("ABC", "DEF","GHI")
                .gotoParent()
                .myCommand();

        MatcherAssert.assertThat(backingBean.getName(), Matchers.is("NAME"));
        MatcherAssert.assertThat(backingBean.midLevelBB(), Matchers.notNullValue());
        MatcherAssert.assertThat(backingBean.midLevelBB(), Matchers.hasSize(1));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).stringArray(), Matchers.arrayContaining("ABC", "DEF","GHI"));


    }

    @Test
    public void test_fluentApi_gotoMidLevel_setStringArrayInDifferentWaysAndCheckImmutability() {

        IntegrationTest.MyMidLevelInterface midLevel1 = IntegrationTestStarter
                .setName("NAME")
                .gotoMidLevel();

        MatcherAssert.assertThat(midLevel1.gotoParent().myCommand().midLevelBB().get(0).stringArray(), Matchers.arrayContaining("abc", "DEF"));

        IntegrationTest.MyMidLevelInterface midLevel2 = midLevel1.setStringArrayImplicitly();

        MatcherAssert.assertThat(midLevel1.gotoParent().myCommand().midLevelBB().get(0).stringArray(), Matchers.arrayContaining("abc", "DEF"));
        MatcherAssert.assertThat(midLevel2.gotoParent().myCommand().midLevelBB().get(0).stringArray(), Matchers.arrayContaining("XYZ","123"));


        IntegrationTest.MyMidLevelInterface midLevel3 = midLevel2.setStringArray("ABC", "DEF","GHI");

        MatcherAssert.assertThat(midLevel1.gotoParent().myCommand().midLevelBB().get(0).stringArray(), Matchers.arrayContaining("abc", "DEF"));
        MatcherAssert.assertThat(midLevel2.gotoParent().myCommand().midLevelBB().get(0).stringArray(), Matchers.arrayContaining("XYZ","123"));
        MatcherAssert.assertThat(midLevel3.gotoParent().myCommand().midLevelBB().get(0).stringArray(), Matchers.arrayContaining("ABC", "DEF","GHI"));

    }

    @Test
    public void test_fluentApi_gotoMidLevel_setStringListViaVarargs() {

        IntegrationTest.MyRootLevelBackingBean backingBean = IntegrationTestStarter
                .setName("NAME")
                .gotoMidLevel()
                .setStringList("XXX")
                .setStringList("ABC", "DEF","GHI")
                .gotoParent()
                .myCommand();

        MatcherAssert.assertThat(backingBean.getName(), Matchers.is("NAME"));
        MatcherAssert.assertThat(backingBean.midLevelBB(), Matchers.notNullValue());
        MatcherAssert.assertThat(backingBean.midLevelBB(), Matchers.hasSize(1));
        MatcherAssert.assertThat(backingBean.midLevelBB().get(0).stringList(), Matchers.contains("ABC", "DEF","GHI"));


    }

    @Test
    public void test_fluentApi_gotoMidLevel_setStringListInDifferentWaysAndCheckImmutability() {

        IntegrationTest.MyMidLevelInterface midLevel1 = IntegrationTestStarter
                .setName("NAME")
                .gotoMidLevel();

        MatcherAssert.assertThat(midLevel1.gotoParent().myCommand().midLevelBB().get(0).stringList(), Matchers.empty());

        IntegrationTest.MyMidLevelInterface midLevel2 = midLevel1.setStringListImplicitly();

        MatcherAssert.assertThat(midLevel1.gotoParent().myCommand().midLevelBB().get(0).stringList(), Matchers.empty());
        MatcherAssert.assertThat(midLevel2.gotoParent().myCommand().midLevelBB().get(0).stringList(), Matchers.contains("XYZ","123"));

        IntegrationTest.MyMidLevelInterface midLevel3 = midLevel2.setStringList("ABC", "DEF","GHI");

        MatcherAssert.assertThat(midLevel1.gotoParent().myCommand().midLevelBB().get(0).stringList(), Matchers.empty());
        MatcherAssert.assertThat(midLevel2.gotoParent().myCommand().midLevelBB().get(0).stringList(), Matchers.contains("XYZ","123"));
        MatcherAssert.assertThat(midLevel3.gotoParent().myCommand().midLevelBB().get(0).stringList(), Matchers.contains("ABC", "DEF","GHI"));

    }

}

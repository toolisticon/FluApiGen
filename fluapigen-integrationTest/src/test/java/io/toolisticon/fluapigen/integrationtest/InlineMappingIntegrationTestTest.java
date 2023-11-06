package io.toolisticon.fluapigen.integrationtest;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Integration test for {@link InlineMappingIntegrationTest}.
 */
public class InlineMappingIntegrationTestTest {

    @Test
    public void testSameBB() {

        InlineMappingIntegrationTest.RootBB rootBB = InlineMappingIntegrationTestStarter.goTo().addInline("===").add().close();

        MatcherAssert.assertThat(rootBB.midBB().inlineBB(), Matchers.notNullValue());
        MatcherAssert.assertThat(rootBB.midBB().inlineBB().value1(), Matchers.is("!!!"));
        MatcherAssert.assertThat(rootBB.midBB().inlineBB().value2(), Matchers.is("==="));

    }

    @Test
    public void testToParentBB() {

        InlineMappingIntegrationTest.RootBB rootBB = InlineMappingIntegrationTestStarter.goTo().addInlineToParent("///").close();

        MatcherAssert.assertThat(rootBB.midBB().inlineBB(), Matchers.notNullValue());
        MatcherAssert.assertThat(rootBB.midBB().inlineBB().value1(), Matchers.is("!!!"));
        MatcherAssert.assertThat(rootBB.midBB().inlineBB().value2(), Matchers.is("///"));

    }

    @Test
    public void testToChildBB() {

        InlineMappingIntegrationTest.RootBB rootBB = InlineMappingIntegrationTestStarter.goTo().addInlineToChild("+++").add("CHILD").add().close();

        MatcherAssert.assertThat(rootBB.midBB().inlineBB(), Matchers.notNullValue());
        MatcherAssert.assertThat(rootBB.midBB().inlineBB().value1(), Matchers.is("!!!"));
        MatcherAssert.assertThat(rootBB.midBB().inlineBB().value2(), Matchers.is("+++"));
        MatcherAssert.assertThat(rootBB.midBB().lowestBB().lowValue(), Matchers.is("CHILD"));

    }


}

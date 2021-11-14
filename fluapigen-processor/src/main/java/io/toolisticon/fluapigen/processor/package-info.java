/**
 * This package contains the fluapigen annotation processor.
 */
@AnnotationWrapper(value = {FluentApi.class, BackingBeanField.class, BackingBeanFieldRef.class, BackingBean.class, FluentApiInterface.class, FinishingCommand.class, FinishingCommandId.class}, usePublicVisibility = true)
package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.annotationwrapper.api.AnnotationWrapper;
import io.toolisticon.fluapigen.api.BackingBean;
import io.toolisticon.fluapigen.api.BackingBeanField;
import io.toolisticon.fluapigen.api.BackingBeanFieldRef;
import io.toolisticon.fluapigen.api.FinishingCommand;
import io.toolisticon.fluapigen.api.FinishingCommandId;
import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.fluapigen.api.FluentApiInterface;
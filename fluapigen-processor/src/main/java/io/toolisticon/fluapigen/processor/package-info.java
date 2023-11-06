/**
 * This package contains the fluapigen annotation processor.
 */
@AnnotationWrapper(
        value = {
                FluentApi.class,
                FluentApiInterface.class,
                FluentApiCommand.class,
                FluentApiBackingBean.class,
                FluentApiBackingBeanField.class,
                FluentApiImplicitValue.class,
                FluentApiBackingBeanMapping.class,
                FluentApiParentBackingBeanMapping.class,
                FluentApiInlineBackingBeanMapping.class,
                FluentApiValidator.class
        })
package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.annotationwrapper.api.AnnotationWrapper;
import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.fluapigen.api.FluentApiBackingBean;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanField;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiCommand;
import io.toolisticon.fluapigen.api.FluentApiImplicitValue;
import io.toolisticon.fluapigen.api.FluentApiInlineBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiInterface;
import io.toolisticon.fluapigen.api.FluentApiParentBackingBeanMapping;
import io.toolisticon.fluapigen.validation.api.FluentApiValidator;
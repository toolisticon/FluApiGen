/**
 * This package contains the fluapigen annotation processor.
 */
@AnnotationWrapper(value={FluentApi.class, FluentApiInterface.class, FluentApiCommand.class, FluentApiBackingBean.class, FluentApiImplicitValue.class})
package io.toolisticon.fluapigen.processor;

import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.aptk.annotationwrapper.api.AnnotationWrapper;
import io.toolisticon.fluapigen.api.FluentApiBackingBean;
import io.toolisticon.fluapigen.api.FluentApiCommand;
import io.toolisticon.fluapigen.api.FluentApiImplicitValue;
import io.toolisticon.fluapigen.api.FluentApiInterface;
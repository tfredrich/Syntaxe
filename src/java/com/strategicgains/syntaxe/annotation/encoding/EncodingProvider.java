/*
    Copyright 2011, Strategic Gains, Inc.

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/
package com.strategicgains.syntaxe.annotation.encoding;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.strategicgains.syntaxe.encoding.XssEncoder;

/**
 * This annotation is usually applied to the encoding annotations
 * in this package. However, if one of those annotations doesn't
 * cover what you need, apply this annotation to a Field, assigning
 * your own XssEncoder implementation as the value.
 *  
 * @author toddf
 * @since Mar 5, 2014
 */
@Target({ANNOTATION_TYPE, FIELD})
@Retention(RUNTIME)
public @interface EncodingProvider
{
	Class<? extends XssEncoder> value();
}

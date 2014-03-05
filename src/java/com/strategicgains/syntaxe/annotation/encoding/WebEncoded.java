/*
    Copyright 2014, Strategic Gains, Inc.

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

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.strategicgains.syntaxe.encoding.owasp.WebEncoder;

/**
 * Annotation to encode a field to help protect against JavaScript as well as HTML XSS attacks.
 * 
 * @author toddf
 * @since Mar 4, 2014
 */
@Target(FIELD)
@Retention(RUNTIME)
@EncodingProvider(WebEncoder.class)
public @interface WebEncoded
{
}

/*
    Copyright 2010, Strategic Gains, Inc.

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
package com.strategicgains.syntaxe.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.strategicgains.syntaxe.validator.impl.StringValidator;

/**
 * Annotations for domain property validations.
 * 
 * @author toddf
 * @since Oct 7, 2010
 */
@Target(FIELD)
@Retention(RUNTIME)
@ValidationProvider(StringValidator.class)
public @interface StringValidation
{
	/**
	 * The name of the property to use in error messages. Defaults to the property name.
	 */
	String name() default "";

	/**
	 * True if a value for the property is necessary.
	 */
	boolean required() default false;

	/**
	 * The minimum number of characters in the property. Must be greater-than or equal-to zero (0).
	 */
	int minLength() default -1;

	/**
	 * The maximum number of characters in the property. Must be greater-than or equal-to zero (0).
	 */
	int maxLength() default -1;

	/**
	 * A regular expression pattern to use in validating the property. Must be a valid Java Regex.
	 */
	String pattern() default "";

	/**
	 * A message to use (appended to the name() setting) only if the pattern() validation fails.
	 */
	String message() default "";
}

/*
    Copyright 2020, Strategic Gains, Inc.

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

import com.strategicgains.syntaxe.validator.impl.DateValidator;

/**
 * @author toddf
 * @since Jun 30, 2020
 */
@Target(FIELD)
@Retention(RUNTIME)
@ValidationProvider(DateValidator.class)
public @interface DateValidation
{
	String name() default "";


	/**
	 * True if a value for the date is necessary.
	 */
	boolean required() default false;

	/**
	 * True to enforce the annotated date to be in the past.
	 */
	boolean past() default false;

	/**
	 * True to enforce the annotated date to be in the present (ignores the time component).
	 * Optionally, use in conjunction with past() or future() to enforce present-or-past and
	 * present-or-future ranges, respectively.
	 */
	boolean present() default false;

	/**
	 * True to enforce the annotated date to be in the future.
	 */
	boolean future() default false;
}

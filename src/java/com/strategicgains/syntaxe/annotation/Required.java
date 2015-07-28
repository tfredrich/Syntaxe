/*
    Copyright 2013, Strategic Gains, Inc.

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

import com.strategicgains.syntaxe.validator.impl.RequiredValidator;

/**
 * Required annotation for field validation.  Verifies that a field is non-null.
 * If the field is an array, verifies that the array is non-null and that each
 * element in the array is non-null.
 * <p>
 * The 'value' property for this annotation is optional, and contains the name 
 * that will be used in error messages.  If it is not set, the name of the property
 * in the underlying object is used.
 * </p>
 * @author toddf
 * @since Apr 3, 2013
 */
@Target(FIELD)
@Retention(RUNTIME)
@ValidationProvider(RequiredValidator.class)
public @interface Required
{
	String value() default "";
}

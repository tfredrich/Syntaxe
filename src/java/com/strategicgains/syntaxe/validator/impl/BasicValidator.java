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
package com.strategicgains.syntaxe.validator.impl;

import java.lang.reflect.Field;
import java.util.List;

import com.strategicgains.syntaxe.annotation.BasicValidate;
import com.strategicgains.syntaxe.util.Validations;
import com.strategicgains.syntaxe.validator.AnnotatedFieldValidator;

/**
 * @author toddf
 * @since Mar 17, 2011
 */
public class BasicValidator
extends AnnotatedFieldValidator<BasicValidate>
{
	/**
     * @param field
     * @param annotation
     */
    public BasicValidator(Field field, BasicValidate annotation)
    {
	    super(field, annotation);
    }

    @Override
    public void perform(Object instance, List<String> errors)
    {
		String name = determineName();
		Object value = getValue(instance);

		if (getAnnotation().required())
		{
			String stringValue = (value == null ? null : String.valueOf(value));
			Validations.require(name, stringValue, errors);
		}

		if (getAnnotation().minLength() > 0)
		{
			String stringValue = (value == null ? null : String.valueOf(value));
			Validations.minLength(name, stringValue, getAnnotation().minLength(), errors);
		}

		if (getAnnotation().maxLength() > 0)
		{
			String stringValue = (value == null ? null : String.valueOf(value));
			Validations.maxLength(name, stringValue, getAnnotation().maxLength(), errors);
		}

		if (getAnnotation().min() != Integer.MIN_VALUE)
		{
			int intValue = ((Integer) value).intValue();
			Validations.greaterThanOrEqual(name, intValue, getAnnotation().min(), errors);
		}

		if (getAnnotation().max() != Integer.MAX_VALUE)
		{
			int intValue = ((Integer) value).intValue();
			Validations.lessThanOrEqual(name, intValue, getAnnotation().max(), errors);
		}
    }

	/**
     * @return
     */
    private String determineName()
    {
		return (getAnnotation().name().isEmpty() ? getFieldName() : getAnnotation().name());
    }
}

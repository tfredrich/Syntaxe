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
import java.util.Collection;
import java.util.List;

import com.strategicgains.syntaxe.annotation.IntegerValidation;
import com.strategicgains.syntaxe.util.Validations;
import com.strategicgains.syntaxe.validator.AnnotatedFieldValidator;

/**
 * @author toddf
 * @since Mar 17, 2011
 */
public class IntegerValidator
extends AnnotatedFieldValidator<IntegerValidation>
{
	/**
     * @param field
     * @param annotation
     */
    public IntegerValidator(Field field, IntegerValidation annotation)
    {
	    super(field, annotation);
    }

    @Override
    public void perform(Object instance, List<String> errors)
    {
		String name = determineName();
		Object value = getValue(instance);

		if (value == null)
		{
			if (!getAnnotation().isNullable())
			{
				Validations.require(name, null, errors);
			}

			return;
		}

		if (isArray())
		{
			validateArray(name, !getAnnotation().isNullable(), (Object[]) value, errors);
		}
		else if (isCollection())
		{
			validateCollection(name, !getAnnotation().isNullable(), (Collection<Object>) value, errors);
		}
		else
		{
			validate(name, value, errors);
		}
    }

    @Override
	protected void validate(String name, Object value, List<String> errors)
	{
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

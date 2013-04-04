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
package com.strategicgains.syntaxe.validator.impl;

import java.lang.reflect.Field;
import java.util.List;

import com.strategicgains.syntaxe.annotation.Required;
import com.strategicgains.syntaxe.util.Validations;
import com.strategicgains.syntaxe.validator.AnnotatedFieldValidator;

/**
 * Verifies an field is non-null. If the field is an array, it will verify that
 * the array is non-null and that each element in the array is non-null also.
 * 
 * @author toddf
 * @since Apr 3, 2013
 */
public class RequiredValidator
extends AnnotatedFieldValidator<Required>
{
	/**
	 * @param field
	 * @param annotation
	 */
	public RequiredValidator(Field field, Required annotation)
	{
		super(field, annotation);
	}

	@Override
	public void perform(Object instance, List<String> errors)
	{
		String name = determineName();
		Object value = getValue(instance);

		if (isArray())
		{
			validateArray(name, (value == null ? null : ((Object[]) value)), errors);
		}
		else
		{
			Validations.require(name, (value == null ? null : String.valueOf(value)), errors);
		}
	}

	public void validateArray(String name, Object[] values, List<String> errors)
	{
		if (values == null || values.length == 0)
		{
			Validations.require(name, null, errors);
		}

		if (values == null) return;
		int i = 0;

		for (Object value : values)
		{
			Validations.require(name + "[" + i++ + "]", (value == null ? null : String.valueOf(value)), errors);
		}
	}

	protected String determineName()
	{
		return (getAnnotation().value().isEmpty() ? getFieldName() : getAnnotation().value());
	}
}

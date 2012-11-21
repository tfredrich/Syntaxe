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
package com.strategicgains.syntaxe.validator;

import java.lang.reflect.Field;

import com.strategicgains.syntaxe.ValidationException;

/**
 * @author toddf
 * @since Mar 17, 2011
 */
public abstract class AbstractFieldValidator
implements Validator
{
	private Field field;

	public AbstractFieldValidator(Field field)
	{
		super();
		this.field = field;
		this.field.setAccessible(true);
	}

	public String getFieldName()
	{
		return field.getName();
	}

	public boolean isArray()
	{
		return field.getType().isArray();
	}

	public Object getValue(Object object) 
	throws ValidationException
	{
		try
		{
			return field.get(object);
		}
		catch (Exception e)
		{
			throw new ValidationException(e);
		}
	}
}

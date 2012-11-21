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

import com.strategicgains.syntaxe.annotation.StringValidation;
import com.strategicgains.syntaxe.util.Validations;
import com.strategicgains.syntaxe.validator.AnnotatedFieldValidator;

/**
 * @author toddf
 * @since Mar 17, 2011
 */
public class StringValidator
extends AnnotatedFieldValidator<StringValidation>
{
	/**
     * @param field
     * @param annotation
     */
    public StringValidator(Field field, StringValidation annotation)
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
			validateString(name, (value == null ? null : String.valueOf(value)), errors);
		}
    }

	public void validateArray(String name, Object[] values, List<String> errors)
    {
	    if (getAnnotation().required())
		{
	    	if (values == null || values.length == 0)
	    	{
	    		errors.add(name + " is required");
	    	}
		}
	 
	    if (values == null) return;
	    
    	for (Object value : values)
    	{
			validateString(name, (value == null ? null : String.valueOf(value)), errors);
    	}
    }

	private void validateString(String name, String stringValue, List<String> errors)
    {
	    if (getAnnotation().required())
		{
			Validations.require(name, stringValue, errors);
		}

		if (getAnnotation().minLength() > 0)
		{
			Validations.minLength(name, stringValue, getAnnotation().minLength(), errors);
		}

		if (getAnnotation().maxLength() > 0)
		{
			Validations.maxLength(name, stringValue, getAnnotation().maxLength(), errors);
		}
    }

    protected String determineName()
    {
		return (getAnnotation().name().isEmpty() ? getFieldName() : getAnnotation().name());
    }
}

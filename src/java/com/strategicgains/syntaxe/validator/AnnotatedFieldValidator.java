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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

/**
 * @author toddf
 * @since Mar 17, 2011
 */
public abstract class AnnotatedFieldValidator<T extends Annotation>
extends AbstractFieldValidator
{
	private static final String EMPTY_PREFIX = "";

	private T annotation;

	public AnnotatedFieldValidator(Field field, T annotation)
	{
		super(field);
		this.annotation = annotation;
	}

	protected T getAnnotation()
	{
		return annotation;
	}

	protected void validateCollection(String name, boolean isRequired, Collection<Object> values, List<String> errors)
	{
		if (values == null) return;

		if (isRequired && values.isEmpty())
		{
			errors.add(name + " is required");
		}

		int i = 0;

		for (Object value : values)
		{
			validate(name + "[" + i++ + "]", value, errors);
		}
	}

	protected void validateArray(String name, boolean isRequired, Object[] values, List<String> errors)
    {
	    if (values == null) return;

		if (isRequired && values.length == 0)
		{
			errors.add(name + " is required");
		}

		int i = 0;
	    
    	for (Object value : values)
    	{
			validate(name + "[" + i++ + "]", value, errors);
    	}
    }

	protected abstract void validate(String name, Object value, List<String> errors);

	protected String trimPrefix(String prefix)
	{
		return (prefix != null && !prefix.trim().isEmpty() ? prefix + "." : EMPTY_PREFIX);
	}
}

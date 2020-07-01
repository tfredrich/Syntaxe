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
package com.strategicgains.syntaxe.validator.impl;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.strategicgains.syntaxe.annotation.DateValidation;
import com.strategicgains.syntaxe.util.Validations;
import com.strategicgains.syntaxe.validator.AnnotatedFieldValidator;

/**
 * @author toddf
 * @since Jun 30, 2020
 */
public class DateValidator
extends AnnotatedFieldValidator<DateValidation>
{
	/**
     * @param field
     * @param annotation
     */
    public DateValidator(Field field, DateValidation annotation)
    {
	    super(field, annotation);
    }

    @Override
    public void perform(Object instance, List<String> errors, String prefix)
    {
		String name = determineName(prefix);
		Object value = getValue(instance);

		if (value == null)
		{
			if (getAnnotation().required())
			{
				Validations.require(name, value, errors);
			}

			return;
		}

		if (isArray())
		{
			validateArray(name, getAnnotation().required(), (Object[]) value, errors);
		}
		else if (isCollection())
		{
			validateCollection(name, getAnnotation().required(), (Collection<Object>) value, errors);
		}
		else if (isMap())
		{
			validateCollection(name, getAnnotation().required(), (value == null ? null : ((Map<?, Object>) value).values()), errors);
		}
		else
		{
			validate(name, value, errors);
		}
    }

    @Override
    protected void validate(String name, Object value, List<String> errors)
    {
    	boolean past = getAnnotation().past();
    	boolean present = getAnnotation().present();
    	boolean future = getAnnotation().future();
    	Calendar now = Calendar.getInstance();
    	Calendar v = Calendar.getInstance();
    	v.setTime((Date) value);

    	if (past && present)
    	{
        	if (v.after(now))
        	{
        		errors.add(String.format("%s must be today or in the past", name));
        	}
    	}
    	else if (future && present)
    	{
    		now.add(Calendar.MILLISECOND, -10);

    		if (v.before(now))
    		{
        		errors.add(String.format("%s must be today or in the future", name));
    		}
    	}
    	else if (past)
    	{
    		now.set(Calendar.HOUR_OF_DAY, 0);
    		now.set(Calendar.MINUTE, 0);
    		now.set(Calendar.SECOND, 0);
    		now.set(Calendar.MILLISECOND, 0);

    		if (!v.before(now))
    		{
        		errors.add(String.format("%s must be in the past", name));
    		}
    	}
    	else if (future)
    	{
    		now.set(Calendar.HOUR_OF_DAY, 23);
    		now.set(Calendar.MINUTE, 59);
    		now.set(Calendar.SECOND, 59);
    		now.set(Calendar.MILLISECOND, 999);

    		if (!v.after(now))
    		{
        		errors.add(String.format("%s must be in the future", name));
    		}
    	}
	}

    private String determineName(String prefix)
    {
    	return (getAnnotation().name().isEmpty() ? trimPrefix(prefix) + getFieldName() : getAnnotation().name());
    }
}

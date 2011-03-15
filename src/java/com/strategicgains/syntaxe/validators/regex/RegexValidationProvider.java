package com.strategicgains.syntaxe.validators.regex;

import java.lang.reflect.Field;
import java.util.List;

import com.strategicgains.syntaxe.ValidationException;
import com.strategicgains.syntaxe.annotation.AbstractValidationProvider;

public class RegexValidationProvider
extends AbstractValidationProvider<RegexValidate>
{
	protected String determineName(Field field, RegexValidate annotation)
	{
		return (annotation.name().isEmpty() ? field.getName() : annotation.name());
	}

	@Override
	public void perform(Object object, List<String> errors, Field field) throws ValidationException {
		Object value = getValue(field, object);
		String name = determineName(field, getAnnotation());
		
		if(value == null || value instanceof String)
		{
			String stringValue = (String) value;
			
			if(stringValue == null)
			{
				if( !getAnnotation().nullable() )
				{
					errors.add(name + " cannot be null");
					return;
				}
				return;
			}

			if(stringValue.isEmpty()) {
				if( !getAnnotation().nullable() )
				{
					errors.add(name + " cannot be null");
					return;
				}
				return;
			}

			if(!stringValue.matches(getAnnotation().pattern())) {
				errors.add(name + " does not match the regular expression pattern");
			}
		}
		else
		{
			errors.add(name + " is not a string");
			return;
		}
	}
	
}

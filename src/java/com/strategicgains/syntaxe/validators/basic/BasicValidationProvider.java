package com.strategicgains.syntaxe.validators.basic;

import java.lang.reflect.Field;
import java.util.List;

import com.strategicgains.syntaxe.ValidationException;
import com.strategicgains.syntaxe.annotation.AbstractValidationProvider;
import com.strategicgains.syntaxe.util.Validations;

public class BasicValidationProvider
extends AbstractValidationProvider<BasicValidate>
{
	protected String determineName(Field field, BasicValidate annotation)
	{
		return (annotation.name().isEmpty() ? field.getName() : annotation.name());
	}

	@Override
	public void perform(Object object, List<String> errors, Field field)
	throws ValidationException
	{
		String name = determineName(field, getAnnotation());
		Object value = getValue(field, object);

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
}

package com.strategicgains.syntaxe.validator.impl;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import com.strategicgains.syntaxe.annotation.CollectionValidation;
import com.strategicgains.syntaxe.util.Validations;
import com.strategicgains.syntaxe.validator.AnnotatedFieldValidator;

public class CollectionValidator
extends AnnotatedFieldValidator<CollectionValidation>
{
	public CollectionValidator(Field field, CollectionValidation annotation)
	{
		super(field, annotation);
	}

	@Override
	public void perform(Object instance, List<String> errors, String prefix)
	{
		String name = determineName(prefix);
		Object value = getValue(instance);

		if (getAnnotation().isNullable() && value == null)
		{
			return;
		}
		else if (!getAnnotation().isNullable())
		{
	    	if (value == null)
	    	{
	    		errors.add(name + " is required");
	    		return;
	    	}
		}

		if(isCollection())
		{
			@SuppressWarnings("unchecked")
			int size = (instance == null ? 0 : ((Collection<Object>) getValue(instance)).size());
			Validations.greaterThanOrEqual(name, size, getAnnotation().minSize(), errors);
			Validations.lessThanOrEqual(name, size, getAnnotation().maxSize(), errors);
		}
		else if (isArray())
		{
			int length = (instance == null ? 0 : ((Object[]) getValue(instance)).length);
			Validations.greaterThanOrEqual(name, length, getAnnotation().minSize(), errors);
			Validations.lessThanOrEqual(name, length, getAnnotation().maxSize(), errors);
		}
    }

    @Override
	protected void validate(String name, Object value, List<String> errors)
	{
	}

    private String determineName(String prefix)
    {
    	return (getAnnotation().name().isEmpty() ? trimPrefix(prefix) + getFieldName() : getAnnotation().name());
    }
}
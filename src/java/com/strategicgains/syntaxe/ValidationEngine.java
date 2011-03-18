/*
    Copyright 2010, Strategic Gains, Inc.

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
package com.strategicgains.syntaxe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.strategicgains.syntaxe.annotation.ObjectValidation;
import com.strategicgains.syntaxe.annotation.ValidationProvider;
import com.strategicgains.syntaxe.util.ClassUtils;
import com.strategicgains.syntaxe.validator.AnnotatedFieldValidator;
import com.strategicgains.syntaxe.validator.Validator;

/**
 * @author toddf
 * @since Oct 7, 2010
 */
public class ValidationEngine
{
	private static final ConcurrentHashMap<Class<?>, List<Field>> cachedFieldsByClass = new ConcurrentHashMap<Class<?>, List<Field>>();
	private static final ConcurrentHashMap<Integer, List<Validator>> cachedValidatorsByHashcode = new ConcurrentHashMap<Integer, List<Validator>>();
	private static final ConcurrentHashMap<Class<?>, Validator> cachedObjectValidatorsByClass = new ConcurrentHashMap<Class<?>, Validator>();

	private ValidationEngine()
	{
		// Prevents instantiation.
	}

	public static List<String> validate(Object object)
	{
		List<String> errors = new ArrayList<String>();

		try
		{
			validateFields(object, errors);
			validateObject(object, errors);
			validateIfValidatable(object);
		}
		catch (Exception e)
		{
			errors.add("Exception while validating: " + e.getMessage());
		}

		return errors;
	}

	private static void validateFields(Object object, List<String> errors)
	throws Exception
	{
		Collection<Field> fields = getAllDeclaredFields(object.getClass());

		for (Field field : fields)
		{
			List<Validator> validators = getValidators(field, object);

			if (!validators.isEmpty())
			{
				try
				{
					for (Validator validator : validators)
					{
						validator.perform(object, errors);
					}
				}
				catch (ValidationException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	private static List<Validator> getValidators(Field field, Object object)
	throws InstantiationException, IllegalAccessException, SecurityException,
		NoSuchMethodException, IllegalArgumentException, InvocationTargetException
	{
		List<Validator> validators = cachedValidatorsByHashcode.get(field.hashCode());
		
		if (validators != null)
		{
			return validators;
		}
		
		validators  = new ArrayList<Validator>();

		for (Annotation a : field.getAnnotations())
		{
			if (a.annotationType().isAnnotationPresent(ValidationProvider.class))
			{
				ValidationProvider providerAnnotation = a.annotationType().getAnnotation(ValidationProvider.class);
				Constructor<?> constructor = providerAnnotation.value().getConstructor(Field.class, a.annotationType());
				AnnotatedFieldValidator<?> provider = (AnnotatedFieldValidator<?>) constructor.newInstance(field, a);
				validators.add(provider);
			}
		}
		
		cachedValidatorsByHashcode.put(field.hashCode(), validators);
		return validators;
	}
	
	private static Collection<Field> getAllDeclaredFields(Class<?> aClass)
	{
		List<Field> fields = cachedFieldsByClass.get(aClass);
		
		if (fields == null)
		{
			fields = ClassUtils.getAllDeclaredFields(aClass);
			cachedFieldsByClass.put(aClass, fields);
		}
		
		return fields;
	}

	private static void validateObject(Object object, List<String> errors)
	throws InstantiationException, IllegalAccessException
	{
		ObjectValidation annotation = object.getClass().getAnnotation(ObjectValidation.class);
		
		if (annotation == null) return;
		
		Validator validator = cachedObjectValidatorsByClass.get(object.getClass());
		
		if (validator == null)
		{
			validator = annotation.value().newInstance();
			cachedObjectValidatorsByClass.put(object.getClass(), validator);
		}
		
		validator.perform(object, errors);
	}

	private static void validateIfValidatable(Object object)
	{
		if (Validatable.class.isAssignableFrom(object.getClass()))
		{
			((Validatable) object).validate();
		}
	}
}

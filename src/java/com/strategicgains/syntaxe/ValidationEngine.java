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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.strategicgains.syntaxe.annotation.ValidationProvidedBy;
import com.strategicgains.syntaxe.annotation.ValidationProvider;
import com.strategicgains.syntaxe.util.ClassUtils;

/**
 * @author toddf
 * @since Oct 7, 2010
 */
public class ValidationEngine
{
	private static final ConcurrentHashMap<Integer, List<ValidationProvider<?>>> cachedValidatorsByFieldHash = new ConcurrentHashMap<Integer, List<ValidationProvider<?>>>();

	private ValidationEngine()
	{
		// Prevents instantiation.
	}

	public static List<String> validate(Object object)
	{
		List<String> errors = new ArrayList<String>();

		try {
			validateFields(object, errors);
		} catch (Exception e) {
			errors.add("Exception while validating: " + e.getMessage());
		}

		return errors;
	}

	private static void validateFields(Object object, List<String> errors) throws Exception
	{
		Collection<Field> fields = ClassUtils.getAllDeclaredFields(object.getClass());

		for (Field field : fields)
		{
			List<ValidationProvider<?>> validators = getValidationProviders(field, object);
			if ( !validators.isEmpty() )
			{
				try
                {
					for(ValidationProvider<?> validator : validators)
					{
						validator.perform(object, errors, field);
					}
                }
                catch (ValidationException e)
                {
	                e.printStackTrace();
                }
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static List<ValidationProvider<?>> getValidationProviders(Field field, Object object) throws InstantiationException, IllegalAccessException
	{
//		if(cachedValidatorsByFieldHash.containsKey(field.hashCode())) {
//			return cachedValidatorsByFieldHash.get(field.hashCode());
//		}
		
		List<ValidationProvider<?>> result = new ArrayList<ValidationProvider<?>>();
		
		for(Annotation a : field.getAnnotations()) {
			if(a.annotationType().isAnnotationPresent(ValidationProvidedBy.class)) {
				ValidationProvidedBy vpAnnotation = a.annotationType().getAnnotation(ValidationProvidedBy.class);
				ValidationProvider<?> provider = vpAnnotation.name().newInstance();
				provider.setAnnotation(a);
				result.add(provider);
			}
		}
		
		result = (result.size() > 0 ? result : Collections.EMPTY_LIST);
		cachedValidatorsByFieldHash.put(field.hashCode(), result);
		
		return result;
	}
}

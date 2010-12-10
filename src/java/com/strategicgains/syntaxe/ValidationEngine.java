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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.strategicgains.jbel.exception.FunctionException;
import com.strategicgains.jbel.function.UnaryFunction;
import com.strategicgains.syntaxe.annotation.Validate;
import com.strategicgains.syntaxe.util.ClassUtils;
import com.strategicgains.syntaxe.util.Validations;

/**
 * @author toddf
 * @since Oct 7, 2010
 */
public class ValidationEngine
{
	private ValidationEngine()
	{
		// Prevents instantiation.
	}

	public static List<String> validate(Object object)
	{
		List<String> errors = new ArrayList<String>();

		validateFields(object, errors);

		return errors;
	}

	private static void validateFields(Object object, List<String> errors)
	{
		Collection<Field> fields = ClassUtils.getAllDeclaredFields(object.getClass());
		FieldValidationClosure validation = new FieldValidationClosure(object, errors);

		for (Field field : fields)
		{
			if (shouldValidateField(field))
			{
				try
                {
	                validation.perform(field);
                }
                catch (FunctionException e)
                {
	                e.printStackTrace();
                }
			}
		}
	}

	private static String determineName(Validate annotation, Field field)
	{
		return (annotation.name().isEmpty() ? field.getName() : annotation.name());
	}

	private static boolean shouldValidateField(Field field)
	{
		return field.isAnnotationPresent(Validate.class);
	}
	
	private static class FieldValidationClosure
	implements UnaryFunction
	{
		private Object object;
		private List<String> errors;
		
		public FieldValidationClosure(Object object, List<String> errors)
		{
			super();
			this.errors = errors;
			this.object = object;
		}

        @Override
        public Object perform(Object argument)
        throws FunctionException
        {
        	Field field = (Field) argument;
        	field.setAccessible(true);
        	Validate annotation = field.getAnnotation(Validate.class);
        	String name = determineName(annotation, field);
        	Object value;
            try
            {
	            value = field.get(object);
            }
            catch (Exception e)
            {
            	throw new FunctionException(e);
            }
        	
        	if (annotation.required())
        	{
        		String stringValue = (value == null ? null : String.valueOf(value));
            	Validations.require(name, stringValue, errors);
        	}
        	
        	if (annotation.minLength() > 0)
        	{
        		String stringValue = (value == null ? null : String.valueOf(value));
            	Validations.minLength(name, stringValue, annotation.minLength(), errors);
        	}
        	
        	if (annotation.maxLength() > 0)
        	{
        		String stringValue = (value == null ? null : String.valueOf(value));
            	Validations.maxLength(name, stringValue, annotation.maxLength(), errors);
        	}
        	
//        	String validator = annotation.validator();
//        	if (validator != null)
//        	{
//        		execValidators(name, value, validator, errors);
//        	}
        	
        	if (annotation.min() != Integer.MIN_VALUE)
        	{
        		int intValue = ((Integer) value).intValue();
        		Validations.greaterThanOrEqual(name, intValue, annotation.min(), errors);
        	}
        	
        	if (annotation.max() != Integer.MAX_VALUE)
        	{
        		int intValue = ((Integer) value).intValue();
        		Validations.lessThanOrEqual(name, intValue, annotation.max(), errors);
        	}

	        return null;
        }

		/**
         * @param validatorString
         */
//        private void execValidators(String name, Object value, String validatorString, List<String> errors)
//        {
//        	String[] validatorNames = validatorString.split("\\+");
//        	
//        	for (String validatorName : validatorNames)
//        	{
//        		FieldValidator validator = Validator.valueOf(validatorName);
//        		
//        		if (validator != null)
//        		{
//        			if (!validator.isValid(value))
//        			{
//        				errors.add(validator.getMessageFor(name, value));
//        			}
//        		}
//        	}
//        }
	}
}

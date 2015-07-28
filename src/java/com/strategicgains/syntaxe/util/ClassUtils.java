/*
    Copyright 2008, Strategic Gains, Inc.

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
package com.strategicgains.syntaxe.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author toddf
 * @since Aug 18, 2008
 */
public class ClassUtils
{
	// SECTION: CONSTANTS

	public static final int IGNORED_FIELD_MODIFIERS = Modifier.FINAL | Modifier.STATIC | Modifier.TRANSIENT;

	
	// SECTION: CONSTRUCTOR - PRIVATE
	
	private ClassUtils()
	{
		// Prevents instantiation.
	}

	
	// SECTION: CLASS UTILITIES

	/**
	 * Traverses from the given object up the inheritance hierarchy to list all the
	 * declared fields.
	 * 
	 * @param aClass
	 * @return list of Field instances.
	 */
	public static List<Field> getAllDeclaredFields(Class<?> aClass)
	{
		return getAllDeclaredFields(aClass, IGNORED_FIELD_MODIFIERS);
	}

	public static List<Field> getAllDeclaredFields(Class<?> aClass, int modifiers)
	{
		FieldListClosure closure = new ClassUtils.FieldListClosure(new ArrayList<Field>(), modifiers);
		computeDeclaredFields(aClass, closure);
		return closure.getValues();
	}
    
    public static HashMap<String, Field> getAllDeclaredFieldsByName(Class<?> aClass)
    {
    	return getAllDeclaredFieldsByName(aClass, IGNORED_FIELD_MODIFIERS);
    }

    public static HashMap<String, Field> getAllDeclaredFieldsByName(Class<?> aClass, int modifiers)
    {
		FieldMapClosure closure = new ClassUtils.FieldMapClosure(new HashMap<String, Field>(), modifiers);
		computeDeclaredFields(aClass, closure);
		return closure.getValues();
    }

	public static <T> T computeDeclaredFields(Class<?> aClass, FieldClosure<T> function)
	{
		for (Field field : aClass.getDeclaredFields())
		{
			function.perform(field);
		}

		if (aClass.getSuperclass() != null)
		{
			computeDeclaredFields(aClass.getSuperclass(), function);
		}
		
		return function.getValues();
	}    

    // SECTION: INNER CLASSES
    
    public interface FieldClosure<T>
    {
    	void perform(Field argument);
    	T getValues();
    }
    
    private static class FieldListClosure
    implements FieldClosure<List<Field>>
    {
    	private List<Field> values;
    	private int ignoredModifiers;

    	public FieldListClosure(List<Field> values, int modifiers)
    	{
    		super();
    		this.values = values;
    		this.ignoredModifiers = modifiers;
    	}

        @Override
        public void perform(Field field)
        {
        	if ((field.getModifiers() & ignoredModifiers) == 0)
        	{
        		values.add(field);
        	}
        }
        
        @Override
        public List<Field> getValues()
        {
        	return values;
        }
    }
    
    private static class FieldMapClosure
    implements FieldClosure<Map<String, Field>>
    {
    	private HashMap<String, Field> values;
    	private int ignoredModifiers;

    	public FieldMapClosure(HashMap<String, Field> values, int modifiers)
    	{
    		super();
    		this.values = values;
    		this.ignoredModifiers = modifiers;
    	}

        @Override
        public void perform(Field field)
        {
        	if ((field.getModifiers() & ignoredModifiers) == 0)
        	{
        		values.put(field.getName(), field);
        	}
        }
        
        @Override
        public HashMap<String, Field> getValues()
        {
        	return values;
        }
    }
}

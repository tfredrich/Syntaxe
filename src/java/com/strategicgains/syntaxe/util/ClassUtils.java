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
	 * @param object
	 * @return
	 */
	public static List<Field> getAllDeclaredFields(Class<?> aClass)
	{
		return getAllDeclaredFields(aClass, IGNORED_FIELD_MODIFIERS);
	}

	public static List<Field> getAllDeclaredFields(Class<?> aClass, int modifiers)
	{
		FieldListClosure closure = new ClassUtils.FieldListClosure(new ArrayList<Field>(), modifiers);
		getAllDeclaredFields(aClass, closure);
		return closure.getValues();
	}
    
    public static HashMap<String, Field> getAllDeclaredFieldsByName(Class<?> aClass)
    {
    	return getAllDeclaredFieldsByName(aClass, IGNORED_FIELD_MODIFIERS);
    }

    public static HashMap<String, Field> getAllDeclaredFieldsByName(Class<?> aClass, int modifiers)
    {
		FieldHashMapClosure closure = new ClassUtils.FieldHashMapClosure(new HashMap<String, Field>(), modifiers);
		getAllDeclaredFields(aClass, closure);
		return closure.getValues();
    }

	public static void getAllDeclaredFields(Class<?> aClass, FieldClosure function)
	{
		for (Field field : aClass.getDeclaredFields())
		{
			function.perform(field);
		}

		if (aClass.getSuperclass() != null)
		{
			getAllDeclaredFields(aClass.getSuperclass(), function);
		}
	}    

    // SECTION: INNER CLASSES
    
    public interface FieldClosure
    {
    	void perform(Field argument);
    }
    
    private static class FieldListClosure
    implements FieldClosure
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
        
        public List<Field> getValues()
        {
        	return values;
        }
    }
    
    private static class FieldHashMapClosure
    implements FieldClosure
    {
    	private HashMap<String, Field> values;
    	private int ignoredModifiers;

    	public FieldHashMapClosure(HashMap<String, Field> values, int modifiers)
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
        
        public HashMap<String, Field> getValues()
        {
        	return values;
        }
    }
}

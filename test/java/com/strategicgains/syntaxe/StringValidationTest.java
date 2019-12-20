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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.strategicgains.syntaxe.annotation.StringValidation;


/**
 * @author toddf
 * @since Oct 8, 2010
 */
public class StringValidationTest
{
	private Inner object = new Inner();

	@Test
	public void shouldFailRequiredValidatedString()
	{
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).contains("required"));
		assertTrue(errors.get(0).contains("validated string"));
	}

	@Test
	public void shouldFailMaxLength()
	{
		object.setValidatedString("todd was here once, at least");
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).contains("validated string"));
		assertTrue(errors.get(0).contains("5 characters"));
	}
	
	@Test
	public void shouldUseActualPropertyName()
	{
		object.setValidatedString("todd");
		object.setUnnamedString("this is a test of the...");
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).contains("unnamedString"));
	}

	@Test
	public void shouldPassValidation()
	{
		object.setValidatedString("todd");
		object.setUnnamedString("fred");
		List<String> errors = ValidationEngine.validate(object);
		assertTrue(errors.isEmpty());
	}
	
	@Test(expected=ValidationException.class)
	public void shouldThrowOnValidationError()
	{
		ValidationEngine.validateAndThrow(object);
	}

	@Test
	public void shouldRequireStringArray()
	{
		InnerToo o = new InnerToo();
		List<String> errors = ValidationEngine.validate(o);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).contains("Required Array"));		
	}

	@Test
	public void shouldRequireStringArrayElements()
	{
		InnerToo o = new InnerToo();
		o.requiredArray = new String[0];
		List<String> errors = ValidationEngine.validate(o);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).contains("Required Array"));		
	}

	@Test
	public void shouldValidateRequiredStringArrayElements()
	{
		InnerToo o = new InnerToo();
		o.requiredArray = new String[] {"a", "toddfredrich", "toddf"};
		List<String> errors = ValidationEngine.validate(o);
		assertEquals(2, errors.size());
		assertTrue(errors.get(0).contains("Required Array"));		
		assertTrue(errors.get(1).contains("Required Array"));		
	}

	@Test
	public void shouldPassArrayValidation()
	{
		InnerToo o = new InnerToo();
		o.requiredArray = new String[] {"toddf"};
		o.stringArray = new String[0];
		List<String> errors = ValidationEngine.validate(o);
		assertTrue(errors.isEmpty());
	}

	@Test
	public void shouldFailNonRequiredArrayValidation()
	{
		InnerToo o = new InnerToo();
		o.requiredArray = new String[] {"toddf"};
		o.stringArray = new String[] {"toddf", "toddfredrich", "isthebomb"};
		List<String> errors = ValidationEngine.validate(o);
		assertEquals(2, errors.size());
		assertTrue(errors.get(0).contains("String Array"));
		assertTrue(errors.get(1).contains("String Array"));
	}

	@Test
	public void shouldValidateNullableMapValues()
	{
		Map<String, String> map = new LinkedHashMap<>();
		map.put("todd", "was here");
		map.put("qr", "st");
		map.put("foo", "barely");
		object.setValidatedString("ab");
		object.setNullableMap(map);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(2, errors.size());
		assertEquals("nullableMap[0] is limited to 5 characters", errors.get(0));
		assertEquals("nullableMap[2] is limited to 5 characters", errors.get(1));
	}

	@Test
	public void shouldValidateNonNullableMapValues()
	{
		Map<String, String> map = new LinkedHashMap<>();
		map.put("todd", "was here");
		map.put("qr", "st");
		map.put("foo", "barely");
		object.setValidatedString("ab");
		object.setNotNullMap(map);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(2, errors.size());
		assertEquals("notNullMap[0] is limited to 5 characters", errors.get(0));
		assertEquals("notNullMap[2] is limited to 5 characters", errors.get(1));
	}

	@SuppressWarnings("unused")
	private class Inner
	{
		@StringValidation(name="validated string", required=true, maxLength=5)
		private String validatedString;

		@StringValidation(maxLength=5)
		private String unnamedString;
		
		private String ignoredString;
		
		private int ignoredInt;

		@StringValidation(maxLength=5)
		private Collection<String> nullableCollection = new HashSet<String>();

		@StringValidation(maxLength=5)
		public Map<String, String> nullableMap;

		@StringValidation(maxLength=5, minLength=2, required=true)
		public Map<String, String> notNullMap = new HashMap<>();

		public Inner()
		{
			super();
			notNullMap.put("zy", "xw");
		}

		public void setValidatedString(String validatedString)
        {
        	this.validatedString = validatedString;
        }
		
		public void setIgnoredString(String ignoredString)
        {
        	this.ignoredString = ignoredString;
        }
		
		public void setIgnoredInt(int ignoredInt)
        {
        	this.ignoredInt = ignoredInt;
        }
		
		public void setUnnamedString(String value)
		{
			this.unnamedString = value;
		}

		public void setNullableCollection(Collection<String> nullableCollection)
        {
        	this.nullableCollection = nullableCollection;
        }

		public void setNullableMap(Map<String, String> nullableMap)
		{
			this.nullableMap = nullableMap;
		}

		public void setNotNullMap(Map<String, String> notNullMap)
		{
			this.notNullMap = notNullMap;
		}
	}

	private class InnerToo
	{
		@StringValidation(name = "Required Array", required = true, maxLength = 6, minLength = 2)
		public String[] requiredArray = null;
		
		@StringValidation(name = "String Array", required = false, maxLength = 6, minLength = 1)
		public String[] stringArray = null;		
	}
}

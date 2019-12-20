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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.strategicgains.syntaxe.annotation.RegexValidation;


/**
 * @author chrisch
 * @since Oct 8, 2010
 */
public class RegexValidationTest
{
	private Inner object = new Inner();

	@Test
	public void shouldFailPattern()
	{
		object.setNotNullString("abc");
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertEquals("not-null-string does not match the regular expression pattern: [a-z]{2}", errors.get(0));
	}
	
	@Test
	public void shouldFailCollectionPattern()
	{
		object.setNotNullString("abc");
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertEquals("not-null-string does not match the regular expression pattern: [a-z]{2}", errors.get(0));
	}

	@Test
	public void shouldFailPatternWithMessage()
	{
		object.setNotNullString("ab");
		object.setNullableString("abc");
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertEquals("nullable-string must be a two-character, lower-case string", errors.get(0));
	}

	@Test
	public void shouldPassPattern()
	{
		object.setNotNullString("ab");
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(0, errors.size());
	}
	
	@Test
	public void shouldFailNullable()
	{
		object.setNotNullString(null);
		object.setNotNullMap(null);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(2, errors.size());
		assertEquals("not-null-string is required", errors.get(0));
		assertEquals("not-null-map is required", errors.get(1));
	}

	@Test
	public void shouldPassNullable()
	{
		object.setNotNullString("ab");
		object.setNullableString(null);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(0, errors.size());
	}
	
	@Test
	public void shouldPassNotNullCollection()
	{
		object.setNotNullString("ab");
		object.setNullableString(null);
		HashSet<String> collection = new HashSet<String>();
		collection.add("ab");
		collection.add("cd");
		object.setNullableCollection(collection);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(0, errors.size());
	}
	
	@Test
	public void shouldPassNotNullStringArray()
	{
		object.setNotNullString("ab");
		object.setNullableString(null);
		HashSet<String> collection = new HashSet<String>();
		collection.add("ab");
		collection.add("cd");
		object.setNullableCollection(collection);
		String[] stringArray = new String[] {"ef", "gh"}; 
		object.setStringArray(stringArray);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(0, errors.size());
	}

	@Test
	public void shouldValidateNullableMapValues()
	{
		Map<String, String> map = new LinkedHashMap<>();
		map.put("todd", "was here");
		map.put("qr", "st");
		map.put("foo", "bar");
		object.setNotNullString("ab");
		object.setNullableMap(map);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(2, errors.size());
		assertEquals("nullable-map[0] must be a two-character, lower-case string", errors.get(0));
		assertEquals("nullable-map[2] must be a two-character, lower-case string", errors.get(1));
	}

	@Test
	public void shouldValidateNonNullableMapValues()
	{
		Map<String, String> map = new LinkedHashMap<>();
		map.put("todd", "was here");
		map.put("qr", "st");
		map.put("foo", "bar");
		object.setNotNullString("ab");
		object.setNotNullMap(map);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(2, errors.size());
		assertEquals("not-null-map[0] does not match the regular expression pattern: [a-z]{2}", errors.get(0));
		assertEquals("not-null-map[2] does not match the regular expression pattern: [a-z]{2}", errors.get(1));
	}

	private class Inner
	{
		@RegexValidation(name="not-null-string", nullable=false, pattern="[a-z]{2}")
		private String notNullString;
		
		@RegexValidation(name="nullable-string", nullable=true, pattern="[a-z]{2}", message = "must be a two-character, lower-case string")
		private String nullableString;

		@RegexValidation(name="nullable-Collection", nullable=true, pattern="[a-z]{2}")
		private Collection<String> nullableCollection = new HashSet<String>();
		
		@RegexValidation(name = "string-array", nullable = true, pattern="[a-z]{2}")
		public String[] stringArray = null;

		@RegexValidation(name="nullable-map", nullable=true, pattern="[a-z]{2}", message = "must be a two-character, lower-case string")
		public Map<String, String> nullableMap;

		@RegexValidation(name="not-null-map", nullable=false, pattern="[a-z]{2}")
		public Map<String, String> notNullMap = new HashMap<>();

		public Inner()
		{
			super();
			notNullMap.put("zy", "xw");
		}

		public void setNotNullString(String notNullString)
        {
        	this.notNullString = notNullString;
        }

		public void setNullableString(String nullableString)
        {
        	this.nullableString = nullableString;
        }

		public void setNullableCollection(Collection<String> nullableCollection)
        {
        	this.nullableCollection = nullableCollection;
        }

		public void setStringArray(String[] stringArray)
		{
			this.stringArray = stringArray;
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
}

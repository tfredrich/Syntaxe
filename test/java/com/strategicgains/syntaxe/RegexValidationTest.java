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

import java.util.HashSet;
import java.util.List;

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
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertEquals("not-null-string is required", errors.get(0));
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
		object.setNotNullCollection(collection);
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
		object.setNotNullCollection(collection);
		String[] stringArray = new String[] {"ef", "gh"}; 
		object.setNotNullStringArray(stringArray);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(0, errors.size());
	}

	private class Inner
	{
		@RegexValidation(name="not-null-string", nullable=false, pattern="[a-z]{2}")
		private String notNullString;
		
		@RegexValidation(name="nullable-string", nullable=true, pattern="[a-z]{2}", message = "must be a two-character, lower-case string")
		private String nullableString;
		
		@RegexValidation(name="not-null-Collection", nullable=true, pattern="[a-z]{2}")
		private HashSet<String> notNullCollection = new HashSet<String>();
		
		@RegexValidation(name = "not-null-String-Array", nullable = true, pattern="[a-z]{2}")
		public String[] stringArray = null;	

		public void setNotNullString(String notNullString)
        {
        	this.notNullString = notNullString;
        }

		public void setNullableString(String nullableString)
        {
        	this.nullableString = nullableString;
        }
		
		public void setNotNullCollection(HashSet<String> notNullCollection)
        {
        	this.notNullCollection = notNullCollection;
        }
		
		public void setNotNullStringArray(String[] stringArray)
        {
        	this.stringArray = stringArray;
        }

	}
}

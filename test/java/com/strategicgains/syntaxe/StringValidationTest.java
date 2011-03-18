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

import java.util.List;

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
	
	@SuppressWarnings("unused")
	private class Inner
	{
		@StringValidation(name="validated string", required=true, maxLength=5)
		private String validatedString;

		@StringValidation(maxLength=5)
		private String unnamedString;
		
		private String ignoredString;
		
		private int ignoredInt;

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
	}
}

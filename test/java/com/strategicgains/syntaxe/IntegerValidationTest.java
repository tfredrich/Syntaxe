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

import com.strategicgains.syntaxe.annotation.IntegerValidation;


/**
 * @author toddf
 * @since Oct 8, 2010
 */
public class IntegerValidationTest
{
	private Inner object = new Inner();

	@Test
	public void shouldFailGreaterThanTest()
	{
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).contains("greater-than or equal-to 5"));
		assertTrue(errors.get(0).contains("validated integer"));
	}

	@Test
	public void shouldFailLessThanTest()
	{
		object.setValidatedInt(25);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).contains("less-than or equal-to 10"));
		assertTrue(errors.get(0).contains("validated integer"));
	}
	
	@Test
	public void shouldUseActualPropertyName()
	{
		object.setIntField(5);
		object.setValidatedInt(7);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).contains("intField"));
	}

	@Test
	public void shouldPassValidation()
	{
		object.setValidatedInt(7);
		List<String> errors = ValidationEngine.validate(object);
		assertTrue(errors.isEmpty());
	}

	@Test
	public void shouldFailRequiredness()
	{
		object.setValidatedInt(7);
		object.setNonNullableInteger(null);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).contains("nonNullableInteger"));
	}

	@Test
	public void shouldFailNonNullOutOfBandValue()
	{
		object.setValidatedInt(7);
		object.setNonNullableInteger(25);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).contains("nonNullableInteger"));
	}

	@Test
	public void shouldPassValidateNonNull()
	{
		object.setValidatedInt(7);
		object.setNullableInteger(5);
		List<String> errors = ValidationEngine.validate(object);
		assertTrue(errors.isEmpty());
	}

	@Test
	public void shouldFailValidateNonNull()
	{
		object.setValidatedInt(7);
		object.setNullableInteger(7);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).contains("nullableInteger"));
	}
	
	@SuppressWarnings("unused")
	private class Inner
	{
		@IntegerValidation(name="validated integer", min=5, max=10)
		private int validatedInt;
		
		private int ignoredInt;

		@IntegerValidation(min=-1, max=1)
		private int intField;

		@IntegerValidation(min=0, max=5, isNullable=true)
		private Integer nullableInteger;

		@IntegerValidation(min=0, max=5)
		private Integer nonNullableInteger = 1;

		public void setValidatedInt(int validatedInt)
        {
        	this.validatedInt = validatedInt;
        }
		
		public void setIgnoredInt(int ignoredInt)
        {
        	this.ignoredInt = ignoredInt;
        }

		public void setIntField(int intField)
        {
        	this.intField = intField;
        }

		public void setNullableInteger(Integer value)
		{
			this.nullableInteger = value;
		}

		public void setNonNullableInteger(Integer value)
		{
			this.nonNullableInteger = value;
		}
	}
}

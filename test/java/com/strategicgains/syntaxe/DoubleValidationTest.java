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

import com.strategicgains.syntaxe.annotation.DoubleValidation;


/**
 * @author Noor Dawod [noor@fineswap.com]
 * @since Nov 17, 2015
 */
public class DoubleValidationTest
{
	private Inner object = new Inner();

	@Test
	public void shouldFailGreaterThanTest()
	{
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).contains("greater-than or equal-to 5"));
		assertTrue(errors.get(0).contains("validated double"));
	}

	@Test
	public void shouldFailLessThanTest()
	{
		object.setValidatedDouble(25d);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).contains("less-than or equal-to 10"));
		assertTrue(errors.get(0).contains("validated double"));
	}
	
	@Test
	public void shouldUseActualPropertyName()
	{
		object.setDoubleField(5d);
		object.setValidatedDouble(7d);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).contains("doubleField"));
	}

	@Test
	public void shouldPassValidation()
	{
		object.setValidatedDouble(7d);
		List<String> errors = ValidationEngine.validate(object);
		assertTrue(errors.isEmpty());
	}

	@Test
	public void shouldFailRequiredness()
	{
		object.setValidatedDouble(7d);
		object.setNonNullableDouble(null);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).contains("nonNullableDouble"));
	}

	@Test
	public void shouldFailNonNullOutOfBandValue()
	{
		object.setValidatedDouble(7d);
		object.setNonNullableDouble(25.5d);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).contains("nonNullableDouble"));
	}

	@Test
	public void shouldPassValidateNonNull()
	{
		object.setValidatedDouble(7d);
		object.setNullableDouble(5.734d);
		List<String> errors = ValidationEngine.validate(object);
		assertTrue(errors.isEmpty());
	}

	@Test
	public void shouldFailValidateNonNull()
	{
		object.setValidatedDouble(7d);
		object.setNullableDouble(7.674d);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).contains("nullableDouble"));
	}
	
	@SuppressWarnings("unused")
	private class Inner
	{
		@DoubleValidation(name="validated double", min=5d, max=10d)
		private double validatedDouble;
		
		private double ignoredDouble;

		@DoubleValidation(min=-1, max=1)
		private double doubleField;

		@DoubleValidation(min=0, max=5, isNullable=true)
		private Double nullableDouble;

		@DoubleValidation(min=0, max=5)
		private Double nonNullableDouble = 1d;

		public void setValidatedDouble(double validatedDouble)
        {
        	this.validatedDouble = validatedDouble;
        }
		
		public void setIgnoredDouble(double ignoredDouble)
        {
        	this.ignoredDouble = ignoredDouble;
        }

		public void setDoubleField(double doubleField)
        {
        	this.doubleField = doubleField;
        }

		public void setNullableDouble(Double value)
		{
			this.nullableDouble = value;
		}

		public void setNonNullableDouble(Double value)
		{
			this.nonNullableDouble = value;
		}
	}
}

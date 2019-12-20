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
		object.setNullableDouble(4.734d);
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

	@Test
	public void shouldValidateNullableMapValues()
	{
		Map<String, Double> map = new LinkedHashMap<>();
		map.put("todd", 3d);
		map.put("qr", 9d);
		map.put("foo", 11d);
		object.setNullableMap(map);
		object.setValidatedDouble(7d);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(2, errors.size());
		assertTrue(errors.get(0).startsWith("nullableMap[1] must be less-than or equal-to 5.0"));
		assertTrue(errors.get(1).startsWith("nullableMap[2] must be less-than or equal-to 5.0"));
	}

	@Test
	public void shouldValidateNonNullableMapValues()
	{
		Map<String, Double> map = new LinkedHashMap<>();
		map.put("todd", 3d);
		map.put("qr", 9d);
		map.put("foo", 11d);
		object.setNotNullMap(map);
		object.setValidatedDouble(7d);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(2, errors.size());
		assertTrue(errors.get(0).startsWith("notNullMap[0] must be greater-than or equal-to 5.0"));
		assertTrue(errors.get(1).startsWith("notNullMap[2] must be less-than or equal-to 10.0"));
	}

	@SuppressWarnings("unused")
	private class Inner
	{
		@DoubleValidation(name="validated double", min=5d, max=10d)
		private double validatedDouble;
		
		private double ignoredDouble;

		@DoubleValidation(min=-1d, max=1d)
		private double doubleField;

		@DoubleValidation(min=0d, max=5d, isNullable=true)
		private Double nullableDouble;

		@DoubleValidation(min=0d, max=5d)
		private Double nonNullableDouble = 1d;

		@DoubleValidation(min=0d, max=5d, isNullable=true)
		private Collection<Double> nullableCollection = new HashSet<>();

		@DoubleValidation(min=0d, max=5d, isNullable=true)
		public Map<String, Double> nullableMap;

		@DoubleValidation(min=5d, max=10d)
		public Map<String, Double> notNullMap = new HashMap<>();

		public Inner()
		{
			super();
			notNullMap.put("ab", 6d);
		}

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

		public void setNullableCollection(Collection<Double> nullableCollection)
        {
        	this.nullableCollection = nullableCollection;
        }

		public void setNullableMap(Map<String, Double> nullableMap)
		{
			this.nullableMap = nullableMap;
		}

		public void setNotNullMap(Map<String, Double> notNullMap)
		{
			this.notNullMap = notNullMap;
		}
	}
}

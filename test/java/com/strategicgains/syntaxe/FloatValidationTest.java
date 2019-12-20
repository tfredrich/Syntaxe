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

import com.strategicgains.syntaxe.annotation.FloatValidation;


/**
 * @author Noor Dawod [noor@fineswap.com]
 * @since Oct 24, 2015
 */
public class FloatValidationTest
{
	private Inner object = new Inner();

	@Test
	public void shouldFailGreaterThanTest()
	{
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).contains("greater-than or equal-to 5"));
		assertTrue(errors.get(0).contains("validated float"));
	}

	@Test
	public void shouldFailLessThanTest()
	{
		object.setValidatedFloat(25f);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).contains("less-than or equal-to 10"));
		assertTrue(errors.get(0).contains("validated float"));
	}
	
	@Test
	public void shouldUseActualPropertyName()
	{
		object.setFloatField(5f);
		object.setValidatedFloat(7f);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).contains("floatField"));
	}

	@Test
	public void shouldPassValidation()
	{
		object.setValidatedFloat(7f);
		List<String> errors = ValidationEngine.validate(object);
		assertTrue(errors.isEmpty());
	}

	@Test
	public void shouldFailRequiredness()
	{
		object.setValidatedFloat(7f);
		object.setNonNullableFloat(null);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).contains("nonNullableFloat"));
	}

	@Test
	public void shouldFailNonNullOutOfBandValue()
	{
		object.setValidatedFloat(7f);
		object.setNonNullableFloat(25f);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).contains("nonNullableFloat"));
	}

	@Test
	public void shouldPassValidateNonNull()
	{
		object.setValidatedFloat(7f);
		object.setNullableFloat(5f);
		List<String> errors = ValidationEngine.validate(object);
		assertTrue(errors.isEmpty());
	}

	@Test
	public void shouldFailValidateNonNull()
	{
		object.setValidatedFloat(7f);
		object.setNullableFloat(7f);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).contains("nullableFloat"));
	}

	@Test
	public void shouldValidateNullableMapValues()
	{
		Map<String, Float> map = new LinkedHashMap<>();
		map.put("todd", 3f);
		map.put("qr", 9f);
		map.put("foo", 11f);
		object.setNullableMap(map);
		object.setValidatedFloat(7f);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(2, errors.size());
		assertTrue(errors.get(0).startsWith("nullableMap[1] must be less-than or equal-to 5.0"));
		assertTrue(errors.get(1).startsWith("nullableMap[2] must be less-than or equal-to 5.0"));
	}

	@Test
	public void shouldValidateNonNullableMapValues()
	{
		Map<String, Float> map = new LinkedHashMap<>();
		map.put("todd", 3f);
		map.put("qr", 9f);
		map.put("foo", 11f);
		object.setNotNullMap(map);
		object.setValidatedFloat(7f);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(2, errors.size());
		assertTrue(errors.get(0).startsWith("notNullMap[0] must be greater-than or equal-to 5.0"));
		assertTrue(errors.get(1).startsWith("notNullMap[2] must be less-than or equal-to 10.0"));
	}

	@SuppressWarnings("unused")
	private class Inner
	{
		@FloatValidation(name="validated float", min=5f, max=10f)
		private float validatedFloat;
		
		private float ignoredFloat;

		@FloatValidation(min=-1f, max=1f)
		private float floatField;

		@FloatValidation(min=0f, max=5f, isNullable=true)
		private Float nullableFloat;

		@FloatValidation(min=0f, max=5f)
		private Float nonNullableFloat = 1f;

		@FloatValidation(min=0f, max=5f, isNullable=true)
		private Collection<Float> nullableCollection = new HashSet<>();

		@FloatValidation(min=0f, max=5f, isNullable=true)
		public Map<String, Float> nullableMap;

		@FloatValidation(min=5f, max=10f)
		public Map<String, Float> notNullMap = new HashMap<>();

		public Inner()
		{
			super();
			notNullMap.put("ab", 6f);
		}

		public void setValidatedFloat(float validatedFloat)
        {
        	this.validatedFloat = validatedFloat;
        }
		
		public void setIgnoredFloat(float ignoredFloat)
        {
        	this.ignoredFloat = ignoredFloat;
        }

		public void setFloatField(float floatField)
        {
        	this.floatField = floatField;
        }

		public void setNullableFloat(Float value)
		{
			this.nullableFloat = value;
		}

		public void setNonNullableFloat(Float value)
		{
			this.nonNullableFloat = value;
		}

		public void setNullableCollection(Collection<Float> nullableCollection)
        {
        	this.nullableCollection = nullableCollection;
        }

		public void setNullableMap(Map<String, Float> nullableMap)
		{
			this.nullableMap = nullableMap;
		}

		public void setNotNullMap(Map<String, Float> notNullMap)
		{
			this.notNullMap = notNullMap;
		}
	}
}

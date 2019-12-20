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
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.strategicgains.syntaxe.annotation.LongValidation;


/**
 * @author toddf
 * @since Oct 8, 2010
 */
public class LongValidationTest
{
	private Inner object = new Inner();

	@Test
	public void shouldFailGreaterThanTest()
	{
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).contains("greater-than or equal-to 5"));
		assertTrue(errors.get(0).contains("validated long"));
	}

	@Test
	public void shouldFailLessThanTest()
	{
		object.setValidatedLong(25);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).contains("less-than or equal-to 10"));
		assertTrue(errors.get(0).contains("validated long"));
	}
	
	@Test
	public void shouldUseActualPropertyName()
	{
		object.setLongField(5);
		object.setValidatedLong(7);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).contains("longField"));
	}

	@Test
	public void shouldPassValidation()
	{
		object.setValidatedLong(7);
		List<String> errors = ValidationEngine.validate(object);
		assertTrue(errors.isEmpty());
	}

	@Test
	public void shouldFailRequiredness()
	{
		object.setValidatedLong(7);
		object.setNonNullableLong(null);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).contains("nonNullableLong"));
	}

	@Test
	public void shouldFailNonNullOutOfBandValue()
	{
		object.setValidatedLong(7);
		object.setNonNullableLong(25L);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).contains("nonNullableLong"));
	}

	@Test
	public void shouldPassValidateNonNull()
	{
		object.setValidatedLong(7);
		object.setNullableLong(5L);
		List<String> errors = ValidationEngine.validate(object);
		assertTrue(errors.isEmpty());
	}

	@Test
	public void shouldFailValidateNonNull()
	{
		object.setValidatedLong(7);
		object.setNullableLong(7L);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).contains("nullableLong"));
	}

	@Test
	public void shouldValidateNullableMapValues()
	{
		Map<String, Long> map = new HashMap<>();
		map.put("todd", 3L);
		map.put("qr", 9L);
		map.put("foo", 11L);
		object.setNullableMap(map);
		object.setValidatedLong(7L);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(2, errors.size());
		assertEquals("nullableMap[0] must be less-than or equal-to 5", errors.get(0));
		assertEquals("nullableMap[1] must be less-than or equal-to 5", errors.get(1));
	}

	@Test
	public void shouldValidateNonNullableMapValues()
	{
		Map<String, Long> map = new HashMap<>();
		map.put("todd", 3L);
		map.put("qr", 9L);
		map.put("foo", 11L);
		object.setNotNullMap(map);
		object.setValidatedLong(7L);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(2, errors.size());
		assertEquals("notNullMap[1] must be less-than or equal-to 10", errors.get(0));
		assertEquals("notNullMap[2] must be greater-than or equal-to 5", errors.get(1));
	}

	@SuppressWarnings("unused")
	private class Inner
	{
		@LongValidation(name="validated long", min=5, max=10)
		private long validatedLong;
		
		private long ignoredLong;

		@LongValidation(min=-1, max=1)
		private long longField;

		@LongValidation(min=-1, max=5, isNullable=true)
		private Long nullableLong;

		@LongValidation(min=-1, max=5)
		private Long nonNullableLong = 1L;

		@LongValidation(min=-1, max=5, isNullable=true)
		private Collection<Long> nullableCollection = new HashSet<>();

		@LongValidation(min=-1, max=5, isNullable=true)
		public Map<String, Long> nullableMap;

		@LongValidation(min=5, max=10)
		public Map<String, Long> notNullMap = new HashMap<>();

		public Inner()
		{
			super();
			notNullMap.put("ab", 6L);
		}

		public void setValidatedLong(long value)
        {
        	this.validatedLong = value;
        }
		
		public void setIgnoredLong(long value)
        {
        	this.ignoredLong = value;
        }

		public void setLongField(long value)
        {
        	this.longField = value;
        }

		public void setNullableLong(Long value)
		{
			this.nullableLong = value;
		}

		public void setNonNullableLong(Long value)
		{
			this.nonNullableLong = value;
		}

		public void setNullableCollection(Collection<Long> nullableCollection)
        {
        	this.nullableCollection = nullableCollection;
        }

		public void setNullableMap(Map<String, Long> nullableMap)
		{
			this.nullableMap = nullableMap;
		}

		public void setNotNullMap(Map<String, Long> notNullMap)
		{
			this.notNullMap = notNullMap;
		}
	}
}

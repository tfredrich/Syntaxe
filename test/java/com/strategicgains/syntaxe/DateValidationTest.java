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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.strategicgains.syntaxe.annotation.DateValidation;


/**
 * @author toddf
 * @since Oct 8, 2010
 */
public class DateValidationTest
{
	@Test
	public void shouldFailPresentPastTest()
	{
		Inner object = new Inner();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 1); // Tomorrow
		object.setPresentPastDate(c.getTime());
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertEquals("required date must be today or in the past", errors.get(0));
	}
	
	@Test
	public void shouldUseActualPropertyName()
	{
		Inner object = new Inner();
		object.setPresentPastDate(new Date());
		object.setPresentFutureDate(null);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertEquals("presentFutureDate is required", errors.get(0));
	}

	@Test
	public void shouldPassMinimalValidation()
	{
		Inner object = new Inner();
		object.setPresentPastDate(new Date());
		List<String> errors = ValidationEngine.validate(object);
		assertTrue(errors.isEmpty());
	}

	@Test
	public void shouldPassPopulatedValidation()
	{
		Inner object = new Inner();
		object.setPresentPastDate(new Date());
		object.setPresentFutureDate(new Date());
		object.setIgnoredDate(new Date());

		Calendar future = Calendar.getInstance();
		future.add(Calendar.DATE, 1);

		Calendar past = Calendar.getInstance();
		past.add(Calendar.DATE, -1);

		object.setNullableFutureDate(future.getTime());
		object.setNullablePastDate(past.getTime());

		List<String> errors = ValidationEngine.validate(object);
		assertTrue(errors.isEmpty());
	}

	@Test
	public void shouldFailRequirednessWithName()
	{
		Inner object = new Inner();
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertEquals("required date is required", errors.get(0));
	}

	@Test
	public void shouldFailPresentFuture()
	{
		Inner object = new Inner();
		object.setPresentPastDate(new Date());
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);	// Yesterday
		object.setPresentFutureDate(c.getTime());
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertEquals("presentFutureDate must be today or in the future", errors.get(0));
	}

	@Test
	public void shouldFailFuture()
	{
		Inner object = new Inner();
		object.setPresentPastDate(new Date());
		object.setNullableFutureDate(new Date());
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertEquals("nullableFutureDate must be in the future", errors.get(0));
	}

	@Test
	public void shouldFailPast()
	{
		Inner object = new Inner();
		object.setPresentPastDate(new Date());
		object.setNullablePastDate(new Date());
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertEquals("nullablePastDate must be in the past", errors.get(0));
	}

	@Test
	public void shouldValidateNullableMapValues()
	{
		Calendar future = Calendar.getInstance();
		future.add(Calendar.DATE, 1);
		Calendar past = Calendar.getInstance();
		past.add(Calendar.DATE, -1);

		Map<String, Date> map = new LinkedHashMap<>();
		map.put("todd", new Date());
		map.put("qr", future.getTime());
		map.put("foo", past.getTime());

		Inner object = new Inner();
		object.setPresentPastDate(new Date());
		object.setNullableMap(map);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(2, errors.size());
		assertEquals("nullableMap[0] must be in the past", errors.get(0));
		assertEquals("nullableMap[1] must be in the past", errors.get(1));
	}

	@Test
	public void shouldValidateNonNullableMapValues()
	{
		Calendar future = Calendar.getInstance();
		future.add(Calendar.DATE, 1);
		Calendar past = Calendar.getInstance();
		past.add(Calendar.DATE, -1);

		Map<String, Date> map = new LinkedHashMap<>();
		map.put("todd", new Date());
		map.put("qr", future.getTime());
		map.put("foo", past.getTime());

		Inner object = new Inner();
		object.setPresentPastDate(new Date());
		object.setNotNullMap(map);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(2, errors.size());
		assertEquals("notNullMap[0] must be in the future", errors.get(0));
		assertEquals("notNullMap[2] must be in the future", errors.get(1));
	}

	@Test
	public void shouldValidateNullableCollectionValues()
	{
		Calendar future = Calendar.getInstance();
		future.add(Calendar.DATE, 1);
		Calendar past = Calendar.getInstance();
		past.add(Calendar.DATE, -1);

		Collection<Date> collection = new ArrayList<>();
		collection.add(new Date());
		collection.add(future.getTime());
		collection.add(past.getTime());

		Inner object = new Inner();
		object.setPresentPastDate(new Date());
		object.setNullableCollection(collection);
		List<String> errors = ValidationEngine.validate(object);
		assertEquals(1, errors.size());
		assertEquals("nullableCollection[1] must be today or in the past", errors.get(0));
	}

	@SuppressWarnings("unused")
	private class Inner
	{
		@DateValidation(name="required date", required=true, present=true, past=true)
		private Date presentPastDate;
		
		private Date ignoredDate;

		@DateValidation(required=true, present=true, future=true)
		private Date presentFutureDate = new Date();

		@DateValidation(future=true)
		private Date nullableFutureDate;

		@DateValidation(past=true)
		private Date nullablePastDate;

		@DateValidation(present=true, past=true)
		private Collection<Date> nullableCollection;

		@DateValidation(past=true)
		public Map<String, Date> nullableMap;

		@DateValidation(required=true, future=true)
		public Map<String, Date> notNullMap = new HashMap<>();

		public Inner()
		{
			super();
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, 1);
			notNullMap.put("ab", c.getTime());
		}

		public void setPresentPastDate(Date value)
        {
        	this.presentPastDate = value;
        }
		
		public void setIgnoredDate(Date value)
        {
        	this.ignoredDate = value;
        }

		public void setPresentFutureDate(Date value)
        {
        	this.presentFutureDate = value;
        }

		public void setNullableFutureDate(Date value)
		{
			this.nullableFutureDate = value;
		}

		public void setNullablePastDate(Date value)
		{
			this.nullablePastDate = value;
		}

		public void setNullableCollection(Collection<Date> collection)
        {
        	this.nullableCollection = collection;
        }

		public void setNullableMap(Map<String, Date> map)
		{
			this.nullableMap = map;
		}

		public void setNotNullMap(Map<String, Date> map)
		{
			this.notNullMap = map;
		}
	}
}

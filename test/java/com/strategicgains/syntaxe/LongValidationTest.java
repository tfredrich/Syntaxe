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
	
	@SuppressWarnings("unused")
	private class Inner
	{
		@LongValidation(name="validated long", min=5, max=10)
		private long validatedLong;
		
		private long ignoredLong;

		@LongValidation(min=-1, max=1)
		private long longField;

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
	}
}

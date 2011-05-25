/*
    Copyright 2011, Strategic Gains, Inc.

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
package com.strategicgains.syntaxe.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * @author toddf
 * @since May 25, 2011
 */
public class ValidationsTest
{
	private List<String> errors;

	@Before
	public void before()
	{
		errors = new ArrayList<String>();
	}

	@Test
	public void shouldPassRequire()
	{
		Validations.require("required", "populated", errors);
		assertTrue(errors.isEmpty());
	}

	@Test
	public void shouldFailRequireOnEmpty()
	{
		Validations.require("required", "", errors);
		assertFalse(errors.isEmpty());
		assertEquals(1, errors.size());
		assertEquals("required is required", errors.get(0));
	}

	@Test
	public void shouldFailRequireOnNull()
	{
		Validations.require("required", null, errors);
		assertFalse(errors.isEmpty());
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).startsWith("required is required"));
	}

	@Test
	public void shouldPassMaxLength()
	{
		Validations.maxLength("max length", "12345", 5, errors);
		assertTrue(errors.isEmpty());
	}

	@Test
	public void shouldFailMaxLength()
	{
		Validations.maxLength("max length", "123456", 5, errors);
		assertFalse(errors.isEmpty());
		assertEquals(1, errors.size());
		assertEquals("max length is limited to 5 characters", errors.get(0));
	}

	@Test
	public void shouldPassMinLength()
	{
		Validations.minLength("min length", "12345", 5, errors);
		assertTrue(errors.isEmpty());
	}

	@Test
	public void shouldFailMinLength()
	{
		Validations.minLength("min length", "1234", 5, errors);
		assertFalse(errors.isEmpty());
		assertEquals(1, errors.size());
		assertEquals("min length must contain more-than 5 characters", errors.get(0));
	}

	@Test
	public void shouldPassLessThanOrEqualWithEqual()
	{
		Validations.lessThanOrEqual("less-than", 5, 5, errors);
		assertTrue(errors.isEmpty());
	}

	@Test
	public void shouldPassLessThanOrEqualWithLessThan()
	{
		Validations.lessThanOrEqual("less-than", 4, 5, errors);
		assertTrue(errors.isEmpty());
	}

	@Test
	public void shouldFailLessThanOrEqual()
	{
		Validations.lessThanOrEqual("less-than", 6, 5, errors);
		assertFalse(errors.isEmpty());
		assertEquals(1, errors.size());
		assertEquals("less-than must be less-than or equal-to 5", errors.get(0));
	}

	@Test
	public void shouldPassGreaterThanOrEqualWithEqual()
	{
		Validations.greaterThanOrEqual("greater-than", 5, 5, errors);
		assertTrue(errors.isEmpty());
	}

	@Test
	public void shouldPassGreaterThanOrEqualWithGreaterThan()
	{
		Validations.greaterThanOrEqual("greater-than", 6, 5, errors);
		assertTrue(errors.isEmpty());
	}

	@Test
	public void shouldFailGreaterThanOrEqual()
	{
		Validations.greaterThanOrEqual("greater-than", 4, 5, errors);
		assertFalse(errors.isEmpty());
		assertEquals(1, errors.size());
		assertEquals("greater-than must be greater-than or equal-to 5", errors.get(0));
	}
}

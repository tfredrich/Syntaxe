package com.strategicgains.syntaxe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.strategicgains.syntaxe.annotation.CollectionValidation;
import com.strategicgains.syntaxe.annotation.StringValidation;

public class CollectionValidationTest
{
	@Test
	public void shouldRequireCollection()
	{
		Inner o = new Inner();
		o.stringArray = new String[] {"toddf", "toddfredrich", "isthebomb"};
		List<String> errors = ValidationEngine.validate(o);
		assertEquals(2, errors.size());
		assertTrue(errors.get(0).contains("requiredCollection"));
		assertTrue(errors.get(1).contains("requiredMap"));
	}

	@Test
	public void shouldFailStringValidation()
	{
		Inner o = new Inner();
		o.requiredCollection = new ArrayList<>(Arrays.asList(new String[] {"blip", ""}));
		Map<String, String> map = new LinkedHashMap<>();
		map.put("foo", "");
		map.put("bar", "blip");
		o.requiredMap = map;
		List<String> errors = ValidationEngine.validate(o);
		assertEquals(4, errors.size());
		assertTrue(errors.get(0).contains("requiredCollection"));
		assertTrue(errors.get(1).contains("requiredCollection"));
		assertTrue(errors.get(2).contains("requiredMap"));
		assertTrue(errors.get(3).contains("requiredMap"));
	}

	@Test
	public void shouldFailLowerLimitSizeValidation()
	{
		Inner o = new Inner();
		o.requiredCollection = new ArrayList<>(Arrays.asList(new String[] {"a"}));
		o.stringArray = new String[] {"a"};
		Map<String, String> map = new LinkedHashMap<>();
		map.put("foo", "me");
		o.requiredMap = map;
		o.stringMap = map;
		List<String> errors = ValidationEngine.validate(o);
		assertEquals(4, errors.size());
		assertTrue(errors.get(0).contains("requiredCollection"));
		assertTrue(errors.get(1).contains("stringArray"));
		assertTrue(errors.get(2).contains("requiredMap"));
		assertTrue(errors.get(3).contains("stringMap"));
	}

	@Test
	public void shouldFailUpperLimitSizeValidation()
	{
		Inner o = new Inner();
		o.requiredCollection = new ArrayList<>(Arrays.asList(new String[] {"a", "b", "c", "d"}));
		o.stringArray = new String[] {"a", "b", "c", "d"};
		Map<String, String> map = new LinkedHashMap<>();
		map.put("foo", "a");
		map.put("bar", "b");
		map.put("bat", "c");
		map.put("baz", "d");
		o.requiredMap = map;
		o.stringMap = map;
		List<String> errors = ValidationEngine.validate(o);
		assertEquals(4, errors.size());
		assertTrue(errors.get(0).contains("requiredCollection"));
		assertTrue(errors.get(1).contains("stringArray"));
		assertTrue(errors.get(2).contains("requiredMap"));
		assertTrue(errors.get(3).contains("stringMap"));
	}

	@Test
	public void shouldBeNullable()
	{
		Inner o = new Inner();
		o.requiredCollection = new ArrayList<>(Arrays.asList(new String[] {"a", "b"}));
		Map<String, String> map = new LinkedHashMap<>();
		map.put("foo", "m");
		map.put("bar", "me");
		o.requiredMap = map;
		List<String> errors = ValidationEngine.validate(o);
		assertEquals(0, errors.size());
	}

	@Test
	public void shouldPassValidation()
	{
		Inner o = new Inner();
		o.requiredCollection = new ArrayList<>(Arrays.asList(new String[] {"foo", "bar", "b"}));
		o.stringArray = new String[] {"toddf", "toddfredrich", "isthebomb"};
		Map<String, String> map = new LinkedHashMap<>();
		map.put("foo", "m");
		map.put("bar", "me");
		o.requiredMap = map;
		o.stringMap = map;
		List<String> errors = ValidationEngine.validate(o);
		assertEquals(0, errors.size());
	}

	private class Inner
	{
		@CollectionValidation(isNullable = false, maxSize = 3, minSize = 2)
		@StringValidation(maxLength = 3, minLength = 1)
		public List<String> requiredCollection = null;
		
		@CollectionValidation(maxSize = 3, minSize = 2)
		public String[] stringArray = null;

		@CollectionValidation(isNullable = false, maxSize = 3, minSize = 2)
		@StringValidation(maxLength = 3, minLength = 1)
		public Map<String, String> requiredMap = null;
		
		@CollectionValidation(maxSize = 3, minSize = 2)
		public Map<String, String> stringMap = null;
	}
}

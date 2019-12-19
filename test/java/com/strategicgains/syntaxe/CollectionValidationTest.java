package com.strategicgains.syntaxe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).contains("requiredCollection"));
	}

	@Test
	public void shouldFailStringValidation()
	{
		Inner o = new Inner();
		o.requiredCollection = new ArrayList<>(Arrays.asList(new String[] {"blip", ""}));
		o.stringArray = new String[] {"", "", ""};
		List<String> errors = ValidationEngine.validate(o);
		assertEquals(2, errors.size());
		assertTrue(errors.get(0).contains("requiredCollection"));
		assertTrue(errors.get(1).contains("requiredCollection"));
	}

	@Test
	public void shouldFailSizeValidation()
	{
		Inner o = new Inner();
		o.requiredCollection = new ArrayList<>(Arrays.asList(new String[] {"a", "b"}));
		o.stringArray = new String[] {""};
		List<String> errors = ValidationEngine.validate(o);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).contains("stringArray"));
	}

	@Test
	public void shouldBeNullable()
	{
		Inner o = new Inner();
		o.requiredCollection = new ArrayList<>(Arrays.asList(new String[] {"a", "b"}));
		List<String> errors = ValidationEngine.validate(o);
		assertEquals(0, errors.size());
	}

	@Test
	public void shouldPassValidation()
	{
		Inner o = new Inner();
		o.requiredCollection = new ArrayList<>(Arrays.asList(new String[] {"foo", "bar", "b"}));
		o.stringArray = new String[] {"toddf", "toddfredrich", "isthebomb"};
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
	}
}

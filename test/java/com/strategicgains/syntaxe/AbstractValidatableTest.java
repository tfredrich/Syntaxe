package com.strategicgains.syntaxe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.strategicgains.syntaxe.annotation.Required;

public class AbstractValidatableTest
{
	@Test
	public void shouldValidateExtenderViaEngine()
	{
		List<String> errors = ValidationEngine.validate(new Inner());
		assertEquals(2, errors.size());
		assertTrue(errors.contains("'other' field is required"));
		assertTrue(errors.contains("String Value is required"));
	}

	@Test
	public void shouldValidateExtenderViaEngineAltCall()
	{
		List<String> errors = new ArrayList<>();
		ValidationEngine.validate(new Inner(), errors, null);
		assertEquals(2, errors.size());
		assertTrue(errors.contains("'other' field is required"));
		assertTrue(errors.contains("String Value is required"));
	}

//	@Test
//	public void shouldValidateExtenderViaValidate()
//	{
//		try
//		{
//			new Inner().validate();
//		}
//		catch(ValidationException v)
//		{
//			assertEquals("String Value is required, 'other' field is required", v.getMessage());
//		}
//	}

	@Test
	public void shouldValidateImplViaEngine()
	{
		List<String> errors = ValidationEngine.validate(new InnerImpl());
		assertEquals(2, errors.size());
		assertTrue(errors.contains("'other' field is required"));
		assertTrue(errors.contains("String Value is required"));
	}

	@Test
	public void shouldValidateImplViaEngineAltCall()
	{
		List<String> errors = new ArrayList<>();
		ValidationEngine.validate(new InnerImpl(), errors, null);
		assertEquals(2, errors.size());
		assertTrue(errors.contains("'other' field is required"));
		assertTrue(errors.contains("String Value is required"));
	}

//	@Test
//	public void shouldValidateImplViaValidate()
//	{
//		try
//		{
//			new InnerImpl().validate();
//		}
//		catch(ValidationException v)
//		{
//			assertEquals("'other' field is required, String Value is required", v.getMessage());
//		}
//	}

	public class Inner
	extends AbstractValidatable
	{
		@Required("String Value")
		private String string;

		private String other;

		@Override
		protected void validateInto(List<String> errors)
		{
			if (other == null)
			{
				errors.add("'other' field is required");
			}
		}
	}

	public class InnerImpl
	implements Validatable
	{
		@Required("String Value")
		private String string;

		private String other;

		@Override
		public void validate()
		{
			List<String> errors = ValidationEngine.validate(this);

			if (other == null)
			{
				errors.add("'other' field is required");
			}

			throw new ValidationException(errors);
		}
	}
}

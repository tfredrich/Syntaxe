/*
    Copyright 2012, Strategic Gains, Inc.

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

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.strategicgains.syntaxe.annotation.FieldValidation;
import com.strategicgains.syntaxe.annotation.StringValidation;
import com.strategicgains.syntaxe.validator.Validator;

/**
 * 
 * @author toddf
 * @since Nov 14, 2012
 */
public class FieldValidationTest
{
	@Test
	public void shouldCallFieldValidatorClass()
	{
		FieldValidationClass fv = new FieldValidationClass();
		fv.field1 = "toddf";
		fv.field2 = "was here";
		List<String> errors = ValidationEngine.validate(fv);
		assertNotNull(errors);
		assertEquals(2, errors.size());
		assertEquals(fv.field1, errors.get(0));
		assertEquals(fv.field2, errors.get(1));
	}

	@Test
	public void shouldCallAdditionalValidators()
	{
		FieldValidationClass fv = new FieldValidationClass();
		fv.field1 = "toddf";
		List<String> errors = ValidationEngine.validate(fv);
		assertNotNull(errors);
		assertEquals(2, errors.size());
		assertEquals(fv.field1, errors.get(0));
		assertEquals("Field 2 is required", errors.get(1));
	}

	@Test(expected = ValidationException.class)
	public void shouldThrowOnValidationError()
	{
		FieldValidationClass fv = new FieldValidationClass();
		fv.field1 = "toddf";
		fv.field2 = "who cares";
		ValidationEngine.validateAndThrow(fv);
	}

	public class FieldValidationClass
	{
		@FieldValidation(TestFieldValidator.class)
		public String field1;
		
		@StringValidation(name = "Field 2", required = true)
		public String field2;
	}

	public static class TestFieldValidator
	implements Validator<FieldValidationClass>
	{
		public TestFieldValidator()
		{
			super();
		}

		@Override
        public void perform(FieldValidationClass instance, List<String> errors, String prefix)
        {
			FieldValidationClass fv = ((FieldValidationClass) instance);
			errors.add(fv.field1);
			
			if (fv.field2 != null)
			{
				errors.add(fv.field2);
			}
        }
	}
}

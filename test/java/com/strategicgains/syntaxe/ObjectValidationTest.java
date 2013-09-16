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
package com.strategicgains.syntaxe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;


/**
 * @author toddf
 * @since Mar 18, 2011
 */
public class ObjectValidationTest
{
	@Test
	public void shouldCallValidatorForInnerOne()
	{
		InnerOne instance = new InnerOne();
		instance.setStringValue("Todd was here.");
		List<String> errors = ValidationEngine.validate(instance);
		assertEquals(1, errors.size());
		assertTrue(errors.get(0).startsWith("InnerOneValidator"));
		assertTrue(errors.get(0).endsWith("Todd was here."));
	}
	
	@Test
	public void shouldCallValidateForInnerTwo()
	{
		InnerTwo instance = new InnerTwo();
		List<String> errors = ValidationEngine.validate(instance);
		assertEquals(1, instance.getValidationCount());
		assertTrue(errors.isEmpty());
	}

	private class InnerTwo
	implements Validatable
	{
		private int validationCount = 0;

        @Override
        public void validate()
        {
        	++validationCount;
        }
        
        public int getValidationCount()
        {
        	return validationCount;
        }
	}
}

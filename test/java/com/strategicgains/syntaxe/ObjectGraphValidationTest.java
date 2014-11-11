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

import com.strategicgains.syntaxe.annotation.FieldValidation;
import com.strategicgains.syntaxe.annotation.Required;
import com.strategicgains.syntaxe.validator.impl.DefaultObjectValidator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ObjectGraphValidationTest
{
    // region Child Object
    @Test
    public void acyclicGraphWithoutErrors()
    {
        RootObject instance = new RootObject();
        ChildObject childObject = new ChildObject();
        instance.childObject = childObject;

        List<String> errors = ValidationEngine.validate(instance);

        assertEquals(1, instance.getValidationCount());
        assertEquals(1, childObject.getValidationCount());
        assertTrue(errors.isEmpty());
    }

    @Test
    public void cyclicGraphWithoutErrors()
    {
        RootObject instance = new RootObject();
        ChildObject childObject = new ChildObject();
        instance.childObject = childObject;
        childObject.parentObject = instance;

        List<String> errors = ValidationEngine.validate(instance);

        assertEquals(1, instance.getValidationCount());
        assertEquals(1, childObject.getValidationCount());
        assertTrue(errors.isEmpty());
    }

    @Test
    public void acyclicGraphWithErrors()
    {
        RootObject instance = new RootObject();
        instance.requiredField = null;
        ChildObject childObject = new ChildObject();
        instance.childObject = childObject;
        childObject.requiredField = null;

        List<String> errors = ValidationEngine.validate(instance);

        assertTrue(errors.get(0).endsWith("is required"));
        assertTrue(errors.get(1).endsWith("is required"));
        assertEquals(1, instance.getValidationCount());
        assertEquals(1, childObject.getValidationCount());
        assertEquals(2, errors.size());
    }

    @Test
    public void cyclicGraphWithErrors()
    {
        RootObject instance = new RootObject();
        instance.requiredField = null;
        ChildObject childObject = new ChildObject();
        instance.childObject = childObject;
        childObject.requiredField = null;
        childObject.parentObject = instance;

        List<String> errors = ValidationEngine.validate(instance);

        assertTrue(errors.get(0).endsWith("is required"));
        assertTrue(errors.get(1).endsWith("is required"));
        assertEquals(1, instance.getValidationCount());
        assertEquals(1, childObject.getValidationCount());
        assertEquals(2, errors.size());
    }

    // endregion

    // region Child Array

    @Test
    public void acyclicGraphArrayWithoutErrors()
    {
        RootObject instance = new RootObject();
        ChildObject[] childObject = new ChildObject[2];
        instance.childArray = childObject;
        childObject[0] = new ChildObject();
        childObject[1] = new ChildObject();
        childObject[0].parentObject = instance;
        childObject[1].parentObject = instance;

        List<String> errors = ValidationEngine.validate(instance);

        assertEquals(1, instance.getValidationCount());
        assertEquals(1, childObject[0].getValidationCount());
        assertEquals(1, childObject[1].getValidationCount());
        assertTrue(errors.isEmpty());
    }

    @Test
    public void cyclicGraphArrayWithoutErrors()
    {
        RootObject instance = new RootObject();
        ChildObject[] childObject = new ChildObject[2];
        instance.childArray = childObject;
        childObject[0] = new ChildObject();
        childObject[1] = new ChildObject();
        childObject[0].parentObject = instance;
        childObject[1].parentObject = instance;

        List<String> errors = ValidationEngine.validate(instance);

        assertEquals(1, instance.getValidationCount());
        assertEquals(1, childObject[0].getValidationCount());
        assertEquals(1, childObject[1].getValidationCount());
        assertTrue(errors.isEmpty());
    }

    @Test
    public void acyclicGraphArrayWithErrors()
    {
        RootObject instance = new RootObject();
        instance.requiredField = null;
        ChildObject[] childObject = new ChildObject[2];
        instance.childArray = childObject;
        childObject[0] = new ChildObject();
        childObject[1] = new ChildObject();
        childObject[0].parentObject = instance;
        childObject[1].parentObject = instance;
        childObject[0].requiredField = null;
        childObject[1].requiredField = null;

        List<String> errors = ValidationEngine.validate(instance);

        assertTrue(errors.get(0).endsWith("is required"));
        assertTrue(errors.get(1).endsWith("is required"));
        assertTrue(errors.get(2).endsWith("is required"));
        assertEquals(1, instance.getValidationCount());
        assertEquals(1, childObject[0].getValidationCount());
        assertEquals(1, childObject[1].getValidationCount());
        assertEquals(3, errors.size());
    }

    @Test
    public void cyclicGraphArrayWithErrors()
    {
        RootObject instance = new RootObject();
        instance.requiredField = null;
        ChildObject[] childObject = new ChildObject[2];
        instance.childArray = childObject;
        childObject[0] = new ChildObject();
        childObject[1] = new ChildObject();
        childObject[0].parentObject = instance;
        childObject[1].parentObject = instance;
        childObject[0].requiredField = null;
        childObject[1].requiredField = null;

        List<String> errors = ValidationEngine.validate(instance);

        assertTrue(errors.get(0).endsWith("is required"));
        assertTrue(errors.get(1).endsWith("is required"));
        assertTrue(errors.get(2).endsWith("is required"));
        assertEquals(1, instance.getValidationCount());
        assertEquals(1, childObject[0].getValidationCount());
        assertEquals(1, childObject[1].getValidationCount());
        assertEquals(3, errors.size());
    }

    // endregion

    // region Child List

    @Test
    public void acyclicGraphListWithoutErrors()
    {
        RootObject instance = new RootObject();
        List<ChildObject> childObject = new ArrayList<ChildObject>();
        instance.childList = childObject;
        childObject.add(new ChildObject());
        childObject.add(new ChildObject());
        childObject.get(0).parentObject = instance;
        childObject.get(1).parentObject = instance;

        List<String> errors = ValidationEngine.validate(instance);

        assertEquals(1, instance.getValidationCount());
        assertEquals(1, childObject.get(0).getValidationCount());
        assertEquals(1, childObject.get(1).getValidationCount());
        assertTrue(errors.isEmpty());
    }

    @Test
    public void cyclicGraphListWithoutErrors()
    {
        RootObject instance = new RootObject();
        List<ChildObject> childObject = new ArrayList<ChildObject>();
        instance.childList = childObject;
        childObject.add(new ChildObject());
        childObject.add(new ChildObject());
        childObject.get(0).parentObject = instance;
        childObject.get(1).parentObject = instance;

        List<String> errors = ValidationEngine.validate(instance);

        assertEquals(1, instance.getValidationCount());
        assertEquals(1, childObject.get(0).getValidationCount());
        assertEquals(1, childObject.get(1).getValidationCount());
        assertTrue(errors.isEmpty());
    }

    @Test
    public void acyclicGraphListWithErrors()
    {
        RootObject instance = new RootObject();
        instance.requiredField = null;
        List<ChildObject> childObject = new ArrayList<ChildObject>();
        instance.childList = childObject;
        childObject.add(new ChildObject());
        childObject.add(new ChildObject());
        childObject.get(0).parentObject = instance;
        childObject.get(1).parentObject = instance;
        childObject.get(0).requiredField = null;
        childObject.get(1).requiredField = null;

        List<String> errors = ValidationEngine.validate(instance);

        assertTrue(errors.get(0).endsWith("is required"));
        assertTrue(errors.get(1).endsWith("is required"));
        assertTrue(errors.get(2).endsWith("is required"));
        assertEquals(1, instance.getValidationCount());
        assertEquals(1, childObject.get(0).getValidationCount());
        assertEquals(1, childObject.get(1).getValidationCount());
        assertEquals(3, errors.size());
    }

    @Test
    public void cyclicGraphListWithErrors()
    {
        RootObject instance = new RootObject();
        instance.requiredField = null;
        List<ChildObject> childObject = new ArrayList<ChildObject>();
        instance.childList = childObject;
        childObject.add(new ChildObject());
        childObject.add(new ChildObject());
        childObject.get(0).parentObject = instance;
        childObject.get(1).parentObject = instance;
        childObject.get(0).requiredField = null;
        childObject.get(1).requiredField = null;

        List<String> errors = ValidationEngine.validate(instance);

        assertTrue(errors.get(0).endsWith("is required"));
        assertTrue(errors.get(1).endsWith("is required"));
        assertTrue(errors.get(2).endsWith("is required"));
        assertEquals(1, instance.getValidationCount());
        assertEquals(1, childObject.get(0).getValidationCount());
        assertEquals(1, childObject.get(1).getValidationCount());
        assertEquals(3, errors.size());
    }

    // endregion

    private class RootObject
	    implements Validatable
	{
        @FieldValidation(DefaultObjectValidator.class)
        private ChildObject childObject;

        @FieldValidation(DefaultObjectValidator.class)
        private ChildObject[] childArray;

        @FieldValidation(DefaultObjectValidator.class)
        private List<ChildObject> childList;

        @Required
        private Object requiredField = new Object();

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

    private class ChildObject
        implements Validatable
    {
        @FieldValidation(DefaultObjectValidator.class)
        private RootObject parentObject;

        @Required
        private Object requiredField = new Object();

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

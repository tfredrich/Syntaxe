/*
    Copyright 2014, Strategic Gains, Inc.

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
package com.strategicgains.syntaxe.validator.impl;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import com.strategicgains.syntaxe.ValidationEngine;
import com.strategicgains.syntaxe.annotation.ChildValidation;
import com.strategicgains.syntaxe.validator.AnnotatedFieldValidator;

/**
 * Implements the default behavior for validating objects. It performs
 * validation on the object if it implements {@code Validator},
 * on all the annotated fields and for any custom validator
 * annotated on the object.
 */

public class ChildObjectValidator
extends AnnotatedFieldValidator<ChildValidation>
{
    public ChildObjectValidator(Field field, ChildValidation annotation)
    {
        super(field, annotation);
    }

    @Override
    public void perform(Object instance, List<String> errors, String prefix)
    {
        String name = determineName(prefix);
        Object value = getValue(instance);

        if (value == null)
        {
            return;
        }

        if (isArray())
        {
            validateArray(name, false, (Object[]) value, errors);
        }
        else if (isCollection())
        {
            validateCollection(name, false, (Collection<Object>) value, errors);
        }
        else
        {
            validate(name, value, errors);
        }
    }

    @Override
    protected void validate(String name, Object value, List<String> errors) {
        List<String> newErrors = ValidationEngine.validate(value, name);
        errors.addAll(newErrors);
    }

    private String determineName(String prefix)
    {
    	return (getAnnotation().name().isEmpty() ? trimPrefix(prefix) + getFieldName() : getAnnotation().name());
    }
}

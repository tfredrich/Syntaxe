package com.strategicgains.syntaxe.validator.impl;

import com.strategicgains.syntaxe.ValidationEngine;
import com.strategicgains.syntaxe.annotation.FieldValidation;
import com.strategicgains.syntaxe.validator.AnnotatedFieldValidator;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

/**
 * Implements the default behavior for validating objects. It performs
 * validation on the object if it implements {@code Validator},
 * on all the annotated fields and for any custom validator
 * annotated on the object.
 */

public class DefaultObjectValidator
        extends AnnotatedFieldValidator<FieldValidation>
{
    public DefaultObjectValidator(Field field, FieldValidation annotation)
    {
        super(field, annotation);
    }

    @Override
    public void perform(Object instance, List<String> errors)
    {
        String name = determineName();
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
        List<String> newErrors = ValidationEngine.validate(value);
        errors.addAll(newErrors);
    }

    private String determineName()
    {
        return (getAnnotation().name().isEmpty() ? getFieldName() : getAnnotation().name());
    }
}

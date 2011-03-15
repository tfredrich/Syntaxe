package com.strategicgains.syntaxe.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.strategicgains.syntaxe.ValidationException;

public abstract class AbstractValidationProvider<AnnotationType extends Annotation>
implements ValidationProvider<AnnotationType>
{
	private AnnotationType annotation;
	
	public AnnotationType getAnnotation() {
		return annotation;
	}
	
	@SuppressWarnings("unchecked")
	public void setAnnotation(Annotation annotation) {
		this.annotation = (AnnotationType) annotation;
	}
	
	public Object getValue(Field field, Object object) throws ValidationException {
		field.setAccessible(true);
		try
		{
			return field.get(object);
		}
		catch (Exception e)
		{
			throw new ValidationException(e);
		}
	}
}

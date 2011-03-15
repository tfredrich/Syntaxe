package com.strategicgains.syntaxe.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import com.strategicgains.syntaxe.ValidationException;

public interface ValidationProvider<AnnotationType extends Annotation> {
	void setAnnotation(Annotation annotation);
	void perform(Object object, List<String> errors, Field field) throws ValidationException;
}

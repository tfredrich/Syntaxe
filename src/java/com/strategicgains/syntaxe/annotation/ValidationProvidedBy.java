package com.strategicgains.syntaxe.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(ANNOTATION_TYPE)
@Retention(RUNTIME)
public @interface ValidationProvidedBy {
	Class<? extends ValidationProvider<?>> name();
}

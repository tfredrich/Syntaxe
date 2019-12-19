package com.strategicgains.syntaxe.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.strategicgains.syntaxe.validator.impl.CollectionValidator;

/**
 * Enables validation of collection size and nullability/requiredness.
 * 
 * @author tfredrich
 * @since December 19, 2019
 */
@Target(FIELD)
@Retention(RUNTIME)
@ValidationProvider(CollectionValidator.class)
public @interface CollectionValidation
{
	String name() default "";
	boolean isNullable() default true;
	int minSize() default 0;
	int maxSize() default Integer.MAX_VALUE;
}

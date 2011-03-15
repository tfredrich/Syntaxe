package com.strategicgains.syntaxe.validators.regex;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.strategicgains.syntaxe.annotation.ValidationProvidedBy;

@Target(FIELD)
@Retention(RUNTIME)
@ValidationProvidedBy(name=RegexValidationProvider.class)
public @interface RegexValidate
{
	String name() default "Regex Validator";
	String pattern();
	boolean nullable() default false;
}

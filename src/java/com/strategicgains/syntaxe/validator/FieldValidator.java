/*
 * Copyright 2010, eCollege, Inc.  All rights reserved.
 */
package com.strategicgains.syntaxe.validator;



/**
 * @author toddf
 * @since Oct 14, 2010
 */
public interface FieldValidator
{
	public boolean isValid(Object value);
	public String getMessageFor(Object value);
}

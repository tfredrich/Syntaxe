/*
    Copyright 2010, Strategic Gains, Inc.

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
package com.strategicgains.syntaxe.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author toddf
 * @since Oct 7, 2010
 */
public class Validations
{
	private static final String MAX_LENGTH_ERROR = "%s is limited to %d characters";
	private static final String MIN_LENGTH_ERROR = "%s must contain at least %d characters";
	private static final String LESS_THAN_OR_EQUAL_ERROR = "%s must be less-than or equal-to %d";
	private static final String GREATER_THAN_OR_EQUAL_ERROR = "%s must be greater-than or equal-to %d";
	private static final String LESS_THAN_OR_EQUAL_FLOAT_ERROR = "%s must be less-than or equal-to %f";
	private static final String GREATER_THAN_OR_EQUAL_FLOAT_ERROR = "%s must be greater-than or equal-to %f";
	private static final String LESS_THAN_OR_EQUAL_DOUBLE_ERROR = "%s must be less-than or equal-to %f";
	private static final String GREATER_THAN_OR_EQUAL_DOUBLE_ERROR = "%s must be greater-than or equal-to %f";
	
	private Validations()
	{
		// Prevent instantiation.
	}

	public static void require(String name, Object value, List<String> errors)
	{
		// Strings are special--a required string can be neither null nor empty.
		if (value == null || (value.getClass().isAssignableFrom(String.class) && ((String) value).trim().isEmpty()))
    	{
			errors.add(name + " is required");
    	}
	}
	
	public static void maxLength(String name, String value, int max, List<String> errors)
	{
		if (value == null) return;
		
		if (value.length() > max)
		{
			errors.add(String.format(MAX_LENGTH_ERROR, name, max));
		}
	}
	
	public static void minLength(String name, String value, int min, List<String> errors)
	{
		if (value == null) return;
		
		if (value.length() < min)
		{
			errors.add(String.format(MIN_LENGTH_ERROR, name, min));
		}
	}

	public static void lessThanOrEqual(String name, int actual, int max, List<String> errors)
	{
		if (actual > max)
		{
			errors.add(String.format(LESS_THAN_OR_EQUAL_ERROR, name, max));
		}
	}

	public static void greaterThanOrEqual(String name, int actual, int min, List<String> errors)
	{
		if (actual < min)
		{
			errors.add(String.format(GREATER_THAN_OR_EQUAL_ERROR, name, min));
		}
	}

	public static void lessThanOrEqual(String name, long actual, long max, List<String> errors)
	{
		if (actual > max)
		{
			errors.add(String.format(LESS_THAN_OR_EQUAL_ERROR, name, max));
		}
	}

	public static void greaterThanOrEqual(String name, long actual, long min, List<String> errors)
	{
		if (actual < min)
		{
			errors.add(String.format(GREATER_THAN_OR_EQUAL_ERROR, name, min));
		}
	}

	public static void lessThanOrEqual(String name, float actual, float max, List<String> errors)
	{
		if (actual > max)
		{
			errors.add(String.format(LESS_THAN_OR_EQUAL_FLOAT_ERROR, name, max));
		}
	}

	public static void greaterThanOrEqual(String name, float actual, float min, List<String> errors)
	{
		if (actual < min)
		{
			errors.add(String.format(GREATER_THAN_OR_EQUAL_FLOAT_ERROR, name, min));
		}
	}

	public static void lessThanOrEqual(String name, double actual, double max, List<String> errors)
	{
		if (actual > max)
		{
			errors.add(String.format(LESS_THAN_OR_EQUAL_DOUBLE_ERROR, name, max));
		}
	}

	public static void greaterThanOrEqual(String name, double actual, double min, List<String> errors)
	{
		if (actual < min)
		{
			errors.add(String.format(GREATER_THAN_OR_EQUAL_DOUBLE_ERROR, name, min));
		}
	}

	public static void regex(String name, String value, Pattern regex, String message, List<String> errors)
	{
		Matcher matcher = regex.matcher(value);
		
		if (!matcher.matches())
		{
			if (message != null && !message.trim().isEmpty())
			{
				errors.add(name + " " + message);
			}
			else
			{
				errors.add(name + " does not match the regular expression pattern: " + regex.pattern());
			}
		}
	}
}

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
package com.strategicgains.syntaxe.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author toddf
 * @since Mar 17, 2011
 */
public abstract class AnnotatedFieldValidator<AnnotationType extends Annotation>
extends AbstractFieldValidator
{
	private AnnotationType annotation;

	public AnnotatedFieldValidator(Field field, AnnotationType annotation)
	{
		super(field);
		this.annotation = annotation;
	}
	
	protected AnnotationType getAnnotation()
	{
		return annotation;
	}
}

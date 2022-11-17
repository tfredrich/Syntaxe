/*
    Copyright 2022, Strategic Gains, Inc.

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
package com.strategicgains.syntaxe.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The ReadOnly annotation is simply a marker annotation applied to a field and indicates
 * it as read-only. While Syntaxe doesn't have any behavior associated with it, the
 * annotation can be used when generating JSON schema, etc. from the domain model.
 *
 * @author toddf
 * @since Nov 17, 2022
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface ReadOnly
{
}

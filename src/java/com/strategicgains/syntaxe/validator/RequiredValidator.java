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
package com.strategicgains.syntaxe.validator;


/**
 * Validates a field has content--that it has been populated--not null and not of zero length.
 * 
 * @author toddf
 * @since Nov 5, 2010
 */
public class RequiredValidator
implements FieldValidator
{
    @Override
    public boolean isValid(Object value)
    {
    	return (value != null && !value.toString().trim().isEmpty());
    }

    @Override
    public String getMessageFor(Object value)
    {
	    // TODO Auto-generated method stub
	    return null;
    }
}

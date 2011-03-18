package com.strategicgains.syntaxe;

import com.strategicgains.syntaxe.annotation.ObjectValidation;

@ObjectValidation(InnerOneValidator.class)
public class InnerOne
{
	private String stringValue = "toddf";

    public void setStringValue(String stringValue)
    {
        this.stringValue = stringValue;
    }

    public String getStringValue()
    {
        return stringValue;
    }
}

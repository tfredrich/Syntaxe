package com.strategicgains.syntaxe;

import java.util.List;

import com.strategicgains.syntaxe.validator.Validator;

public class InnerOneValidator
implements Validator
{
    @Override
    public void perform(Object object, List<String> errors)
    {
    	InnerOne instance = (InnerOne) object;
    	errors.add("InnerOneValidator.perform() was called: " + instance.getStringValue());
    }
}

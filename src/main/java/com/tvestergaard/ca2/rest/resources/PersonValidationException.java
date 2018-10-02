package com.tvestergaard.ca2.rest.resources;

import com.tvestergaard.ca2.rest.exceptions.ValidationException;

public class PersonValidationException extends ValidationException
{
    public PersonValidationException()
    {
        super("The person could not be validated.", 422);
    }
}

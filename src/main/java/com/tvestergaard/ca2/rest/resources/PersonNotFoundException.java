package com.tvestergaard.ca2.rest.resources;

import com.tvestergaard.ca2.rest.exceptions.APIException;

public class PersonNotFoundException extends APIException
{

    public PersonNotFoundException(Integer id)
    {
        super(String.format("No person with provided id %d.", id), 404);
    }
}

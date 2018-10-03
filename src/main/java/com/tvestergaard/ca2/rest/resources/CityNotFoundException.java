package com.tvestergaard.ca2.rest.resources;

import com.tvestergaard.ca2.rest.exceptions.APIException;

public class CityNotFoundException extends APIException
{
    public CityNotFoundException(Integer id)
    {
        super(String.format("No city with provided id '%d'.", id), 404);
    }
}

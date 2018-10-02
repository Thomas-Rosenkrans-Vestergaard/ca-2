package com.tvestergaard.ca2.rest.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidationException extends APIException
{

    public final List<String> validationErrors = new ArrayList<>();

    public ValidationException(String message, int responseCode)
    {
        super(message, responseCode);
    }

    public ValidationException(String message, Throwable cause, int responseCode)
    {
        super(message, cause, responseCode);
    }
}

package com.tvestergaard.ca2.rest.exceptions;

import net.sf.oval.ConstraintViolation;

import java.util.List;

public class ValidationException extends APIException
{

    public final List<ConstraintViolation> constraintViolations;

    public ValidationException(String message, List<ConstraintViolation> constraintViolations)
    {
        super(message, 422);
        this.constraintViolations = constraintViolations;
    }
}

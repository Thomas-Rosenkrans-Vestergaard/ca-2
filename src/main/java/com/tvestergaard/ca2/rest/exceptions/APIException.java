package com.tvestergaard.ca2.rest.exceptions;

public class APIException extends Exception
{

    private int responseCode;

    public APIException(String message, int responseCode)
    {
        super(message);

        this.responseCode = responseCode;
    }

    public APIException(String message, Throwable cause, int responseCode)
    {
        super(message, cause);

        this.responseCode = responseCode;
    }

    public int getResponseCode()
    {
        return this.responseCode;
    }
}
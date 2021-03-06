package com.tvestergaard.ca2.rest.exceptions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class APIExceptionMapper implements ExceptionMapper<APIException>
{

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Context
    private ServletContext context;

    @Override public Response toResponse(APIException exception)
    {
        boolean isDebug = "true".equals(context.getInitParameter("debug"));

        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.exception = exception.getClass().getSimpleName();
        exceptionResponse.message = exception.getMessage();
        exceptionResponse.responseCode = exception.getResponseCode();
        exceptionResponse.debug = isDebug;

        return Response.status(exception.getResponseCode()).entity(gson.toJson(exceptionResponse)).build();
    }
}

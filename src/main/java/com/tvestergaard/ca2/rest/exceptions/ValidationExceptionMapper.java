package com.tvestergaard.ca2.rest.exceptions;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.sf.oval.ConstraintViolation;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.List;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException>
{

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Context
    private ServletContext context;

    @Override public Response toResponse(ValidationException exception)
    {
        boolean isDebug = "true".equals(context.getInitParameter("debug"));

        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.exception = exception.getClass().getSimpleName();
        exceptionResponse.message = exception.getMessage();
        exceptionResponse.responseCode = exception.getResponseCode();
        exceptionResponse.debug = isDebug;
        for (ConstraintViolation constraintViolation : exception.constraintViolations)
            exceptionResponse.violations.add(new ConstraintViolationDTO(constraintViolation));

        return Response.status(exception.getResponseCode()).entity(gson.toJson(exceptionResponse)).build();
    }

    private class ExceptionResponse
    {
        public String                       exception;
        public String                       message;
        public Integer                      responseCode;
        public Boolean                      debug;
        public List<ConstraintViolationDTO> violations = new ArrayList<>();
    }

    private class ConstraintViolationDTO
    {
        public String message;
        public String checkName;
        public String invalidValue;

        public ConstraintViolationDTO(ConstraintViolation constraintViolation)
        {
            String checkName = constraintViolation.getCheckName();

            this.message = constraintViolation.getMessage();
            this.checkName = checkName.substring(checkName.lastIndexOf(".") + 1);
            try {
                this.invalidValue = String.valueOf(constraintViolation.getInvalidValue());
            } catch (Exception e) {
                this.invalidValue = "// Could not serialize value.";
            }
        }
    }
}

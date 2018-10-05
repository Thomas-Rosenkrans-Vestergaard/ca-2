package com.tvestergaard.ca2.rest.exceptions;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.context.FieldContext;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        for (ConstraintViolation constraintViolation : exception.constraintViolations) {
            if (constraintViolation.getCauses() != null) {
                for (ConstraintViolation cause : constraintViolation.getCauses())
                    exceptionResponse.violations.add(new ConstraintViolationDTO(cause));
            } else
                exceptionResponse.violations.add(new ConstraintViolationDTO(constraintViolation));
        }

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
        public String     message;
        public String     checkName;
        public String     attribute;
        public String     invalidValue;
        public JsonObject variables;

        public ConstraintViolationDTO(ConstraintViolation constraintViolation)
        {

            String checkName = constraintViolation.getCheckName();

            this.message = constraintViolation.getMessage();
            this.checkName = checkName.substring(checkName.lastIndexOf(".") + 1);
            if (constraintViolation.getContext() instanceof FieldContext)
                attribute = ((FieldContext) constraintViolation.getContext()).getField().getName();
            try {
                this.invalidValue = String.valueOf(constraintViolation.getInvalidValue());
            } catch (Exception e) {
                this.invalidValue = "// Could not serialize value.";
            }

            Map<String, ? extends Serializable> variables = constraintViolation.getMessageVariables();
            if (variables != null && !variables.isEmpty()) {
                this.variables = new JsonObject();
                for (Map.Entry<String, ? extends Serializable> entry : variables.entrySet()) {
                    String key = entry.getKey();
                    Object val = entry.getValue();

                    if (val instanceof String)
                        this.variables.addProperty(key, (String) val);
                    else if (val instanceof Number)
                        this.variables.addProperty(key, (Number) val);
                    else if (val instanceof Boolean)
                        this.variables.addProperty(key, (Boolean) val);
                }
            }
        }
    }
}

package com.chriseze.jp.processor.exceptionmappers;

import com.chriseze.jp.processor.enums.ResponseEnum;
import com.chriseze.jp.processor.restartifacts.BaseResponse;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintDeclarationException;
import javax.validation.ConstraintDefinitionException;
import javax.validation.GroupDefinitionException;
import javax.validation.ValidationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import org.jboss.resteasy.api.validation.ResteasyConstraintViolation;
import org.jboss.resteasy.api.validation.ResteasyViolationException;
import org.jboss.resteasy.api.validation.Validation;

public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {

    @Override
    public Response toResponse(ValidationException exception) {
        if (exception instanceof ConstraintDefinitionException) {
            return buildGenericErrorResponse();
        }

        if (exception instanceof ConstraintDeclarationException) {
            return buildGenericErrorResponse();
        }

        if (exception instanceof GroupDefinitionException) {
            return buildGenericErrorResponse();
        }

        if (exception instanceof ResteasyViolationException) {
            ResteasyViolationException resteasyViolationException = (ResteasyViolationException) exception;

            Exception exc = resteasyViolationException.getException();

            if (exc != null) {
//                log.error("VIOLATION EXCEPTION", exc);
                return buildGenericErrorResponse();
            } else {

                List<ResteasyConstraintViolation> violations = resteasyViolationException.getViolations();

                String responseDescription = violations.stream().map(ResteasyConstraintViolation::getMessage).collect(Collectors.joining(", "));

                BaseResponse response = new BaseResponse();

                response.setCode(ResponseEnum.INVALID_REQUEST.getCode());
                response.setDescription("Invalid request : "+responseDescription);

                return buildGenericErrorResponse(response);
            }
        }
        return buildGenericErrorResponse();
    }

    private Response buildGenericErrorResponse(BaseResponse baseResponse) {
        Response.ResponseBuilder responseBuilder = Response.ok().entity(baseResponse);
        responseBuilder.header(Validation.VALIDATION_HEADER, "true");

        return responseBuilder.build();
    }

    private Response buildGenericErrorResponse() {
        return buildGenericErrorResponse(new BaseResponse(ResponseEnum.INVALID_REQUEST));
    }
}

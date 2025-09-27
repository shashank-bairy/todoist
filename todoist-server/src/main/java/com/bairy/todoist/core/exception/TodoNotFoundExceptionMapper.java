package com.bairy.todoist.core.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Exception mapper for TodoNotFoundException.
 * Maps the exception to a 404 Not Found HTTP response.
 */
@Slf4j
@Provider
public class TodoNotFoundExceptionMapper implements ExceptionMapper<TodoNotFoundException> {
    
    @Override
    public Response toResponse(TodoNotFoundException exception) {
        log.warn("Todo not found: {}", exception.getMessage());
        return Response.status(Response.Status.NOT_FOUND)
                .entity(ErrorResponse.builder()
                        .code(404)
                        .message(exception.getMessage())
                        .build())
                .build();
    }
    
    /**
     * Simple error response class.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorResponse {
        private int code;
        private String message;
    }
}

package com.bairy.todoist.core.exception;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for TodoNotFoundExceptionMapper.
 */
class TodoNotFoundExceptionMapperTest {

    private TodoNotFoundExceptionMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new TodoNotFoundExceptionMapper();
    }

    @Test
    void toResponse_WithException_ShouldReturnNotFoundResponse() {
        // Given
        String errorMessage = "Todo with id 999 not found";
        TodoNotFoundException exception = new TodoNotFoundException(errorMessage);

        // When
        Response response = mapper.toResponse(exception);

        // Then
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NOT_FOUND);
        
        TodoNotFoundExceptionMapper.ErrorResponse errorResponse = 
                (TodoNotFoundExceptionMapper.ErrorResponse) response.getEntity();
        assertThat(errorResponse.getCode()).isEqualTo(404);
        assertThat(errorResponse.getMessage()).isEqualTo(errorMessage);
    }

    @Test
    void toResponse_WithNullMessage_ShouldReturnNotFoundResponse() {
        // Given
        TodoNotFoundException exception = new TodoNotFoundException();

        // When
        Response response = mapper.toResponse(exception);

        // Then
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        
        TodoNotFoundExceptionMapper.ErrorResponse errorResponse = 
                (TodoNotFoundExceptionMapper.ErrorResponse) response.getEntity();
        assertThat(errorResponse.getCode()).isEqualTo(404);
        assertThat(errorResponse.getMessage()).isNull();
    }

    @Test
    void toResponse_WithEmptyMessage_ShouldReturnNotFoundResponse() {
        // Given
        TodoNotFoundException exception = new TodoNotFoundException("");

        // When
        Response response = mapper.toResponse(exception);

        // Then
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        
        TodoNotFoundExceptionMapper.ErrorResponse errorResponse = 
                (TodoNotFoundExceptionMapper.ErrorResponse) response.getEntity();
        assertThat(errorResponse.getCode()).isEqualTo(404);
        assertThat(errorResponse.getMessage()).isEmpty();
    }
}

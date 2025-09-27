package com.bairy.todoist.core.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Unit tests for ValidationExceptionMapper.
 */
@ExtendWith(MockitoExtension.class)
class ValidationExceptionMapperTest {

    @Mock
    private ConstraintViolation<?> violation1;

    @Mock
    private ConstraintViolation<?> violation2;

    private ValidationExceptionMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ValidationExceptionMapper();
    }

    @Test
    void toResponse_WithSingleViolation_ShouldReturnBadRequestResponse() {
        // Given
        String errorMessage = "Title cannot be blank";
        when(violation1.getMessage()).thenReturn(errorMessage);
        
        Set<ConstraintViolation<?>> violations = new HashSet<>();
        violations.add(violation1);
        
        ConstraintViolationException exception = new ConstraintViolationException(violations);

        // When
        Response response = mapper.toResponse(exception);

        // Then
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.BAD_REQUEST);
        
        ValidationExceptionMapper.ErrorResponse errorResponse = 
                (ValidationExceptionMapper.ErrorResponse) response.getEntity();
        assertThat(errorResponse.getCode()).isEqualTo(400);
        assertThat(errorResponse.getMessage()).isEqualTo("Validation failed: " + errorMessage);
    }

    @Test
    void toResponse_WithMultipleViolations_ShouldReturnBadRequestResponseWithCombinedMessage() {
        // Given
        String message1 = "Title cannot be blank";
        String message2 = "Description cannot exceed 1000 characters";
        
        when(violation1.getMessage()).thenReturn(message1);
        when(violation2.getMessage()).thenReturn(message2);
        
        Set<ConstraintViolation<?>> violations = new HashSet<>();
        violations.add(violation1);
        violations.add(violation2);
        
        ConstraintViolationException exception = new ConstraintViolationException(violations);

        // When
        Response response = mapper.toResponse(exception);

        // Then
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        
        ValidationExceptionMapper.ErrorResponse errorResponse = 
                (ValidationExceptionMapper.ErrorResponse) response.getEntity();
        assertThat(errorResponse.getCode()).isEqualTo(400);
        assertThat(errorResponse.getMessage()).contains("Validation failed:");
        assertThat(errorResponse.getMessage()).contains(message1);
        assertThat(errorResponse.getMessage()).contains(message2);
    }

    @Test
    void toResponse_WithEmptyViolations_ShouldReturnBadRequestResponse() {
        // Given
        Set<ConstraintViolation<?>> violations = new HashSet<>();
        ConstraintViolationException exception = new ConstraintViolationException(violations);

        // When
        Response response = mapper.toResponse(exception);

        // Then
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        
        ValidationExceptionMapper.ErrorResponse errorResponse = 
                (ValidationExceptionMapper.ErrorResponse) response.getEntity();
        assertThat(errorResponse.getCode()).isEqualTo(400);
        assertThat(errorResponse.getMessage()).isEqualTo("Validation failed: ");
    }

    @Test
    void toResponse_WithNullViolations_ShouldReturnBadRequestResponse() {
        // Given
        ConstraintViolationException exception = new ConstraintViolationException("message", null);

        // When
        Response response = mapper.toResponse(exception);

        // Then
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        
        ValidationExceptionMapper.ErrorResponse errorResponse = 
                (ValidationExceptionMapper.ErrorResponse) response.getEntity();
        assertThat(errorResponse.getCode()).isEqualTo(400);
        assertThat(errorResponse.getMessage()).isEqualTo("Validation failed: ");
    }
}

package com.bairy.todoist.health;

import com.bairy.todoist.core.TodoService;
import com.codahale.metrics.health.HealthCheck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TodoHealthCheck.
 */
@ExtendWith(MockitoExtension.class)
class TodoHealthCheckTest {

    @Mock
    private TodoService todoService;

    private TodoHealthCheck healthCheck;

    @BeforeEach
    void setUp() {
        healthCheck = new TodoHealthCheck(todoService);
    }

    @Test
    void check_WhenServiceIsHealthy_ShouldReturnHealthy() throws Exception {
        // Given
        long todoCount = 5L;
        when(todoService.getTodoCount()).thenReturn(todoCount);

        // When
        HealthCheck.Result result = healthCheck.check();

        // Then
        assertThat(result.isHealthy()).isTrue();
        assertThat(result.getMessage()).contains("Todo service is healthy");
        assertThat(result.getMessage()).contains("Total todos: 5");
        verify(todoService).getTodoCount();
    }

    @Test
    void check_WhenServiceReturnsZeroCount_ShouldReturnHealthy() throws Exception {
        // Given
        when(todoService.getTodoCount()).thenReturn(0L);

        // When
        HealthCheck.Result result = healthCheck.check();

        // Then
        assertThat(result.isHealthy()).isTrue();
        assertThat(result.getMessage()).contains("Todo service is healthy");
        assertThat(result.getMessage()).contains("Total todos: 0");
        verify(todoService).getTodoCount();
    }

    @Test
    void check_WhenServiceThrowsException_ShouldReturnUnhealthy() throws Exception {
        // Given
        String errorMessage = "Database connection failed";
        when(todoService.getTodoCount()).thenThrow(new RuntimeException(errorMessage));

        // When
        HealthCheck.Result result = healthCheck.check();

        // Then
        assertThat(result.isHealthy()).isFalse();
        assertThat(result.getMessage()).contains("Todo service is unhealthy");
        assertThat(result.getMessage()).contains(errorMessage);
        verify(todoService).getTodoCount();
    }

    @Test
    void check_WhenServiceThrowsNullPointerException_ShouldReturnUnhealthy() throws Exception {
        // Given
        when(todoService.getTodoCount()).thenThrow(new NullPointerException("Null pointer"));

        // When
        HealthCheck.Result result = healthCheck.check();

        // Then
        assertThat(result.isHealthy()).isFalse();
        assertThat(result.getMessage()).contains("Todo service is unhealthy");
        assertThat(result.getMessage()).contains("Null pointer");
        verify(todoService).getTodoCount();
    }

    @Test
    void check_WhenServiceThrowsExceptionWithNullMessage_ShouldReturnUnhealthy() throws Exception {
        // Given
        when(todoService.getTodoCount()).thenThrow(new RuntimeException());

        // When
        HealthCheck.Result result = healthCheck.check();

        // Then
        assertThat(result.isHealthy()).isFalse();
        assertThat(result.getMessage()).contains("Todo service is unhealthy");
        assertThat(result.getMessage()).contains("null");
        verify(todoService).getTodoCount();
    }

    @Test
    void check_WhenServiceThrowsExceptionWithEmptyMessage_ShouldReturnUnhealthy() throws Exception {
        // Given
        when(todoService.getTodoCount()).thenThrow(new RuntimeException(""));

        // When
        HealthCheck.Result result = healthCheck.check();

        // Then
        assertThat(result.isHealthy()).isFalse();
        assertThat(result.getMessage()).contains("Todo service is unhealthy");
        assertThat(result.getMessage()).contains("");
        verify(todoService).getTodoCount();
    }
}

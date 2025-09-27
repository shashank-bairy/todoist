package com.bairy.todoist.health;

import com.bairy.todoist.core.TodoService;
import com.codahale.metrics.health.HealthCheck;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Health check for the Todo service.
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TodoHealthCheck extends HealthCheck {
    
    private final TodoService todoService;
    
    @Override
    protected Result check() throws Exception {
        try {
            // Simple health check - try to get todo count
            long count = todoService.getTodoCount();
            log.debug("Health check passed. Total todos: {}", count);
            return Result.healthy("Todo service is healthy. Total todos: " + count);
        } catch (Exception e) {
            log.error("Health check failed", e);
            return Result.unhealthy("Todo service is unhealthy: " + e.getMessage());
        }
    }
}

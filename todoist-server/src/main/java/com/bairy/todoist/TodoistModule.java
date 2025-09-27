package com.bairy.todoist;

import com.bairy.todoist.api.TodoResource;
import com.bairy.todoist.core.TodoService;
import com.bairy.todoist.core.exception.TodoNotFoundExceptionMapper;
import com.bairy.todoist.core.exception.ValidationExceptionMapper;
import com.bairy.todoist.db.InMemoryTodoRepository;
import com.bairy.todoist.db.TodoRepository;
import com.bairy.todoist.health.TodoHealthCheck;
import com.google.inject.AbstractModule;

/**
 * Guice module for Todoist application dependency injection.
 * Configures all the dependencies for the application.
 */
public class TodoistModule extends AbstractModule {
    
    @Override
    protected void configure() {
        // Bind repository interface to implementation
        bind(TodoRepository.class).to(InMemoryTodoRepository.class);
        
        // Bind service (will be automatically injected with repository)
        bind(TodoService.class);
        
        // Bind REST resources
        bind(TodoResource.class);
        
        // Bind exception mappers
        bind(TodoNotFoundExceptionMapper.class);
        bind(ValidationExceptionMapper.class);
        
        // Bind health checks
        bind(TodoHealthCheck.class);
    }
}

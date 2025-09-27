package com.bairy.todoist;

import com.bairy.todoist.api.TodoResource;
import com.bairy.todoist.core.exception.TodoNotFoundExceptionMapper;
import com.bairy.todoist.core.exception.ValidationExceptionMapper;
import com.bairy.todoist.health.TodoHealthCheck;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;

public class TodoistApplication extends Application<TodoistConfiguration> {

    public static void main(final String[] args) throws Exception {
        new TodoistApplication().run(args);
    }

    @Override
    public String getName() {
        return "Todoist";
    }

    @Override
    public void initialize(final Bootstrap<TodoistConfiguration> bootstrap) {
        // Application initialization
    }

    @Override
    public void run(final TodoistConfiguration configuration,
                    final Environment environment) {
        // Create Guice injector
        Injector injector = Guice.createInjector(new TodoistModule());
        
        // Register REST resources
        environment.jersey().register(injector.getInstance(TodoResource.class));
        
        // Register exception mappers
        environment.jersey().register(injector.getInstance(TodoNotFoundExceptionMapper.class));
        environment.jersey().register(injector.getInstance(ValidationExceptionMapper.class));
        
        // Register health checks
        environment.healthChecks().register("todo", injector.getInstance(TodoHealthCheck.class));
    }

}

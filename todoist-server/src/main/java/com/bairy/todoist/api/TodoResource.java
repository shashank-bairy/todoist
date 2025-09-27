package com.bairy.todoist.api;

import com.bairy.todoist.core.Todo;
import com.bairy.todoist.core.TodoService;
import com.bairy.todoist.core.dto.CreateTodoRequest;
import com.bairy.todoist.core.dto.UpdateTodoRequest;
import com.bairy.todoist.core.exception.TodoNotFoundException;
import com.google.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

/**
 * REST resource for Todo CRUD operations.
 * Provides HTTP endpoints for managing todos.
 */
@Slf4j
@Path("/todos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TodoResource {
    
    private final TodoService todoService;
    
    /**
     * Creates a new todo.
     * 
     * @param request the create request
     * @return the created todo
     */
    @POST
    public Response createTodo(@Valid @NotNull CreateTodoRequest request) {
        log.info("Creating new todo with title: {}", request.getTitle());
        Todo todo = todoService.createTodo(request);
        return Response.status(Response.Status.CREATED)
                .entity(todo)
                .build();
    }
    
    /**
     * Retrieves all todos.
     * 
     * @return list of all todos
     */
    @GET
    public Response getAllTodos() {
        List<Todo> todos = todoService.getAllTodos();
        return Response.ok(todos).build();
    }
    
    /**
     * Retrieves todos by completion status.
     * 
     * @param completed the completion status filter
     * @return list of todos matching the completion status
     */
    @GET
    @Path("/filter")
    public Response getTodosByCompleted(@QueryParam("completed") Boolean completed) {
        List<Todo> todos = todoService.getTodosByCompleted(completed);
        return Response.ok(todos).build();
    }
    
    /**
     * Retrieves a specific todo by ID.
     * 
     * @param id the todo ID
     * @return the todo if found
     */
    @GET
    @Path("/{id}")
    public Response getTodoById(@PathParam("id") Long id) {
        Optional<Todo> todo = todoService.getTodoById(id);
        if (todo.isPresent()) {
            return Response.ok(todo.get()).build();
        } else {
            throw new TodoNotFoundException("Todo with id " + id + " not found");
        }
    }
    
    /**
     * Updates an existing todo.
     * 
     * @param id the todo ID
     * @param request the update request
     * @return the updated todo
     */
    @PUT
    @Path("/{id}")
    public Response updateTodo(@PathParam("id") Long id, 
                              @Valid @NotNull UpdateTodoRequest request) {
        Optional<Todo> updatedTodo = todoService.updateTodo(id, request);
        if (updatedTodo.isPresent()) {
            return Response.ok(updatedTodo.get()).build();
        } else {
            throw new TodoNotFoundException("Todo with id " + id + " not found");
        }
    }
    
    /**
     * Deletes a todo by ID.
     * 
     * @param id the todo ID
     * @return 204 No Content if deleted successfully
     */
    @DELETE
    @Path("/{id}")
    public Response deleteTodo(@PathParam("id") Long id) {
        boolean deleted = todoService.deleteTodo(id);
        if (deleted) {
            return Response.noContent().build();
        } else {
            throw new TodoNotFoundException("Todo with id " + id + " not found");
        }
    }
    
    /**
     * Gets the total count of todos.
     * 
     * @return the total count
     */
    @GET
    @Path("/count")
    public Response getTodoCount() {
        long count = todoService.getTodoCount();
        return Response.ok(new CountResponse(count)).build();
    }
    
    /**
     * Simple response class for count endpoint.
     */
    @lombok.Data
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    public static class CountResponse {
        private long count;
    }
}

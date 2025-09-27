package com.bairy.todoist.core;

import com.bairy.todoist.core.dto.CreateTodoRequest;
import com.bairy.todoist.core.dto.UpdateTodoRequest;
import com.bairy.todoist.db.TodoRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

/**
 * Service class for Todo business logic.
 * Handles all business operations related to Todo management.
 */
@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TodoService {
    
    private final TodoRepository todoRepository;
    
    /**
     * Creates a new Todo.
     * 
     * @param request the create request containing todo details
     * @return the created Todo
     */
    public Todo createTodo(CreateTodoRequest request) {
        log.info("Creating new todo with title: {}", request.getTitle());
        Todo todo = Todo.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .build();
        return todoRepository.save(todo);
    }
    
    /**
     * Retrieves a Todo by ID.
     * 
     * @param id the ID of the Todo
     * @return Optional containing the Todo if found
     */
    public Optional<Todo> getTodoById(Long id) {
        log.debug("Retrieving todo by ID: {}", id);
        return todoRepository.findById(id);
    }
    
    /**
     * Retrieves all Todos.
     * 
     * @return List of all Todos
     */
    public List<Todo> getAllTodos() {
        log.debug("Retrieving all todos");
        return todoRepository.findAll();
    }
    
    /**
     * Retrieves Todos by completion status.
     * 
     * @param completed the completion status to filter by
     * @return List of Todos matching the completion status
     */
    public List<Todo> getTodosByCompleted(Boolean completed) {
        log.debug("Retrieving todos by completion status: {}", completed);
        return todoRepository.findByCompleted(completed);
    }
    
    /**
     * Updates an existing Todo.
     * 
     * @param id the ID of the Todo to update
     * @param request the update request containing new values
     * @return Optional containing the updated Todo if found and updated
     */
    public Optional<Todo> updateTodo(Long id, UpdateTodoRequest request) {
        log.info("Updating todo with ID: {}", id);
        return todoRepository.findById(id)
                .map(existingTodo -> {
                    // Update fields only if they are provided in the request
                    if (request.getTitle() != null) {
                        existingTodo.setTitle(request.getTitle());
                    }
                    if (request.getDescription() != null) {
                        existingTodo.setDescription(request.getDescription());
                    }
                    if (request.getCompleted() != null) {
                        existingTodo.setCompleted(request.getCompleted());
                    }
                    
                    return todoRepository.save(existingTodo);
                });
    }
    
    /**
     * Deletes a Todo by ID.
     * 
     * @param id the ID of the Todo to delete
     * @return true if the Todo was deleted, false if not found
     */
    public boolean deleteTodo(Long id) {
        log.info("Deleting todo with ID: {}", id);
        return todoRepository.deleteById(id);
    }
    
    /**
     * Checks if a Todo exists with the given ID.
     * 
     * @param id the ID to check
     * @return true if a Todo exists with this ID, false otherwise
     */
    public boolean todoExists(Long id) {
        log.debug("Checking if todo exists with ID: {}", id);
        return todoRepository.existsById(id);
    }
    
    /**
     * Gets the total count of Todos.
     * 
     * @return the total count of Todos
     */
    public long getTodoCount() {
        log.debug("Getting total todo count");
        return todoRepository.count();
    }
}

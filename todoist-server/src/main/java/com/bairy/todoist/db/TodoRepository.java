package com.bairy.todoist.db;

import com.bairy.todoist.core.Todo;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Todo entities.
 * Provides CRUD operations for Todo management.
 */
public interface TodoRepository {
    
    /**
     * Saves a new Todo or updates an existing one.
     * 
     * @param todo the Todo to save
     * @return the saved Todo with generated ID
     */
    Todo save(Todo todo);
    
    /**
     * Finds a Todo by its ID.
     * 
     * @param id the ID of the Todo
     * @return Optional containing the Todo if found, empty otherwise
     */
    Optional<Todo> findById(Long id);
    
    /**
     * Finds all Todos.
     * 
     * @return List of all Todos
     */
    List<Todo> findAll();
    
    /**
     * Finds all Todos with the specified completion status.
     * 
     * @param completed the completion status to filter by
     * @return List of Todos matching the completion status
     */
    List<Todo> findByCompleted(Boolean completed);
    
    /**
     * Deletes a Todo by its ID.
     * 
     * @param id the ID of the Todo to delete
     * @return true if the Todo was deleted, false if not found
     */
    boolean deleteById(Long id);
    
    /**
     * Checks if a Todo exists with the given ID.
     * 
     * @param id the ID to check
     * @return true if a Todo exists with this ID, false otherwise
     */
    boolean existsById(Long id);
    
    /**
     * Counts the total number of Todos.
     * 
     * @return the total count of Todos
     */
    long count();
}

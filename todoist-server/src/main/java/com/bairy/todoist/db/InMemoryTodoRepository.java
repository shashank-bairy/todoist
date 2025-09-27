package com.bairy.todoist.db;

import com.bairy.todoist.core.Todo;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * In-memory implementation of TodoRepository.
 * Uses ConcurrentHashMap for thread-safe operations and AtomicLong for ID generation.
 */
@Slf4j
@Singleton
public class InMemoryTodoRepository implements TodoRepository {
    
    private final Map<Long, Todo> todos = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    @Override
    public Todo save(Todo todo) {
        if (todo.getId() == null) {
            // New todo - generate ID
            todo.setId(idGenerator.getAndIncrement());
            log.debug("Created new todo with ID: {}", todo.getId());
        } else {
            log.debug("Updated existing todo with ID: {}", todo.getId());
        }
        todos.put(todo.getId(), todo);
        return todo;
    }
    
    @Override
    public Optional<Todo> findById(Long id) {
        log.debug("Finding todo by ID: {}", id);
        return Optional.ofNullable(todos.get(id));
    }
    
    @Override
    public List<Todo> findAll() {
        log.debug("Finding all todos, count: {}", todos.size());
        return new ArrayList<>(todos.values());
    }
    
    @Override
    public List<Todo> findByCompleted(Boolean completed) {
        log.debug("Finding todos by completed status: {}", completed);
        return todos.values().stream()
                .filter(todo -> Objects.equals(todo.getCompleted(), completed))
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean deleteById(Long id) {
        log.debug("Deleting todo by ID: {}", id);
        return todos.remove(id) != null;
    }
    
    @Override
    public boolean existsById(Long id) {
        log.debug("Checking if todo exists by ID: {}", id);
        return todos.containsKey(id);
    }
    
    @Override
    public long count() {
        long count = todos.size();
        log.debug("Getting todo count: {}", count);
        return count;
    }
}

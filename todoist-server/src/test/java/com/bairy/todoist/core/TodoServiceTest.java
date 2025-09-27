package com.bairy.todoist.core;

import com.bairy.todoist.core.dto.CreateTodoRequest;
import com.bairy.todoist.core.dto.UpdateTodoRequest;
import com.bairy.todoist.core.exception.TodoNotFoundException;
import com.bairy.todoist.db.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TodoService.
 */
@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    private TodoService todoService;

    @BeforeEach
    void setUp() {
        todoService = new TodoService(todoRepository);
    }

    @Test
    void createTodo_ValidRequest_ShouldCreateAndReturnTodo() {
        // Given
        CreateTodoRequest request = CreateTodoRequest.builder()
                .title("Test Todo")
                .description("Test Description")
                .build();
        
        Todo expectedTodo = Todo.builder()
                .id(1L)
                .title("Test Todo")
                .description("Test Description")
                .completed(false)
                .build();
        
        when(todoRepository.save(any(Todo.class))).thenReturn(expectedTodo);

        // When
        Todo result = todoService.createTodo(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test Todo");
        assertThat(result.getDescription()).isEqualTo("Test Description");
        assertThat(result.getCompleted()).isFalse();
        
        verify(todoRepository).save(any(Todo.class));
    }

    @Test
    void getTodoById_ExistingId_ShouldReturnTodo() {
        // Given
        Long id = 1L;
        Todo expectedTodo = Todo.builder()
                .id(id)
                .title("Test Todo")
                .build();
        
        when(todoRepository.findById(id)).thenReturn(Optional.of(expectedTodo));

        // When
        Optional<Todo> result = todoService.getTodoById(id);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(expectedTodo);
        verify(todoRepository).findById(id);
    }

    @Test
    void getTodoById_NonExistingId_ShouldReturnEmpty() {
        // Given
        Long id = 999L;
        when(todoRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<Todo> result = todoService.getTodoById(id);

        // Then
        assertThat(result).isEmpty();
        verify(todoRepository).findById(id);
    }

    @Test
    void getAllTodos_ShouldReturnAllTodos() {
        // Given
        List<Todo> expectedTodos = Arrays.asList(
                Todo.builder().id(1L).title("Todo 1").build(),
                Todo.builder().id(2L).title("Todo 2").build()
        );
        
        when(todoRepository.findAll()).thenReturn(expectedTodos);

        // When
        List<Todo> result = todoService.getAllTodos();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(expectedTodos);
        verify(todoRepository).findAll();
    }

    @Test
    void getTodosByCompleted_WithCompletedTrue_ShouldReturnCompletedTodos() {
        // Given
        List<Todo> completedTodos = Arrays.asList(
                Todo.builder().id(1L).title("Completed 1").completed(true).build(),
                Todo.builder().id(2L).title("Completed 2").completed(true).build()
        );
        
        when(todoRepository.findByCompleted(true)).thenReturn(completedTodos);

        // When
        List<Todo> result = todoService.getTodosByCompleted(true);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(completedTodos);
        verify(todoRepository).findByCompleted(true);
    }

    @Test
    void getTodosByCompleted_WithCompletedFalse_ShouldReturnPendingTodos() {
        // Given
        List<Todo> pendingTodos = Arrays.asList(
                Todo.builder().id(1L).title("Pending 1").completed(false).build()
        );
        
        when(todoRepository.findByCompleted(false)).thenReturn(pendingTodos);

        // When
        List<Todo> result = todoService.getTodosByCompleted(false);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(pendingTodos);
        verify(todoRepository).findByCompleted(false);
    }

    @Test
    void updateTodo_ExistingId_ShouldUpdateAndReturnTodo() {
        // Given
        Long id = 1L;
        UpdateTodoRequest request = UpdateTodoRequest.builder()
                .title("Updated Title")
                .description("Updated Description")
                .completed(true)
                .build();
        
        Todo existingTodo = Todo.builder()
                .id(id)
                .title("Original Title")
                .description("Original Description")
                .completed(false)
                .build();
        
        Todo updatedTodo = Todo.builder()
                .id(id)
                .title("Updated Title")
                .description("Updated Description")
                .completed(true)
                .build();
        
        when(todoRepository.findById(id)).thenReturn(Optional.of(existingTodo));
        when(todoRepository.save(any(Todo.class))).thenReturn(updatedTodo);

        // When
        Optional<Todo> result = todoService.updateTodo(id, request);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Updated Title");
        assertThat(result.get().getDescription()).isEqualTo("Updated Description");
        assertThat(result.get().getCompleted()).isTrue();
        
        verify(todoRepository).findById(id);
        verify(todoRepository).save(existingTodo);
    }

    @Test
    void updateTodo_PartialUpdate_ShouldUpdateOnlyProvidedFields() {
        // Given
        Long id = 1L;
        UpdateTodoRequest request = UpdateTodoRequest.builder()
                .title("Updated Title")
                .completed(true)
                .build(); // description not provided
        
        Todo existingTodo = Todo.builder()
                .id(id)
                .title("Original Title")
                .description("Original Description")
                .completed(false)
                .build();
        
        when(todoRepository.findById(id)).thenReturn(Optional.of(existingTodo));
        when(todoRepository.save(any(Todo.class))).thenReturn(existingTodo);

        // When
        Optional<Todo> result = todoService.updateTodo(id, request);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Updated Title");
        assertThat(result.get().getDescription()).isEqualTo("Original Description"); // unchanged
        assertThat(result.get().getCompleted()).isTrue();
        
        verify(todoRepository).findById(id);
        verify(todoRepository).save(existingTodo);
    }

    @Test
    void updateTodo_NonExistingId_ShouldReturnEmpty() {
        // Given
        Long id = 999L;
        UpdateTodoRequest request = UpdateTodoRequest.builder()
                .title("Updated Title")
                .build();
        
        when(todoRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<Todo> result = todoService.updateTodo(id, request);

        // Then
        assertThat(result).isEmpty();
        verify(todoRepository).findById(id);
        verify(todoRepository, never()).save(any(Todo.class));
    }

    @Test
    void deleteTodo_ExistingId_ShouldReturnTrue() {
        // Given
        Long id = 1L;
        when(todoRepository.deleteById(id)).thenReturn(true);

        // When
        boolean result = todoService.deleteTodo(id);

        // Then
        assertThat(result).isTrue();
        verify(todoRepository).deleteById(id);
    }

    @Test
    void deleteTodo_NonExistingId_ShouldReturnFalse() {
        // Given
        Long id = 999L;
        when(todoRepository.deleteById(id)).thenReturn(false);

        // When
        boolean result = todoService.deleteTodo(id);

        // Then
        assertThat(result).isFalse();
        verify(todoRepository).deleteById(id);
    }

    @Test
    void todoExists_ExistingId_ShouldReturnTrue() {
        // Given
        Long id = 1L;
        when(todoRepository.existsById(id)).thenReturn(true);

        // When
        boolean result = todoService.todoExists(id);

        // Then
        assertThat(result).isTrue();
        verify(todoRepository).existsById(id);
    }

    @Test
    void todoExists_NonExistingId_ShouldReturnFalse() {
        // Given
        Long id = 999L;
        when(todoRepository.existsById(id)).thenReturn(false);

        // When
        boolean result = todoService.todoExists(id);

        // Then
        assertThat(result).isFalse();
        verify(todoRepository).existsById(id);
    }

    @Test
    void getTodoCount_ShouldReturnCount() {
        // Given
        long expectedCount = 5L;
        when(todoRepository.count()).thenReturn(expectedCount);

        // When
        long result = todoService.getTodoCount();

        // Then
        assertThat(result).isEqualTo(expectedCount);
        verify(todoRepository).count();
    }
}

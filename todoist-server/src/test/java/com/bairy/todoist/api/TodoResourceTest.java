package com.bairy.todoist.api;

import com.bairy.todoist.core.Todo;
import com.bairy.todoist.core.TodoService;
import com.bairy.todoist.core.dto.CreateTodoRequest;
import com.bairy.todoist.core.dto.UpdateTodoRequest;
import com.bairy.todoist.core.exception.TodoNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TodoResource.
 */
@ExtendWith(MockitoExtension.class)
class TodoResourceTest {

    @Mock
    private TodoService todoService;

    private TodoResource todoResource;

    @BeforeEach
    void setUp() {
        todoResource = new TodoResource(todoService);
    }

    @Test
    void createTodo_ValidRequest_ShouldReturnCreated() {
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
        
        when(todoService.createTodo(any(CreateTodoRequest.class))).thenReturn(expectedTodo);

        // When
        Response response = todoResource.createTodo(request);

        // Then
        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(expectedTodo);
        verify(todoService).createTodo(request);
    }

    @Test
    void getAllTodos_ShouldReturnAllTodos() {
        // Given
        List<Todo> expectedTodos = Arrays.asList(
                Todo.builder().id(1L).title("Todo 1").build(),
                Todo.builder().id(2L).title("Todo 2").build()
        );
        
        when(todoService.getAllTodos()).thenReturn(expectedTodos);

        // When
        Response response = todoResource.getAllTodos();

        // Then
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(expectedTodos);
        verify(todoService).getAllTodos();
    }

    @Test
    void getTodosByCompleted_WithCompletedTrue_ShouldReturnCompletedTodos() {
        // Given
        List<Todo> completedTodos = Arrays.asList(
                Todo.builder().id(1L).title("Completed 1").completed(true).build()
        );
        
        when(todoService.getTodosByCompleted(true)).thenReturn(completedTodos);

        // When
        Response response = todoResource.getTodosByCompleted(true);

        // Then
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(completedTodos);
        verify(todoService).getTodosByCompleted(true);
    }

    @Test
    void getTodosByCompleted_WithCompletedFalse_ShouldReturnPendingTodos() {
        // Given
        List<Todo> pendingTodos = Arrays.asList(
                Todo.builder().id(1L).title("Pending 1").completed(false).build()
        );
        
        when(todoService.getTodosByCompleted(false)).thenReturn(pendingTodos);

        // When
        Response response = todoResource.getTodosByCompleted(false);

        // Then
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(pendingTodos);
        verify(todoService).getTodosByCompleted(false);
    }

    @Test
    void getTodoById_ExistingId_ShouldReturnTodo() {
        // Given
        Long id = 1L;
        Todo expectedTodo = Todo.builder()
                .id(id)
                .title("Test Todo")
                .build();
        
        when(todoService.getTodoById(id)).thenReturn(Optional.of(expectedTodo));

        // When
        Response response = todoResource.getTodoById(id);

        // Then
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(expectedTodo);
        verify(todoService).getTodoById(id);
    }

    @Test
    void getTodoById_NonExistingId_ShouldThrowNotFoundException() {
        // Given
        Long id = 999L;
        when(todoService.getTodoById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> todoResource.getTodoById(id))
                .isInstanceOf(TodoNotFoundException.class)
                .hasMessage("Todo with id 999 not found");
        
        verify(todoService).getTodoById(id);
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
        
        Todo updatedTodo = Todo.builder()
                .id(id)
                .title("Updated Title")
                .description("Updated Description")
                .completed(true)
                .build();
        
        when(todoService.updateTodo(id, request)).thenReturn(Optional.of(updatedTodo));

        // When
        Response response = todoResource.updateTodo(id, request);

        // Then
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(updatedTodo);
        verify(todoService).updateTodo(id, request);
    }

    @Test
    void updateTodo_NonExistingId_ShouldThrowNotFoundException() {
        // Given
        Long id = 999L;
        UpdateTodoRequest request = UpdateTodoRequest.builder()
                .title("Updated Title")
                .build();
        
        when(todoService.updateTodo(id, request)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> todoResource.updateTodo(id, request))
                .isInstanceOf(TodoNotFoundException.class)
                .hasMessage("Todo with id 999 not found");
        
        verify(todoService).updateTodo(id, request);
    }

    @Test
    void deleteTodo_ExistingId_ShouldReturnNoContent() {
        // Given
        Long id = 1L;
        when(todoService.deleteTodo(id)).thenReturn(true);

        // When
        Response response = todoResource.deleteTodo(id);

        // Then
        assertThat(response.getStatus()).isEqualTo(Response.Status.NO_CONTENT.getStatusCode());
        verify(todoService).deleteTodo(id);
    }

    @Test
    void deleteTodo_NonExistingId_ShouldThrowNotFoundException() {
        // Given
        Long id = 999L;
        when(todoService.deleteTodo(id)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> todoResource.deleteTodo(id))
                .isInstanceOf(TodoNotFoundException.class)
                .hasMessage("Todo with id 999 not found");
        
        verify(todoService).deleteTodo(id);
    }

    @Test
    void getTodoCount_ShouldReturnCount() {
        // Given
        long expectedCount = 5L;
        when(todoService.getTodoCount()).thenReturn(expectedCount);

        // When
        Response response = todoResource.getTodoCount();

        // Then
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        TodoResource.CountResponse countResponse = (TodoResource.CountResponse) response.getEntity();
        assertThat(countResponse.getCount()).isEqualTo(expectedCount);
        verify(todoService).getTodoCount();
    }

}

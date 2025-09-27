package com.bairy.todoist.db;

import com.bairy.todoist.core.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for InMemoryTodoRepository.
 */
class InMemoryTodoRepositoryTest {

    private InMemoryTodoRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryTodoRepository();
    }

    @Test
    void save_NewTodo_ShouldGenerateIdAndSave() {
        // Given
        Todo todo = Todo.builder()
                .title("Test Todo")
                .description("Test Description")
                .build();

        // When
        Todo savedTodo = repository.save(todo);

        // Then
        assertThat(savedTodo.getId()).isNotNull();
        assertThat(savedTodo.getTitle()).isEqualTo("Test Todo");
        assertThat(savedTodo.getDescription()).isEqualTo("Test Description");
        assertThat(savedTodo.getCompleted()).isFalse();
        assertThat(savedTodo.getCreatedAt()).isNotNull();
        assertThat(savedTodo.getUpdatedAt()).isNotNull();
    }

    @Test
    void save_ExistingTodo_ShouldUpdateAndReturn() {
        // Given
        Todo todo = Todo.builder()
                .title("Original Title")
                .description("Original Description")
                .build();
        Todo savedTodo = repository.save(todo);
        Long id = savedTodo.getId();

        // When
        savedTodo.setTitle("Updated Title");
        savedTodo.setDescription("Updated Description");
        savedTodo.setCompleted(true);
        Todo updatedTodo = repository.save(savedTodo);

        // Then
        assertThat(updatedTodo.getId()).isEqualTo(id);
        assertThat(updatedTodo.getTitle()).isEqualTo("Updated Title");
        assertThat(updatedTodo.getDescription()).isEqualTo("Updated Description");
        assertThat(updatedTodo.getCompleted()).isTrue();
    }

    @Test
    void findById_ExistingId_ShouldReturnTodo() {
        // Given
        Todo todo = Todo.builder()
                .title("Test Todo")
                .description("Test Description")
                .build();
        Todo savedTodo = repository.save(todo);
        Long id = savedTodo.getId();

        // When
        Optional<Todo> foundTodo = repository.findById(id);

        // Then
        assertThat(foundTodo).isPresent();
        assertThat(foundTodo.get().getId()).isEqualTo(id);
        assertThat(foundTodo.get().getTitle()).isEqualTo("Test Todo");
    }

    @Test
    void findById_NonExistingId_ShouldReturnEmpty() {
        // When
        Optional<Todo> foundTodo = repository.findById(999L);

        // Then
        assertThat(foundTodo).isEmpty();
    }

    @Test
    void findAll_WithMultipleTodos_ShouldReturnAll() {
        // Given
        Todo todo1 = Todo.builder().title("Todo 1").build();
        Todo todo2 = Todo.builder().title("Todo 2").build();
        repository.save(todo1);
        repository.save(todo2);

        // When
        List<Todo> allTodos = repository.findAll();

        // Then
        assertThat(allTodos).hasSize(2);
        assertThat(allTodos).extracting(Todo::getTitle)
                .containsExactlyInAnyOrder("Todo 1", "Todo 2");
    }

    @Test
    void findByCompleted_WithCompletedTodos_ShouldReturnOnlyCompleted() {
        // Given
        Todo completedTodo1 = Todo.builder().title("Completed 1").completed(true).build();
        Todo completedTodo2 = Todo.builder().title("Completed 2").completed(true).build();
        Todo pendingTodo = Todo.builder().title("Pending").completed(false).build();
        
        repository.save(completedTodo1);
        repository.save(completedTodo2);
        repository.save(pendingTodo);

        // When
        List<Todo> completedTodos = repository.findByCompleted(true);
        List<Todo> pendingTodos = repository.findByCompleted(false);

        // Then
        assertThat(completedTodos).hasSize(2);
        assertThat(completedTodos).extracting(Todo::getTitle)
                .containsExactlyInAnyOrder("Completed 1", "Completed 2");
        
        assertThat(pendingTodos).hasSize(1);
        assertThat(pendingTodos).extracting(Todo::getTitle)
                .containsExactly("Pending");
    }

    @Test
    void deleteById_ExistingId_ShouldReturnTrueAndRemove() {
        // Given
        Todo todo = Todo.builder().title("Test Todo").build();
        Todo savedTodo = repository.save(todo);
        Long id = savedTodo.getId();

        // When
        boolean deleted = repository.deleteById(id);

        // Then
        assertThat(deleted).isTrue();
        assertThat(repository.findById(id)).isEmpty();
        assertThat(repository.count()).isZero();
    }

    @Test
    void deleteById_NonExistingId_ShouldReturnFalse() {
        // When
        boolean deleted = repository.deleteById(999L);

        // Then
        assertThat(deleted).isFalse();
    }

    @Test
    void existsById_ExistingId_ShouldReturnTrue() {
        // Given
        Todo todo = Todo.builder().title("Test Todo").build();
        Todo savedTodo = repository.save(todo);
        Long id = savedTodo.getId();

        // When
        boolean exists = repository.existsById(id);

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void existsById_NonExistingId_ShouldReturnFalse() {
        // When
        boolean exists = repository.existsById(999L);

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void count_WithMultipleTodos_ShouldReturnCorrectCount() {
        // Given
        repository.save(Todo.builder().title("Todo 1").build());
        repository.save(Todo.builder().title("Todo 2").build());
        repository.save(Todo.builder().title("Todo 3").build());

        // When
        long count = repository.count();

        // Then
        assertThat(count).isEqualTo(3);
    }

    @Test
    void count_EmptyRepository_ShouldReturnZero() {
        // When
        long count = repository.count();

        // Then
        assertThat(count).isZero();
    }

    @Test
    void save_MultipleTodos_ShouldGenerateUniqueIds() {
        // Given
        Todo todo1 = Todo.builder().title("Todo 1").build();
        Todo todo2 = Todo.builder().title("Todo 2").build();
        Todo todo3 = Todo.builder().title("Todo 3").build();

        // When
        Todo saved1 = repository.save(todo1);
        Todo saved2 = repository.save(todo2);
        Todo saved3 = repository.save(todo3);

        // Then
        assertThat(saved1.getId()).isNotEqualTo(saved2.getId());
        assertThat(saved2.getId()).isNotEqualTo(saved3.getId());
        assertThat(saved1.getId()).isNotEqualTo(saved3.getId());
    }
}

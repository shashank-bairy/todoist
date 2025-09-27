package com.bairy.todoist.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a Todo item in the system.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Todo {
    
    @JsonProperty
    private Long id;
    
    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    @JsonProperty
    private String title;
    
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    @JsonProperty
    private String description;
    
    @NotNull(message = "Completed status cannot be null")
    @JsonProperty
    @Builder.Default
    private Boolean completed = false;
    
    @JsonProperty
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @JsonProperty
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    // Custom setter for title to update updatedAt
    public void setTitle(String title) {
        this.title = title;
        this.updatedAt = LocalDateTime.now();
    }
    
    // Custom setter for description to update updatedAt
    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }
    
    // Custom setter for completed to update updatedAt
    public void setCompleted(Boolean completed) {
        this.completed = completed;
        this.updatedAt = LocalDateTime.now();
    }
}

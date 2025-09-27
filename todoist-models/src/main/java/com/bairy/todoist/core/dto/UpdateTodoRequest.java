package com.bairy.todoist.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating an existing Todo.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTodoRequest {
    
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    @JsonProperty
    private String title;
    
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    @JsonProperty
    private String description;
    
    @JsonProperty
    private Boolean completed;
}

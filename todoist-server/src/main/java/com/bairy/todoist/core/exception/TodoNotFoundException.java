package com.bairy.todoist.core.exception;

import lombok.experimental.StandardException;

/**
 * Exception thrown when a Todo is not found.
 */
@StandardException
public class TodoNotFoundException extends RuntimeException {
}

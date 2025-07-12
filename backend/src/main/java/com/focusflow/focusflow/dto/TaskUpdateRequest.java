package com.focusflow.focusflow.dto;

import lombok.Data;

@Data
public class TaskUpdateRequest {
    private TaskKeyDTO key;
    private boolean completed;
}

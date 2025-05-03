package com.example.todo.controller.task;

import com.example.todo.entity.Task;

public record TaskDTO(
        long id,
        long category_id,
        String summary,
        String description,
        String status
) {
    public static TaskDTO toDTO(Task entity) {
        return new TaskDTO(
                entity.getId(),
                entity.getCategory().getId(),
                entity.getSummary(),
                entity.getDescription(),
                entity.getStatus().name()
        );
    }
}

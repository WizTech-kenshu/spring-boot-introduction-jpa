package com.example.todo.entity;

import lombok.Data;
import com.example.todo.service.task.TaskStatus;
import com.example.todo.entity.Category;

import javax.persistence.*;

@Data
@Entity
@Table(name="tasks")
public class Task {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
   private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private  String summary;
    private String description;
    private TaskStatus status;

    public Task( String summary, String description, TaskStatus taskStatus) {
        this.summary = summary;
        this.description = description;
        this.status = taskStatus;
    }

    public Task( long id,String summary, String description, TaskStatus taskStatus) {
        this.id = id;
        this.summary = summary;
        this.description = description;
        this.status = taskStatus;
    }

}
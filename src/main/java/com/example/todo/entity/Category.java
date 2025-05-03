package com.example.todo.entity;

import java.util.List;

import lombok.Data;
import lombok.ToString;

import jakarta.persistence.*;

@Data
@Entity
@Table(name="categories")
public class Category {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;


    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Task> tasks;


    public Category() {
    }

    public Category( String name, String description, List<Object> objects) {
    }
}




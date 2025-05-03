package com.example.todo.repository.category;

import com.example.todo.entity.Category;
import com.example.todo.entity.Task;
import com.example.todo.service.task.TaskSearchEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}

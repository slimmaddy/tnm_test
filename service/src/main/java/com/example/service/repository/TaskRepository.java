package com.example.service.repository;

import com.example.service.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Task.TaskId> {

    @Query(value = "select t from Task t " +
            "left join TeamAssignment ta " +
            "on t.taskId = ta.taskId " +
            "where ta.taskId is null")
    List<Task> getUnassignedTasks();
}

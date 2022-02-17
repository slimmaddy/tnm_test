package com.example.service.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "task")
@IdClass(Task.TaskId.class)
public class Task {
    @Id
    @Column(name = "task_id")
    private String taskId;

    @Id
    @Column(name = "skill")
    private String skill;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TaskId implements Serializable {
        private String taskId;
        private String skill;
    }
}

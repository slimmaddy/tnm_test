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
@Table(name = "team_assignment")
@IdClass(TeamAssignment.TeamAssignmentId.class)
public class TeamAssignment {
    @Id
    @Column(name = "team_id")
    private String teamId;

    @Id
    @Column(name = "task_id")
    private String taskId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TeamAssignmentId implements Serializable {
        private String teamId;
        private String taskId;
    }
}

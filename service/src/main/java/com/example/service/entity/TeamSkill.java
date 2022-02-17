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
@Table(name = "team_skill")
@IdClass(TeamSkill.TeamSkillId.class)
public class TeamSkill {
    @Id
    @Column(name = "team_id")
    private String teamId;

    @Id
    @Column(name = "skill")
    private String skill;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TeamSkillId implements Serializable {
        private String teamId;
        private String skill;
    }
}

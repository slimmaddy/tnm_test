package com.example.service.repository;

import com.example.service.entity.TeamSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamSkillRepository extends JpaRepository<TeamSkill, TeamSkill.TeamSkillId> {
    List<TeamSkill> findBySkill(String skill);
}

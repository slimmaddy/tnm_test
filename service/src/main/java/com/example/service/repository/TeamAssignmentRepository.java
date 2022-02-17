package com.example.service.repository;

import com.example.service.entity.TeamAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamAssignmentRepository extends JpaRepository<TeamAssignment, TeamAssignment.TeamAssignmentId> {

}

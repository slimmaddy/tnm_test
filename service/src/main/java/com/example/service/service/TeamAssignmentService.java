package com.example.service.service;

import com.example.service.entity.TeamAssignment;

import java.util.List;

public interface TeamAssignmentService {
    void assignTask();
    List<TeamAssignment> getAllTeamAssignment();
}

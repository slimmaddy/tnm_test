package com.example.service.service.impl;

import com.example.service.CollectionsUtil;
import com.example.service.entity.Task;
import com.example.service.entity.TeamAssignment;
import com.example.service.entity.TeamSkill;
import com.example.service.repository.TaskRepository;
import com.example.service.repository.TeamAssignmentRepository;
import com.example.service.repository.TeamRepository;
import com.example.service.repository.TeamSkillRepository;
import com.example.service.service.TeamAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeamAssignmentServiceImpl implements TeamAssignmentService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamSkillRepository teamSkillRepository;

    @Autowired
    private TeamAssignmentRepository teamAssignmentRepository;

    @Override
    @Transactional
    public void assignTask() {
        List<Task> listUnassignedTask = taskRepository.getUnassignedTasks();
        Map<String, Set<String>> taskSkillsMap = listUnassignedTask.stream()
                .collect(Collectors.groupingBy(Task::getTaskId))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream().map(Task::getSkill).collect(Collectors.toSet()))
                );

        taskSkillsMap.forEach((taskId, skills) -> {
            Set<Set<String>> candidateTeamMatchSkill = skills.stream()
                    .map(skill -> teamSkillRepository.findBySkill(skill).stream().map(TeamSkill::getTeamId).collect(Collectors.toSet()))
                    .collect(Collectors.toSet());

            Set<String> qualifiedTeams = CollectionsUtil.intersect(candidateTeamMatchSkill);
            if (qualifiedTeams.size() != 0) {
                String bestTeamMatch = teamRepository.getAssignedTaskCount(qualifiedTeams);
                teamAssignmentRepository.save(
                        new TeamAssignment(bestTeamMatch, taskId)
                );
            }
        });
    }

    @Override
    public List<TeamAssignment> getAllTeamAssignment() {
        return teamAssignmentRepository.findAll();
    }
}

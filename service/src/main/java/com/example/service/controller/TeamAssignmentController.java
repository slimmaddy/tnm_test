package com.example.service.controller;

import com.example.service.entity.TeamAssignment;
import com.example.service.service.TeamAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/team-assignment")
public class TeamAssignmentController {

    @Autowired
    TeamAssignmentService restService;

    @GetMapping()
    private ResponseEntity<List<TeamAssignment>> getAll() {
        try {
            return new ResponseEntity<>(restService.getAllTeamAssignment(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

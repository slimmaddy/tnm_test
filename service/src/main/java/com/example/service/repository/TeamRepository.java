package com.example.service.repository;

import com.example.service.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface TeamRepository extends JpaRepository<Team, String> {
    @Query(value = "select t.team_id from team t left join team_assignment ta on t.team_id = ta.team_id " +
            "where t.team_id in :teamIdList " +
            "group by t.team_id " +
            "order by count(ta.task_id) asc " +
            "limit 1", nativeQuery = true
    )
    String getAssignedTaskCount(@Param("teamIdList") Collection<String> teamIdList);
}

package com.hciot.dao;

import com.hciot.entity.Process;
import com.hciot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface ProcessRepository extends JpaRepository<Process,Long> , JpaSpecificationExecutor<Process> {

    @Query(nativeQuery = true,value = "SELECT pe.* FROM t_process pe, t_project pj WHERE pj.id = pe.project_id and pe.status != \"未开始\" and pe.status != \"已完成\"")
    List<Process> findProcessesByStatusAndProjectId();

    @Query(nativeQuery = true,value = "SELECT pe.* FROM t_process pe WHERE pe.project_id = ?1  and end_time = (SELECT MAX(end_time) end_time FROM t_process pe WHERE pe.project_id = ?1);")
    Process findMaxEndTime(Long projectId);

    @Query(nativeQuery = true,value = "SELECT pe.* FROM t_process pe WHERE pe.project_id = ?1 and pe.status != \"未开始\" and pe.status != \"已完成\" and  pe.end_time = ?2;")
    Process findProcessByMinEndTime(Long projectId,Date minEndTime);

    @Query(nativeQuery = true, value = "SELECT pe.* FROM t_process pe WHERE pe.project_id = ?1 and pe.status != \"未开始\" and pe.status != \"已完成\" and pe.end_time = " +
            "(SELECT MIN(end_time) end_time FROM t_process pe WHERE pe.project_id = ?1 and pe.status != \"未开始\" and pe.status != \"已完成\")")
    Process findCurrentProcess(Long projectId);

    @Query(nativeQuery = true, value = "SELECT pe.* FROM t_process pe WHERE pe.project_id = ?1 \n" +
            "and end_time = (SELECT MAX(end_time) end_time FROM t_process pe WHERE pe.project_id = ?1);")
    Process findEndProcess(Long projectId);

    @Query(nativeQuery = true, value = "select * from t_process p where TO_DAYS(p.end_time) = TO_DAYS(NOW());")
    List<Process> findDayProcess(User user);
}

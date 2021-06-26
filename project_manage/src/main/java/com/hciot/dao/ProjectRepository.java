package com.hciot.dao;

import com.hciot.entity.Project;
import com.hciot.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project,Long>, JpaSpecificationExecutor<Project>{

    @Query(nativeQuery = true,value = "select * from t_project")
    Page<Project> findAllProjects(Pageable pageable);

    @Query(nativeQuery = true,value = "select * from t_project p where p.user_id = ?1")
    Page<Project> findProjectsByUser(User user, Pageable pageable);

    @Query(nativeQuery = true,value = "select * from t_project pj where pj.status= ?1")
    Page<Project> findProjectsByStatus(String status, Pageable pageable);

    @Query(nativeQuery = true,value = "select * from t_project pj where pj.status= ?2 and pj.id =any \n" +
            "(select project_id from t_process p where p.user_id = ?1 GROUP BY project_id)\n" +
            "or (pj.user_id = ?1 and pj.status = ?2)")
    Page<Project> findProjectsByUserAndStatus(User user,String status, Pageable pageable);

    @Query(nativeQuery = true,value = "select * from t_project pj where pj.status= '未开始'")
    Page<Project> findProjectsByNotStatus(Pageable pageable);

    @Query(nativeQuery = true,value = "select * from t_project pj where pj.status= '未开始' and pj.user_id = ?1")
    Page<Project> findProjectsByUserAndStatus(User user,Pageable pageable);

    @Query(nativeQuery = true,value = "select * from t_project pj where pj.id =any\n" +
            "(select project_id from t_process p where TO_DAYS(p.end_time) = TO_DAYS(NOW()) \n" +
            "and p.status != \"未开始\" and p.status != \"已完成\")")
    Page<Project> findDayProjects(Pageable pageable);

    @Query(nativeQuery = true,value = "select * from t_project pj where pj.id =any\n" +
            "(select project_id from t_process p where TO_DAYS(p.end_time) = TO_DAYS(NOW()) \n" +
            "and p.user_id = 1 and p.status != \"未开始\" and p.status != \"已完成\")")
    Page<Project> findDayProjects(User user, Pageable pageable);

    @Query(nativeQuery = true,value = "select * from t_project pj where pj.id =any \n" +
            "(SELECT project_id FROM t_process p  WHERE YEARWEEK(date_format(p.end_time,'%Y-%m-%d')) = YEARWEEK(now()) " +
            "and p.status != \"未开始\" and p.status != \"已完成\")")
    Page<Project> findWeekProjects(Pageable pageable);

    @Query(nativeQuery = true,value = "select * from t_project pj where pj.id =any \n" +
            "(SELECT project_id FROM t_process p  WHERE YEARWEEK(date_format(p.end_time,'%Y-%m-%d')) = YEARWEEK(now()) " +
            "and p.user_id = ?1 and p.status != \"未开始\" and p.status != \"已完成\")")
    Page<Project> findWeekProjects(User user, Pageable pageable);

}

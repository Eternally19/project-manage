package com.hciot.service;

import com.hciot.entity.Process;
import com.hciot.entity.Project;
import com.hciot.entity.User;
import com.hciot.vo.ProjectVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectService {

    Project getProject(Long id);

    Page<Project> listProject(Pageable pageable);

    Page<Project> listProject(Pageable pageable, ProjectVO project);

    Page<Project> listProject(User user, Pageable pageable);

    Page<Project> listProject(String status, Pageable pageable);

    Page<Project> listProject(User user, String status, Pageable pageable);

    Page<Project> listNoStartProject(Pageable pageable);

    Page<Project> listNoStartProject(User user, Pageable pageable);

    Page<Project> listDayProject(Pageable pageable);

    Page<Project> listDayProject(User user,Pageable pageable);

    Page<Project> listWeekProject(Pageable pageable);

    Page<Project> listWeekProject(User user,Pageable pageable);

    Project saveProject(Project project);

    Project updateProject(Long id,Project project);

    void deleteProject(Long id);

}

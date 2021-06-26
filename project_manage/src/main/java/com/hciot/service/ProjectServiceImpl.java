package com.hciot.service;

import com.hciot.NotFoundException;
import com.hciot.dao.ProcessRepository;
import com.hciot.dao.ProjectRepository;
import com.hciot.entity.Process;
import com.hciot.entity.Project;
import com.hciot.entity.Type;
import com.hciot.entity.User;
import com.hciot.util.DateUtil;
import com.hciot.util.MyBeanUtils;
import com.hciot.vo.ProjectVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.*;

import static com.hciot.util.DateFormat.*;


@Service
public class ProjectServiceImpl implements ProjectService{

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProcessRepository processRepository;

    @Override
    public Project getProject(Long id) {
        return projectRepository.getOne(id);
    }

    @Override
    public Page<Project> listProject(Pageable pageable) {
        return projectRepository.findAllProjects(pageable);
    }

    @Override
    public Page<Project> listProject(Pageable pageable, ProjectVO project) {
        return projectRepository.findAll(new Specification<Project>() {
            @Override
            public Predicate toPredicate(Root<Project> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (!"".equals(project.getTitle()) && project.getTitle() != null) {
                    predicate.getExpressions().add(cb.like(root.get("title"),"%"+project.getTitle()+"%"));
                }
                if (project.getYear() != null) {
                    String yearBeginTime = getYearBeginTime(project.getYear());
                    String yearEndTime = getYearEndTime(project.getYear());
                    predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("endTime").as(String.class),yearBeginTime));
                    predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("endTime").as(String.class),yearEndTime));
                }
                if (project.getYear() != null && project.getMonth() != null) {
                    String monthBeginTime = getMonthBeginTime(project.getYear(),project.getMonth());
                    String monthEndTime = getMonthEndTime(project.getYear(),project.getMonth());
                    predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("endTime").as(String.class),monthBeginTime));
                    predicate.getExpressions().add(cb.lessThanOrEqualTo(root.get("endTime").as(String.class),monthEndTime));
                }
                if (project.getTypeId() != null) {
                    predicate.getExpressions().add(cb.equal(root.<Type>get("type").get("id"),project.getTypeId()));
                }
                if (!"".equals(project.getStatus()) && project.getStatus() != null) {
                    predicate.getExpressions().add(cb.equal(root.get("status").as(String.class),project.getStatus()));
                }
                return predicate;
            }
        },pageable);
    }

    @Override
    public Page<Project> listProject(User user, Pageable pageable) {
        return projectRepository.findProjectsByUser(user,pageable);
    }

    @Override
    public Page<Project> listProject(String status, Pageable pageable) {
        return projectRepository.findProjectsByStatus(status, pageable);
    }

    @Override
    public Page<Project> listProject(User user, String status, Pageable pageable) {
        return projectRepository.findProjectsByUserAndStatus(user, status, pageable);
    }

    @Override
    public Page<Project> listNoStartProject(Pageable pageable) {
        return projectRepository.findProjectsByNotStatus(pageable);
    }

    @Override
    public Page<Project> listNoStartProject(User user, Pageable pageable) {
        return projectRepository.findProjectsByUserAndStatus(user,pageable);
    }

    @Override
    public Page<Project> listDayProject(Pageable pageable) {
        return projectRepository.findDayProjects(pageable);
    }

    @Override
    public Page<Project> listDayProject(User user, Pageable pageable) {
        return projectRepository.findDayProjects(user, pageable);
    }

    @Override
    public Page<Project> listWeekProject(Pageable pageable) {
        return projectRepository.findWeekProjects(pageable);
    }

    @Override
    public Page<Project> listWeekProject(User user, Pageable pageable) {
        return projectRepository.findWeekProjects(user, pageable);
    }

    @Transactional
    @Override
    public Project saveProject(Project project) {
        if (project.getId() == null) {
            project.setCreateTime(new Date());
        }
//        Calendar cal = DateUtil.increaseHour(project.getSelectTime());
//        project.setEndTime(cal.getTime());
        return projectRepository.save(project);
    }

    @Transactional
    @Override
    public Project updateProject(Long id, Project project) {
        Project p = projectRepository.getOne(id);
        if (p == null){
            throw new NotFoundException("该项目不存在");
        }
//        Calendar cal = DateUtil.increaseHour(project.getSelectTime());
//        project.setEndTime(cal.getTime());
        BeanUtils.copyProperties(project, p, MyBeanUtils.getNullPropertyNames(project));
        return projectRepository.save(p);
    }

    @Transactional
    @Override
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
}

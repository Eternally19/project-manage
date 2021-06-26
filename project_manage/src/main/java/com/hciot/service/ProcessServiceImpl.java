package com.hciot.service;

import com.hciot.NotFoundException;
import com.hciot.dao.ProcessRepository;
import com.hciot.dao.ProjectRepository;
import com.hciot.entity.Process;
import com.hciot.entity.Project;
import com.hciot.entity.User;
import com.hciot.util.DateUtil;
import com.hciot.util.MyBeanUtils;
import com.hciot.util.PageUtil;
import com.hciot.vo.ProcessVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProcessServiceImpl implements ProcessService{

    @Autowired
    private ProcessRepository processRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @PersistenceContext
    protected EntityManager em;

    @Transactional
    @Override
    public Process getProcess(Long id) {
        return processRepository.getOne(id);
    }

    @Transactional
    @Override
    public Page<Process> listProcess(Pageable pageable, Long projectId) {

        return processRepository.findAll(new Specification<Process>() {
            @Override
            public Predicate toPredicate(Root<Process> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Join join = root.join("project");
                return cb.equal(join.get("id"),projectId);
            }
        },pageable);
    }

    @Override
    public Page<Process> listProcess(Pageable pageable, User user) {
        List<Process> ProcessList = processRepository.findDayProcess(user);
        Page<Process> page = PageUtil.createPageFromList(ProcessList, pageable);
        return page;
    }

    @Override
    public Map<Long, Process> currentProcess() {
        List<Process> processes = processRepository.findProcessesByStatusAndProjectId();
        Map<Long, Process> map = new HashMap<>();
        for (Process p: processes) {
            map.put(p.getProject().getId(),p);
        }
        return map;
    }

    @Transactional
    @Override
    public Process saveProcess(Process process) {
        if (process.getId() == null) {
            process.setCreateTime(new Date());
            if (process.getProject() != null) {
                Project project = process.getProject();
                if (project.getProcessList().isEmpty()) {
                   process.setStatus("已开始");
//                   DateUtil.saveProcessEndTime(process);
                } else {
                    if (processRepository.findCurrentProcess(project.getId()) != null){
                        process.setStatus("未开始");
                    }else {
                        process.setStatus("已开始");
                    }
                    Process endProcess = processRepository.findEndProcess(project.getId());
//                    DateUtil.editHourBySelectTime(endProcess.getEndTime(),process);
                }
            }
        }

        return processRepository.save(process);
    }

    @Transactional
    @Override
    public Process updateProcess(Long id, Process process) {
        Process p = processRepository.getOne(id);
        if (p == null){
            throw new NotFoundException("该流程不存在");
        }
        BeanUtils.copyProperties(process, p, MyBeanUtils.getNullPropertyNames(process));
        return processRepository.save(p);
    }

    @Transactional
    @Override
    public Process getCurrentProcess(Long projectId) {
        return processRepository.findCurrentProcess(projectId);
    }

    @Override
    public Process getEndProcess(Long projectId) {
        return processRepository.findEndProcess(projectId);
    }


    @Transactional
    @Override
    public void deleteProcess(Long id) {
        processRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void batchUpdate(List<Process> processList) {
        for (int i = 0; i < processList.size(); i++) {
            em.merge(processList.get(i));
            if (i % 30 == 0) {
                em.flush();
                em.clear();
            }

        }
    }
}

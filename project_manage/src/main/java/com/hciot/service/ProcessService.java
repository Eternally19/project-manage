package com.hciot.service;

import com.hciot.entity.Process;
import com.hciot.entity.User;
import com.hciot.vo.ProcessVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ProcessService {

    Process getProcess(Long id);

    Page<Process> listProcess(Pageable pageable,Long projectId);

    Page<Process> listProcess(Pageable pageable,User user);

    Map<Long,Process> currentProcess();

    Process saveProcess(Process process);

    Process updateProcess(Long id,Process process);

    Process getCurrentProcess(Long projectId);

    Process getEndProcess(Long projectId);

    void deleteProcess(Long id);

    void batchUpdate(List<Process> processList);



}

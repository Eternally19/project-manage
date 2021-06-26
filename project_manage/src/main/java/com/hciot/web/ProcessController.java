package com.hciot.web;

import com.alibaba.fastjson.JSONObject;
import com.hciot.entity.Process;
import com.hciot.entity.Project;
import com.hciot.entity.User;
import com.hciot.service.EmailService;
import com.hciot.service.ProcessService;

import com.hciot.service.ProjectService;
import com.hciot.service.UserService;
import com.hciot.util.DateUtil;
import com.hciot.vo.ProcessVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class ProcessController {

    @Autowired
    private ProcessService processService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private EmailService emailService;

//    @PostMapping("/OneProcess")
//    @ResponseBody
//    public Process process( @RequestParam Map<String,Object> info) {
//
//        Long id = Long.parseLong(info.get("processId").toString());
//
//        System.out.println("前端传递过来的名字是"+id);
//        Process process = processService.getProcess(id);
//        System.out.println(process.getContext());
//
//        return process;
//    }

//    @PostMapping("/OneProcess")
//    @ResponseBody
//    public Process process( @RequestParam("processId") String id ) {
//
//        Long pid = Long.parseLong(id);
//
//        System.out.println("前端传递过来的名字是"+pid);
//        Process process = processService.getProcess(pid);
//        System.out.println(process.getContext());
//
//        return process;
//    }


//    @GetMapping("/process/input")
//    public String input(){
//        return "process-input";
//    }


    @PostMapping("/{sortId}/process")
    public String post(Process process, RedirectAttributes attributes,@PathVariable Long sortId,
                       @PageableDefault(size = 8, sort = {"endTime"}, direction = Sort.Direction.ASC) Pageable pageable){
        Long projectId = process.getProject().getId();
        Project project = projectService.getProject(projectId);
        List<Process> processList = project.getProcessList();
        process.setProject(project);
        Process p;
        if (process.getId() == null){
            process.setUser(userService.getUser(process.getUser().getId()));
            p = processService.saveProcess(process);
            if(!project.getStatus().equals("进行中")) {
                project.setStatus("进行中");
                projectService.updateProject(projectId,project);
            }
            emailService.saveProcessEmail(p);
        }else {
            if (process.getStatus()!=null && !"".equals(process.getStatus())) {
                if (process.getStatus().equals("12h") || process.getStatus().equals("24h") || process.getStatus().equals("36h")|| process.getStatus().equals("0h")
                        || process.getStatus().equals("-24h") || process.getStatus().equals("-36h")|| process.getStatus().equals("-12h")) {
                    Process p1 = processService.getProcess(process.getId());
//                    List<Process > processList1 = new ArrayList<>();
                    for (int i = 0; i < processList.size(); i++) {
                        if (processList.get(i).getEndTime().after(p1.getEndTime())) {
                            processList.get(i).setStatus(process.getStatus());
                            DateUtil.editHourByStatus(processList.get(i));
                            processList.get(i).setStatus("未开始");
//                            processList1.add(processList.get(i));
                            processService.updateProcess(processList.get(i).getId(),processList.get(i));
                            emailService.redoProcessEmail(p1,processList.get(i));
                        }
                    }
                    process.setEndTime(p1.getEndTime());
                    DateUtil.editHourByStatus(process);
                    process.setStatus("重做");
                    p = processService.updateProcess(process.getId(),process);
                    emailService.redoProcessEmail(p);
//                    processList1.add(p1);
//                    processService.batchUpdate(processList1);
                } else if (process.getStatus().equals("已完成")) {
                    Page<Process> processes = processService.listProcess(pageable, projectId);
                    List<Process> processes1 = processes.toList();
                    for (int i = 0; i < processes1.size(); i++) {
                        if (i+1 != processes1.size()) {
                            if (processes1.get(i).getId().equals(process.getId())) {
                                processes1.get(i+1).setStatus("已开始");
                                processService.updateProcess(processes1.get(i+1).getId(),processes1.get(i+1));
                                emailService.email(processes1.get(i+1));
                                break;
                            }
                        }
                    }
                    p = processService.updateProcess(process.getId(),process);
                } else  p = processService.updateProcess(process.getId(),process);
            }else  {
                Process p2 = processService.getProcess(process.getId());
                User user = p2.getUser();
                String context = p2.getContext();
                Date endTime = p2.getEndTime();
//                Page<Process> processes = processService.listProcess(pageable, projectId);
//                DateUtil.editHourBySelectTime(processes,process);    根据前一个的时间递增 12h 24h 36h
                process.setUser(userService.getUser(process.getUser().getId()));
                p = processService.updateProcess(process.getId(),process);
                if (p.getUser() != user) {
                    emailService.updateProcessEmail(p,user,user.getEmail(),context,endTime);
                    emailService.updateProcessEmail(p,user,p.getUser().getEmail(),context,endTime);
                }else emailService.updateProcessEmail(p,user,p.getUser().getEmail(),context,endTime);
            }
        }
        if(processService.getCurrentProcess(projectId)!=null) {
            Process currentProcess = processService.getCurrentProcess(projectId);
            project.setProgress(currentProcess.getContext());
            project.setStatus("进行中");
            projectService.updateProject(projectId,project);
        }else {
            project.setProgress("项目已完成");
            project.setStatus("已完成");
            projectService.updateProject(projectId,project);
        }
        if (processService.getEndProcess(projectId).getEndTime() != project.getEndTime()) {
            project.setEndTime(processService.getEndProcess(projectId).getEndTime());
            projectService.updateProject(projectId, project);
        }
        if (p == null){
            attributes.addFlashAttribute("message","操作失败");
        }else {
            attributes.addFlashAttribute("message","操作成功");
        }
        System.out.println(process.getId());
        System.out.println(sortId);
        return "redirect:/admin/projects/" + projectId +"/"+ sortId+"/look";
    }




    @GetMapping("/process/{id}/input")
    public String editInput(@PageableDefault(size = 8, sort = {"createTime"}, direction = Sort.Direction.ASC) Pageable pageable,
                            @PathVariable Long id, Model model){
        Process process = processService.getProcess(id);
        model.addAttribute("page",processService.listProcess(pageable,process.getId()));
        model.addAttribute("process",process);
        model.addAttribute("users",userService.listUser());
        model.addAttribute("project",process.getProject());
        return "process";
    }

    @GetMapping("/process/{id}/{sortId}/delete")
    public String delete(@PathVariable Long id,@PathVariable Long sortId,RedirectAttributes attributes){
        Process process = processService.getProcess(id);
        Long projectId = process.getProject().getId();
        processService.deleteProcess(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/projects/"+projectId+"/"+sortId+"/look";
    }


}

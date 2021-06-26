package com.hciot.web;

import com.hciot.entity.Process;
import com.hciot.entity.Project;
import com.hciot.entity.User;
import com.hciot.service.ProcessService;
import com.hciot.service.ProjectService;
import com.hciot.service.TypeService;
import com.hciot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;


@Controller
@RequestMapping("/admin")
public class ProjectController {

    private static final String INPUT = "projects-input";
    private static final String LIST = "index";
    private static final String REDIRECT_LIST = "redirect:/admin/projects";

    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProcessService processService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private UserService userService;

    @GetMapping("/projects")
    public String projects(@PageableDefault(size = 8,sort = {"create_Time"},direction = Sort.Direction.DESC)
                                       Pageable pageable, HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        model.addAttribute("user",user);
        Page<Project> projects = null;
        if (user.getType() == 1) {
            projects = projectService.listProject(pageable);
        }else projects = projectService.listProject(user,pageable);
        model.addAttribute("page", projects);
        return LIST;
    }

    @GetMapping("/projects/option")
    public String selectProjects(@PageableDefault(size = 8,sort = {"create_Time"},direction = Sort.Direction.DESC) Pageable pageable,
                                 @RequestParam(value = "status", defaultValue = "进行中") String status,
                                 HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        model.addAttribute("user",user);
        Page<Project> projects;
        if(!status.equals("未开始")) {
            if (user.getType() == 1) {
                projects = projectService.listProject(status,pageable);
            }else projects = projectService.listProject(user,status,pageable);
        }
        else {
            if (user.getType() == 1) {
                projects = projectService.listNoStartProject(pageable);
            }else projects = projectService.listNoStartProject(user,pageable);

        }
        model.addAttribute("page", projects);
        return LIST;
    }

    @GetMapping("/projects/day")
    public String dayProjects(@PageableDefault(size = 8,sort = {"create_Time"},direction = Sort.Direction.DESC) Pageable pageable,
                                 HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        model.addAttribute("user",user);
        Page<Project> projects = null;
        if (user.getType() == 1) {
            projects = projectService.listDayProject(pageable);
        }else projects = projectService.listDayProject(user,pageable);
        model.addAttribute("page", projects);
        return LIST;
    }

    @GetMapping("/projects/week")
    public String weekProjects(@PageableDefault(size = 8,sort = {"create_Time"},direction = Sort.Direction.DESC) Pageable pageable,
                                 HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        model.addAttribute("user",user);
        Page<Project> projects = null;
        if (user.getType() == 1) {
            projects = projectService.listWeekProject(pageable);
        }else projects = projectService.listWeekProject(user,pageable);
        model.addAttribute("page", projects);
        return LIST;
    }

    @GetMapping("/projects/input")
    public String input(Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("project",new Project());
        return INPUT;
    }

    @GetMapping("/projects/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("project",projectService.getProject(id));
        return INPUT;
    }

    @PostMapping("/projects")
    public String post(Project project, RedirectAttributes attributes, HttpSession session) throws UnsupportedEncodingException {
        project.setUser((User) session.getAttribute("user"));
        Project p;
        if (project.getId() == null){
            project.setStatus("未开始");
            p = projectService.saveProject(project);
        }else {
            p = projectService.updateProject(project.getId(),project);
        }
        if (p == null){
            attributes.addFlashAttribute("message","操作失败");
        }else {
            attributes.addFlashAttribute("message","操作成功");
        }
        return "redirect:/admin/projects/option?status="+ URLEncoder.encode(p.getStatus(),"UTF-8");
    }


    @GetMapping("/projects/{id}/{sortId}/look")
    public String processByPid(@PageableDefault(size = 100, sort = {"endTime"}, direction = Sort.Direction.ASC) Pageable pageable,
                               @PathVariable Long id,@PathVariable Long sortId, Model model){
        Page<Process> processes = processService.listProcess(pageable, id);
        model.addAttribute("page",processes);
        model.addAttribute("users",userService.listUser());
        model.addAttribute("project",projectService.getProject(id));
        model.addAttribute("process",new Process());
        model.addAttribute("sortId",sortId);
        return "process";
    }

    @GetMapping("/projects/{id}/{sortId}/editProcess/{processId}")
    public String editProcessByPid(@PageableDefault(size = 100, sort = {"endTime"}, direction = Sort.Direction.ASC) Pageable pageable,
                                   @PathVariable Long id,@PathVariable Long sortId, Model model,@PathVariable Long processId ){
        Page<Process> processes = processService.listProcess(pageable, id);

        model.addAttribute("page",processes);
        model.addAttribute("users",userService.listUser());
        model.addAttribute("project",projectService.getProject(id));
        model.addAttribute("linkId",processId);
        Process process = processService.getProcess(processId);
        model.addAttribute("process",process);
        return "process";
    }

    @GetMapping("/projects/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes) throws UnsupportedEncodingException {
        Project p = projectService.getProject(id);
        projectService.deleteProject(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/projects/option?status="+ URLEncoder.encode(p.getStatus(),"UTF-8");
    }
}

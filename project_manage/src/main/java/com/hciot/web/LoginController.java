package com.hciot.web;

import com.hciot.entity.Process;
import com.hciot.entity.Project;
import com.hciot.entity.User;
import com.hciot.service.ProcessService;
import com.hciot.service.ProjectService;
import com.hciot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProcessService processService;

    @GetMapping
    public String loginPage(){
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        @PageableDefault(size = 8,sort = {"create_Time"},direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(value = "status", defaultValue = "进行中") String status,
                        HttpSession session, Model model){
        User user = userService.checkUser(username,password);
        if (user != null){
            user.setPassword(null);
            session.setAttribute("user",user);
            Page<Project> projects = null;
            if (user.getType()==1) {
                projects = projectService.listProject(status,pageable);
            }else projects = projectService.listProject(user,status,pageable);
            model.addAttribute("page",projects);
            return "index";
        } else {
            return "redirect:/admin";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/admin";
    }
}

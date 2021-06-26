package com.hciot.web;

import com.hciot.entity.Project;
import com.hciot.entity.User;
import com.hciot.service.ProcessService;
import com.hciot.service.ProjectService;
import com.hciot.service.TypeService;
import com.hciot.service.UserService;
import com.hciot.vo.ProjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class StatisticsController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private TypeService typeService;

    @GetMapping("/statistics")
    public String statistics(@PageableDefault(size = 8,sort = {"id"},direction = Sort.Direction.ASC)
                                   Pageable pageable, HttpSession session, Model model){
        model.addAttribute("page",projectService.listProject(pageable));
        User user = (User) session.getAttribute("user");
        model.addAttribute("user",user);
        model.addAttribute("types",typeService.listType());
        return "statistics";
    }

    @PostMapping("/statistics/search")
    public String search(@PageableDefault(size = 8,sort = {"id"},direction = Sort.Direction.ASC)
                                   Pageable pageable, ProjectVO project, Model model){
        System.out.println(project.getStatus()+111);
        model.addAttribute("page",projectService.listProject(pageable,project));
        return "statistics :: statisticList";
    }
}

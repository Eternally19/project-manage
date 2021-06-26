package com.hciot.web;

import com.hciot.entity.User;
import com.hciot.service.UserService;
import com.hciot.util.MD5Utills;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


@Controller
@RequestMapping("/admin")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String users(@PageableDefault(size = 8,sort = {"id"},direction = Sort.Direction.ASC)
                                    Pageable pageable, Model model){
        model.addAttribute("page",userService.listUser(pageable));
        return "users";
    }

    @GetMapping("/users/input")
    public String input(Model model){
        model.addAttribute("user",new User());
        return "users-input";
    }

    @PostMapping("/users")
    public String post(@Valid User user, BindingResult result, RedirectAttributes attributes){
        User user1 = userService.getUserByUsername(user.getUsername());
        if (user1 != null){
            result.rejectValue("username","usernameError","已有该用户");
        }
        if (result.hasErrors()){
            return "users-input";
        }
        User u = userService.saveUser(user);
        if (u == null){
            attributes.addFlashAttribute("message","新增失败");
        }else {
            attributes.addFlashAttribute("message","新增成功");
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/users/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("user",userService.getUser(id));
        return "users-input";
    }

    @PostMapping("/users/{id}")
    public String editpost(@Valid User user, BindingResult result,@PathVariable Long id, RedirectAttributes attributes){
        User user1 = userService.getUserByUsername(user.getUsername());
        if (user1 != null && !user1.getId().equals(id)){
            result.rejectValue("username","usernameError","已有该用户");
        }
        if (result.hasErrors()){
            return "users-input";
        }
        User u = userService.updateUser(id,user);
        if (u == null){
            attributes.addFlashAttribute("message","更新失败");
        }else {
            attributes.addFlashAttribute("message","更新成功");
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/users/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
        userService.deleteUser(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/users";
    }
}

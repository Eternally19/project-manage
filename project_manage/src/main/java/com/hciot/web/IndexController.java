package com.hciot.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(){
//        int i = 9/0;
        return "index";
    }

    @GetMapping("/process")
    public String process(){
//        int i = 9/0;
        return "process";
    }
}

package com.kekeek.travel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalTime;

@Controller
@RequestMapping("/")
public class MyController {

    @RequestMapping
    public String handleRequest(Model model) {
        model.addAttribute("msg", "A message from the controller - ışık");
        model.addAttribute("time", LocalTime.now());
        return "test-dir/my-page";
    }
}

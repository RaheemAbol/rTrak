package org.abol.retentiontrak.controller;


import org.abol.retentiontrak.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/upload")
    public String uploadPage() {
        return "upload";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
        try {
            employeeService.save(file);
            model.addAttribute("message", "File uploaded successfully!");
        } catch (Exception e) {
            model.addAttribute("message", "File upload failed! " + e.getMessage());
            e.printStackTrace();
        }
        return "upload";
    }

    @GetMapping("/hub")
    public String hubPage() {
        return "hub";
    }
}
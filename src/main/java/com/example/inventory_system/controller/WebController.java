package com.example.inventory_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    // This tells Spring: "When someone visits the main URL (/), show them the HTML page."
    @GetMapping("/")
    public String showDashboard() {
        // This MUST match the exact name of your HTML file (without the .html extension)
        return "index"; 
    }
    
}
package com.jcluna.auth_api.controller;


import com.jcluna.auth_api.dto.QuoteResponse;
import com.jcluna.auth_api.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/dashboard")
public class DashboardController {


    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/")
    public QuoteResponse getDashboard() {
        return dashboardService.getRandomQuote();
    }



}

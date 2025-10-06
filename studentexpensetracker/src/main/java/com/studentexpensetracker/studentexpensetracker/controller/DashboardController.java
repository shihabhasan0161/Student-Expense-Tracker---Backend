package com.studentexpensetracker.studentexpensetracker.controller;

import com.studentexpensetracker.studentexpensetracker.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getDashBoardData() {
        Map<String, Object> dashboardData = dashboardService.getDashBoardData();
        return ResponseEntity.ok(dashboardData);
    }
}

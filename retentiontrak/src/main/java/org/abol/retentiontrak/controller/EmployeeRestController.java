package org.abol.retentiontrak.controller;

import org.abol.retentiontrak.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class EmployeeRestController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/api/retention-data")
    public Map<String, Double> getRetentionData(@RequestParam int years) {
        return employeeService.getRetentionRates(years);
    }


    @GetMapping("/api/position-allocation")
    public Map<String, Double> getPositionAllocation(@RequestParam String position) {
        return employeeService.getPositionAllocation(position);
    }
}
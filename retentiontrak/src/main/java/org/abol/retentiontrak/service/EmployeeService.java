package org.abol.retentiontrak.service;

import org.abol.retentiontrak.model.Employee;
import org.abol.retentiontrak.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public void save(MultipartFile file) {
        try {
            List<Employee> employees = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String line;
            boolean isFirstLine = true; // Flag to skip header
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] data = line.split(",");
                if (data.length >= 6) { // Ensure there are enough columns
                    Employee employee = new Employee();
                    employee.setName(data[1]);
                    employee.setPosition(data[2]);
                    employee.setManager(data[3]);
                    employee.setHireDate(LocalDate.parse(data[4]));
                    employee.setExitDate(data[5].isEmpty() ? null : LocalDate.parse(data[5]));
                    employees.add(employee);
                }
            }
            employeeRepository.saveAll(employees);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Double> getRetentionRates(int years) {
        LocalDate currentDate = LocalDate.now();
        List<Employee> employees = employeeRepository.findAll();
        Map<String, List<Employee>> groupedByManager = employees.stream()
                .collect(Collectors.groupingBy(Employee::getManager));

        return groupedByManager.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> {
                    List<Employee> managerEmployees = entry.getValue();
                    long totalEmployees = managerEmployees.size();
                    long retainedEmployees = managerEmployees.stream()
                            .filter(emp -> {
                                LocalDate hireDate = emp.getHireDate();
                                LocalDate exitDate = emp.getExitDate();
                                LocalDate cutoffDate = hireDate.plusYears(years);
                                return (hireDate.isBefore(currentDate.minusYears(years)) || hireDate.isEqual(currentDate.minusYears(years)))
                                        && (exitDate == null || exitDate.isAfter(cutoffDate));
                            })
                            .count();
                    return (double) retainedEmployees / totalEmployees * 100; // Return percentage
                }));
    }
}

package ma.dnaengineering.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EmployeeService {
    public List<Employee> readEmployeesFromCSV(MultipartFile file) {
        List<Employee> employees = new ArrayList<>();
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<Employee> csvToBean = new CsvToBeanBuilder<Employee>(reader)
                    .withType(Employee.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            employees = csvToBean.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public Map<String, Double> getAverageSalaryByJobTitle(List<Employee> employees) {
        Map<String, Double> averageSalaryByJobTitle = new HashMap<>();
        Map<String, List<Employee>> employeesByJobTitle = employees.stream()
                .collect(Collectors.groupingBy(Employee::getJobTitle));
        employeesByJobTitle.forEach((jobTitle, jobTitleEmployees) -> {
            Double averageSalary = jobTitleEmployees.stream()
                    .mapToDouble(Employee::getSalary)
                    .average()
                    .orElse(0.0);
            averageSalaryByJobTitle.put(jobTitle, averageSalary);
        });
        return averageSalaryByJobTitle;
    }
}


package com.csye6220.controller;

import java.util.List;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication; // Import Spring Security Authentication
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.csye6220.dao.EmployeeDAO;
import com.csye6220.model.Employee;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class EmployeeController {
    
    @Autowired
    private EmployeeDAO employeeDAO;
    
    @GetMapping("/addEmployee")
    public String newEmployee(Model model) {
        Employee employee=new Employee();
        model.addAttribute("employee", employee);
        return "addEmployee";
        
    }
    @PostMapping("/saveEmployee")
    public String saveEmployee(@Valid @ModelAttribute("employee") Employee employee, BindingResult br, RedirectAttributes redirectAttributes) {
        if (br.hasErrors()) {
            return employee.getId() != null ? "updateEmployee" : "addEmployee";
        }

        try {
            Employee existingEmployee = employeeDAO.findByEmail(employee.getEmail());
            if (existingEmployee != null && !existingEmployee.getId().equals(employee.getId())) {
                redirectAttributes.addFlashAttribute("error", "An employee with this email already exists.");
                return "redirect:/addEmployee";
            }
            employeeDAO.save(employee);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error during saving: " + e.getMessage());
            return "redirect:/addEmployee";
        }
        
        return "redirect:/index";
    }

    @GetMapping("/updateEmployee/{id}")
    public String updateEmployee(@PathVariable (value="id")Long id,Model model) {
        Employee employee=employeeDAO.getEmployeeById(id);
        model.addAttribute("employee", employee);
        return "updateEmployee";
    }
    
    @GetMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable (value="id")Long id, Model model) {
        employeeDAO.deleteEmployee(id);
        return "redirect:/index";
    }
    @GetMapping("/index")
    public String displayEmployee(Model model,
            @RequestParam(defaultValue ="0")int page,
            @RequestParam(defaultValue="4") int size,
            @RequestParam(defaultValue="lastName") String sortField,
            @RequestParam(defaultValue="asc") String sortOrder,
            Authentication authentication) { // Change HttpSession to Authentication
        if(authentication != null && authentication.isAuthenticated()) { // Check if user is authenticated
            Map<String,Object> list=employeeDAO.getAllEmployees(page,size,sortField,sortOrder);
            model.addAttribute("employeeList",list.get("employeeList"));
            model.addAttribute("totalP",list.get("totalP"));
            model.addAttribute("currentPage",page);
            model.addAttribute("reverseSort",sortOrder.equals("asc")? "desc" : "asc");
            model.addAttribute("sortField",sortField);
            model.addAttribute("sortOrder",sortOrder);
            model.addAttribute("totalC",list.get("totalC"));
            return "index";
        }
        else {
            return "redirect:/login";
        }
        
    }
    @GetMapping("/searchEmployee")
    public String searchEmployee(
        @RequestParam("searchTerm") String searchTerm,
        @RequestParam("searchCriteria") String searchCriteria,
        Model model) {
        List<Employee> employees =employeeDAO.searchByCriteria(searchTerm, searchCriteria);
        model.addAttribute("employees", employees);
        model.addAttribute("searchTerm", searchTerm);
        return "searchEmployee";
        
        
    }

}


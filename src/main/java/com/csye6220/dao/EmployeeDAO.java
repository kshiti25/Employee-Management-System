package com.csye6220.dao;

import java.util.List;
import java.util.Map;

import com.csye6220.model.Employee;

public interface EmployeeDAO {
	void save(Employee employee);
	Employee findByEmail(String email);
	List<Employee> getAllEmployees();
	Employee getEmployeeById(Long id);
	void deleteEmployee(Long id);
	Map<String,Object> getAllEmployees(int page,int size,String sortBy,String sortOrder);
	List<Employee> searchByCriteria(String searchTerm,String searchBy);
	
	
	

}

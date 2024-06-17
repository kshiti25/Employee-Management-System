package com.csye6220.test;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.query.Query;

import com.csye6220.model.User;
import com.csye6220.model.Employee;
import com.csye6220.util.HibernateUtil;

public class CredentialsMain {
	
	public static void main(String[] args) {
		User user=new User();
		user.setUsername("employee25");
		user.setPassword("admin");
		
		Session s=HibernateUtil.getSessionFactory().openSession();
		   Query<User> query = s.createQuery("FROM User WHERE username = :username", User.class);
	        query.setParameter("username", user.getUsername());
	        User details=(User) query.uniqueResult();
	        if(details != null && details.getPassword().equals(user.getPassword())) {
	        	String hql="From Employee";
	        	 Query<Employee> det = s.createQuery(hql, Employee.class);
	        	    List<Employee> list = det.getResultList();
	        	for(Employee emp :list) {
	        		System.out.println(emp);
	        	}
	        }
	        else {
	        	System.out.println("Unsuccessful login");
	        }
	
	        
	        
	        
		
	}

}

package com.csye6220.dao;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.csye6220.model.Employee;
import com.csye6220.util.HibernateUtil;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Component
public class EmployeeDAOImpln implements EmployeeDAO {
	

	@Override
	public void save(Employee employee) {
		// TODO Auto-generated method stub
		Session s = HibernateUtil.getSessionFactory().openSession();
		Transaction trans=s.beginTransaction();
		s.merge(employee);
		trans.commit();
	}

    @Override
    public List<Employee> getAllEmployees() {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query<Employee> query = s.createQuery("FROM Employee", Employee.class);
        List<Employee> employees = query.getResultList();
        s.close();
        return employees;
    }
	@Override
	public Employee getEmployeeById(Long id) {
		// TODO Auto-generated method stub
		Session s=HibernateUtil.getSessionFactory().openSession();
		Query<Employee> query = s.createQuery("FROM Employee WHERE id = :id", Employee.class);
		query.setParameter("id", id);
		Employee employee=(Employee) query.uniqueResult();
		  s.close();
		return employee;		
		
	}

	@Override
	public void deleteEmployee(Long id) {
		// TODO Auto-generated method stub
		Session s=HibernateUtil.getSessionFactory().openSession();
		Query<Employee> query = s.createQuery("FROM Employee WHERE id = :id", Employee.class);
		query.setParameter("id", id);
		Transaction trans=s.beginTransaction();
		Employee employee=(Employee) query.uniqueResult();
		s.remove(employee);
		trans.commit();
		
	}
	public Employee findByEmail(String email) {
	    Session session = HibernateUtil.getSessionFactory().openSession();
	    Transaction tx = null;
	    try {
	        tx = session.beginTransaction();
	        Query<Employee> query = session.createQuery("FROM Employee WHERE email = :email", Employee.class);
	        query.setParameter("email", email);
	        Employee employee = query.uniqueResult();
	        tx.commit();
	        return employee;
	    } catch (Exception e) {
	        if (tx != null) tx.rollback();
	        e.printStackTrace();
	        return null;
	    } finally {
	        session.close();
	    }
	}


	@Override
	public Map<String, Object> getAllEmployees(int page, int size, String sortBy, String sortOrder) {
		// TODO Auto-generated method stub
		Session s=HibernateUtil.getSessionFactory().openSession();
		CriteriaBuilder criteriaBuldr=s.getCriteriaBuilder();
		CriteriaQuery<Employee> criteriaQ=criteriaBuldr.createQuery(Employee.class);
		 Root<Employee> root = criteriaQ.from(Employee.class);
		 
		 criteriaQ.orderBy(sortOrder.equals("asc")?criteriaBuldr.asc(root.get(sortBy)):criteriaBuldr.desc(root.get(sortBy)));
		 Query<Employee> query=s.createQuery(criteriaQ);
		 
		 CriteriaQuery<Long> countQ=criteriaBuldr.createQuery(Long.class);
		 countQ.select(criteriaBuldr.count(countQ.from(Employee.class)));
		 Long totalC=s.createQuery(countQ).getSingleResult();
		 
		 int totalP=(int) Math.ceil((double) totalC/size);
		 
		  if (page < 0) {
	            page = 0;
	        } else if (page >= totalP && totalP > 0) {
	            page = totalP - 1;
	        }
		 query.setFirstResult(page*size);
		 query.setMaxResults(size);
		 Map<String,Object> employeeMap=new HashMap<>();
		 employeeMap.put("employeeList", query.getResultList());
		 employeeMap.put("totalP",totalP);
		 employeeMap.put("totalC",totalC);
		 
		return employeeMap;
	}

	@Override
	public List<Employee> searchByCriteria(String searchTerm, String searchBy) {
		// TODO Auto-generated method stub
		Session s=HibernateUtil.getSessionFactory().openSession();
		CriteriaBuilder criteriaBuldr=s.getCriteriaBuilder();
		CriteriaQuery<Employee> criteriaQ=criteriaBuldr.createQuery(Employee.class);
		Root <Employee> root =criteriaQ.from(Employee.class);
		
		Predicate search=null;
		
		if(searchBy.equalsIgnoreCase("firstName")) {
			search = criteriaBuldr.like(criteriaBuldr.lower(root.get("firstName")), "%" + searchTerm.toLowerCase() + "%");

		}
		else if(searchBy.equalsIgnoreCase("lastName")) {
			search = criteriaBuldr.like(criteriaBuldr.lower(root.get("lastName")), "%" + searchTerm.toLowerCase() + "%");

		}
		else if(searchBy.equalsIgnoreCase("email")) {
			search = criteriaBuldr.like(criteriaBuldr.lower(root.get("email")), "%" + searchTerm.toLowerCase() + "%");

		}
		if(search!=null) {
			criteriaQ.where(search);
		}
		Query<Employee> query=s.createQuery(criteriaQ);
		
		return query.getResultList();
}
}


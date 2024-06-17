package com.csye6220.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.csye6220.model.User;

import com.csye6220.util.HibernateUtil;




@Repository
public class UserDAOImpln implements UserDAO{

	@Override
	public void save(User user) {
		Session s=HibernateUtil.getSessionFactory().openSession();
		Transaction trans= s.beginTransaction();
		s.persist(user);
		trans.commit();

		
	}

	@Override
	public List<User> getAllUsers() {
		Session s=HibernateUtil.getSessionFactory().openSession();
		 Query<User> query = s.createQuery("FROM User", User.class);
	        return query.getResultList();
	}

	@Override
	public User getUserByUsername(String username) {
	    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
	        Query<User> query = session.createQuery("FROM User WHERE username = :username", User.class);
	        query.setParameter("username", username);
	        return query.uniqueResult();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;  
	    }
	}


}

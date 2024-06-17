package com.csye6220.dao;
import java.util.List;
import com.csye6220.model.User;
public interface UserDAO {
	void save(User user);
	List<User>getAllUsers();
	User getUserByUsername(String username);

}

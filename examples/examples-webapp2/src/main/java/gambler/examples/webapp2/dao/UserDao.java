package gambler.examples.webapp2.dao;

import gambler.examples.webapp2.domain.User;

import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {

	User findByUserId(String userId);
	
	int saveUser(User user);
}

package gambler.examples.webapp2.dao;

import gambler.commons.auth.User;

import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {

	public User findUserById(String userId);
}

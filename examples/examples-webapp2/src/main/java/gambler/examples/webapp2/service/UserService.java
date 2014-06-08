package gambler.examples.webapp2.service;

import gambler.examples.webapp2.dao.UserDao;
import gambler.examples.webapp2.domain.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	@Transactional
	public User save(User user) {
		userDao.saveUser(user);
		System.out.println(user.getId());
		if (user.getUserId().equals("hello")) {
			throw new IllegalArgumentException("xxx");
		}
		return user;
	}

	public User findUserById(String userId) {
		return userDao.findByUserId(userId);
	}
}

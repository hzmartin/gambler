package gambler.examples.webapp2.service;

import gambler.examples.webapp2.dao.AuthUserDao;
import gambler.examples.webapp2.domain.AuthUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("authUserService")
public class AuthUserService {

	@Autowired
	private AuthUserDao userDao;

	@Transactional
	public AuthUser save(AuthUser user) {
		userDao.saveUser(user);
		System.out.println(user.getId());
		if (user.getUserId().equals("hello")) {
			throw new IllegalArgumentException("xxx");
		}
		return user;
	}

	public AuthUser findUserById(String userId) {
		return userDao.findByUserId(userId);
	}
}

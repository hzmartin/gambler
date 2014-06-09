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
	public int save(AuthUser user) {
		return userDao.saveUser(user);
	}

	public AuthUser findUserById(String userId) {
		return userDao.findByUserId(userId);
	}
}

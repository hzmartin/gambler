package gambler.examples.webapp.auth.service;

import gambler.commons.auth.User;
import gambler.commons.persistence.PersistenceException;
import gambler.examples.webapp.auth.UserAlreadyExistException;
import gambler.examples.webapp.auth.UserNotFoundException;
import gambler.examples.webapp.auth.dao.UserDao;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserService {

	@Resource(name = "jasyptStringDigester")
	private org.jasypt.digest.StandardStringDigester jasyptStringDigester;

	@Resource
	private UserDao userDao;

	@Transactional
	public User save(User account) throws UserAlreadyExistException,
			PersistenceException {
		if (account == null || StringUtils.isBlank(account.getUserId())) {
			throw new IllegalArgumentException("user id required!");
		}

		if (StringUtils.isBlank(account.getPassword())) {
			throw new IllegalArgumentException("password required!");
		}

		User existAccount = userDao.findByUserId(account.getUserId());
		if (existAccount != null) {
			throw new UserAlreadyExistException("user account("
					+ account.getUserId() + ") already exists!");
		}
		account.setPassword(jasyptStringDigester.digest(account.getPassword()));
		userDao.save(account);
		return account;
	}

	public boolean validate(User account) throws PersistenceException,
			UserNotFoundException {
		if (account == null || StringUtils.isBlank(account.getUserId())) {
			return false;
		}
		User existAccount = userDao.findByUserId(account.getUserId());
		if (existAccount == null) {
			throw new UserNotFoundException("user account("
					+ account.getUserId() + ") not found!");
		}

		if (StringUtils.isBlank(account.getPassword())) {
			return false;
		}

		return jasyptStringDigester.matches(account.getPassword(),
				existAccount.getPassword());

	}

}

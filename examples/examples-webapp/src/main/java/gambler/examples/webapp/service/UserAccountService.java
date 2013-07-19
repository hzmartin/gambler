package gambler.examples.webapp.service;

import gambler.commons.persistence.PersistenceException;
import gambler.commons.persistence.hibernate.IHibernatePersistence;
import gambler.examples.webapp.domain.UserAccount;
import gambler.examples.webapp.exception.UserAccountAlreadyExistException;
import gambler.examples.webapp.exception.UserAccountNotFoundException;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserAccountService {

	@Resource(name = "simpleHibernatePersistence")
	private IHibernatePersistence persister;

	@Resource(name = "jasyptStringDigester")
	private org.jasypt.digest.StandardStringDigester jasyptStringDigester;

	@Transactional
	public UserAccount save(UserAccount account)
			throws UserAccountAlreadyExistException, PersistenceException {
		if (account == null || StringUtils.isBlank(account.getName())) {
			throw new IllegalArgumentException("name required!");
		}

		if (StringUtils.isBlank(account.getPassword())) {
			throw new IllegalArgumentException("password required!");
		}

		UserAccount existAccount = findByName(account.getName());
		if (existAccount != null) {
			throw new UserAccountAlreadyExistException("user account("
					+ account.getName() + ") already exists!");
		}
		account.setPassword(jasyptStringDigester.digest(account.getPassword()));
		persister.save(account);
		return account;
	}

	public boolean validate(UserAccount account) throws PersistenceException,
			UserAccountNotFoundException {
		if (account == null || StringUtils.isBlank(account.getName())) {
			return false;
		}
		UserAccount existAccount = findByName(account.getName());
		if (existAccount == null) {
			throw new UserAccountNotFoundException("user account("
					+ account.getName() + ") not found!");
		}

		if (StringUtils.isBlank(account.getPassword())) {
			return false;
		}

		return jasyptStringDigester.matches(account.getPassword(),
				existAccount.getPassword());

	}

	public UserAccount findByName(String name) throws PersistenceException {
		if (StringUtils.isBlank(name)) {
			throw new IllegalArgumentException("name required!");
		}
		Object account = persister.unique("from " + UserAccount.class.getName()
				+ " where name = ?", name);
		return (UserAccount) account;
	}
}

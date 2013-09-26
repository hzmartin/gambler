package gambler.examples.webapp.auth.dao;

import gambler.commons.auth.User;
import gambler.commons.persistence.PersistenceException;
import gambler.commons.persistence.hibernate.IHibernatePersistence;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

	@Resource(name = "simpleHibernatePersistence")
	private IHibernatePersistence persister;

	public User findByUserId(String userId) throws PersistenceException {
		Object account = persister.unique("from " + User.class.getName()
				+ " where userId = ?", userId);
		return (User) account;
	}

	public void save(User account) throws PersistenceException {
		persister.save(account);
	}
}

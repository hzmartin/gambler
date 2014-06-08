package gambler.examples.webapp2.dao;

import gambler.examples.webapp2.domain.AuthUser;

import org.springframework.stereotype.Repository;

@Repository
public interface AuthUserDao {

	AuthUser findByUserId(String userId);
	
	int saveUser(AuthUser user);
}

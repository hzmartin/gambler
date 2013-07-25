package gambler.commons.auth;

import gambler.commons.auth.AuthenticationService;
import junit.framework.TestCase;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Authentication Service unit test
 * 
 * @auther Martin
 */

public class AuthenticationServiceTest extends TestCase {

	private static ClassPathXmlApplicationContext ctx = null;

	static {
		ctx = new ClassPathXmlApplicationContext("auth-test.xml");
	}

	private AuthenticationService authService = null;

	@Override
	protected void setUp() throws Exception {
		authService = (AuthenticationService) ctx.getBean("auth-service");
	}

	public void testLoadPermission() {
		assertNotNull(authService);
	}

}

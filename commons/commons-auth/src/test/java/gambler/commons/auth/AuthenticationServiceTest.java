package gambler.commons.auth;

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

	public void testCheckUserPermission() throws Exception {
		User superUser = new User();
		superUser.setUserId("super");
		superUser.setSuperUser(true);
		superUser.setActive(true);
		authService.getPersistence().save(superUser);
		assertTrue(authService.checkUserPermission(superUser.getUserId(),
				"can_publish", "news"));
		authService.getPersistence().delete(superUser);
	}
	
	public void testCheckUserPermission2() throws Exception {
		User helloUser = new User();
		helloUser.setUserId("hello");
		helloUser.setActive(true);
		Permission p1 = new Permission();
		p1.setCodename("can_publish");
		p1.setContent("news");
		authService.getPersistence().save(helloUser);
		authService.getPersistence().save(p1);
		assertFalse(authService.checkUserPermission(helloUser.getUserId(),
				"can_publish", "news"));
		UserPermission uperm = new UserPermission();
		uperm.setUserId(helloUser.getId());
		uperm.setPermId(p1.getId());
		authService.getPersistence().save(uperm);
		assertTrue(authService.checkUserPermission(helloUser.getUserId(),
				"can_publish", "news"));
		authService.getPersistence().delete(helloUser);
		authService.getPersistence().delete(p1);
		authService.getPersistence().delete(uperm);
	}
	
	public void testCheckUserPermission3() throws Exception {
		User helloUser = new User();
		helloUser.setUserId("hello");
		helloUser.setActive(true);
		authService.getPersistence().save(helloUser);
		assertFalse(authService.checkUserPermission(helloUser.getUserId(),
				"can_publish", "news"));
		Role r1 = new Role();
		r1.setName("hello");
		authService.getPersistence().save(r1);
		UserRole urole = new UserRole();
		urole.setUserId(helloUser.getId());
		urole.setRoleId(r1.getId());
		authService.getPersistence().save(urole);
		assertFalse(authService.checkUserPermission(helloUser.getUserId(),
				"can_publish", "news"));
		Permission p1 = new Permission();
		p1.setCodename("can_publish");
		p1.setContent("news");
		authService.getPersistence().save(p1);
		RolePermission rperm = new RolePermission();
		rperm.setPermId(p1.getId());
		rperm.setRoleId(r1.getId());
		authService.getPersistence().save(rperm);
		assertTrue(authService.checkUserPermission(helloUser.getUserId(),
				"can_publish", "news"));
		authService.getPersistence().delete(helloUser);
		authService.getPersistence().delete(p1);
		authService.getPersistence().delete(r1);
		authService.getPersistence().delete(urole);
		authService.getPersistence().delete(rperm);
	}

}

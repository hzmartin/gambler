package gambler.commons.util.password;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PasswordUtilTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEncrypt() {
		String password = "root";
		String encrypt = PasswordUtil.encrypt(password);
		System.out.println("Encrypt Password for " + password + ": " + encrypt);
		assertTrue(PasswordUtil.checkPassword(encrypt, password));
	}

}

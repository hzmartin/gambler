package gambler.commons.util.jasypt;

import org.apache.commons.lang.StringUtils;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;

/**
 * Provide feature of setting default encryption password by invoking the
 * <code>setDefaultPassword</code> method when constructing the subclasses
 * 
 * @author Martin
 */
public class GBEnvironmentStringPBEConfig extends EnvironmentStringPBEConfig {

	private String defaultPassword = "gambler-martin";

	public GBEnvironmentStringPBEConfig() {
		setPassword(defaultPassword);
	}

	public void setDefaultPassword(String defaultPassword) {
		this.defaultPassword = defaultPassword;
	}

	/**
	 * Set the config object to use the specified environment variable to load
	 * the value for the password. default password will be used once the
	 * environment value doesn't exists.
	 *
	 * @param passwordEnvName
	 *            the name of the environment variable
	 */
	@Override
	public void setPasswordEnvName(String passwordEnvName) {
		String password = System.getenv(passwordEnvName);
		if (StringUtils.isNotEmpty(password)) {
			setPassword(password);
		}
	}

	/**
	 * Set the config object to use the specified JVM system property to load
	 * the value for the password. default password will be used once the
	 * property value doesn't exists.
	 *
	 * @param passwordSysPropertyName
	 *            the name of the property
	 */
	@Override
	public void setPasswordSysPropertyName(String passwordSysPropertyName) {
		String password = System.getProperty(passwordSysPropertyName);
		setPassword(password);
	}

	@Override
	public void setPassword(String password) {
		if (password == null) {
			password = defaultPassword;
		}
		super.setPassword(password);
	}

}

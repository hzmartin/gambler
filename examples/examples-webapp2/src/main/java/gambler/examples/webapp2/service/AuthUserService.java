package gambler.examples.webapp2.service;

import gambler.commons.advmap.XMLMap;
import gambler.examples.webapp2.dao.AuthUserDao;
import gambler.examples.webapp2.domain.AuthUser;
import gambler.examples.webapp2.vo.Account;

import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("authUserService")
public class AuthUserService {

	private static final Logger logger = Logger
			.getLogger(AuthUserService.class);

	private static final int DEFAULT_LOGIN_ACCESS_EXPIRY = 24 * 60 * 60;

	private static final String COOKIE_LOGIN = "GAMBLER_ID";

	private static final String COOKIE_ACCESS_TOKEN = "GAMBLER_ACCESS_TOKEN";

	private static final String SESSION_USER_KEY = "sgambler";

	@Autowired
	private AuthUserDao userDao;

	@Autowired
	private XMLMap sysconf;

	public boolean checkLogin(HttpServletRequest request) {
		return hasLogined(request) || cookieLogin(request) != null;
	}

	public Account login(final HttpServletRequest request,
			final HttpServletResponse response, String userId, String password,
			boolean remme) {
		Account loginUser = verifyLogin(request, userId, password);
		if (loginUser == null) {
			return null;
		}
		HttpSession session = request.getSession();
		session.setAttribute(SESSION_USER_KEY, loginUser);
		if (remme) {
			remmCookie(response, userId, password);
		}
		return loginUser;
	}

	public boolean hasLogined(HttpServletRequest request) {
		return null != getLoginUser(request);
	}

	private Account verifyLogin(final HttpServletRequest request,
			String userId, String password) {
		AuthUser user = userDao.findByUserId(userId);
		if (user == null) {
			return null;
		}
		if (!isCorrentPassword(password, user.getPassword(), userId)) {
			return null;
		}
		Account account = new Account();
		account.setUserId(userId);

		HttpSession session = request.getSession();
		session.setAttribute(SESSION_USER_KEY, account);
		return account;
	}

	private boolean isCorrentPassword(String rawPass, String dbPass,
			String userId) {
		if (rawPass == null || rawPass.isEmpty())
			return false;
		return getSaltedPassword(rawPass, userId).equals(dbPass);
	}

	public String getSaltedPassword(String rawPass, String userId) {
		return DigestUtils.md5Hex(rawPass + userId + "Gkx*&#F-j93+");
	}

	public Account getLoginUser(HttpServletRequest request) {
		return (Account) request.getSession().getAttribute(SESSION_USER_KEY);
	}

	public Account cookieLogin(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null || cookies.length == 0) {
			return null;
		}
		String userId = null, password = null;
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(COOKIE_LOGIN)) {
				userId = cookie.getValue();
			}
			if (cookie.getName().equals(COOKIE_ACCESS_TOKEN)) {
				password = cookie.getValue();
			}
		}
		if (userId == null || password == null) {
			return null;
		}
		try {
			userId = getStringEncryptor().decrypt(userId);
			password = getStringEncryptor().decrypt(password);
		} catch (Exception e) {
			logger.error("decrypt error!", e);
			return null;
		}
		return verifyLogin(request, userId, password);
	}

	private void remmCookie(HttpServletResponse response, String userId,
			String password) {
		String key = "gLoginCookieAccessExpiry";
		response.addCookie(createCookie(COOKIE_LOGIN, getStringEncryptor()
				.encrypt(userId), sysconf.getInteger(key,
				DEFAULT_LOGIN_ACCESS_EXPIRY)));
		response.addCookie(createCookie(COOKIE_ACCESS_TOKEN,
				getStringEncryptor().encrypt(password),
				sysconf.getInteger(key, DEFAULT_LOGIN_ACCESS_EXPIRY)));
	}

	private Cookie createCookie(String name, String value, int expiry) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(expiry);
		cookie.setPath("/");
		return cookie;
	}

	/**
	 * 销毁Cookie和Session
	 * 
	 * @param session
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("rawtypes")
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Enumeration attributeNames = request.getSession().getAttributeNames();
		while (attributeNames.hasMoreElements()) {
			Object attribute = attributeNames.nextElement();
			session.removeAttribute((String) attribute);
		}
		session.setMaxInactiveInterval(0);
		Cookie[] cookies = request.getCookies();
		if (cookies == null || cookies.length == 0)
			return;
		for (Cookie cookie : cookies) {
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
		response.addCookie(createCookie(COOKIE_LOGIN, "", 0));
		response.addCookie(createCookie(COOKIE_ACCESS_TOKEN, "", 0));
	}

	private StandardPBEStringEncryptor getStringEncryptor() {
		String ePassword = sysconf.getString("encryptPassword",
				"hzwangqh.*&^JLIjkd<w+lnLjGH!");
		String algorithm = sysconf.getString("passwordAlgorithm",
				"PBEWithMD5AndDES");
		StandardPBEStringEncryptor encryptor = new org.jasypt.encryption.pbe.StandardPBEStringEncryptor();
		EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
		config.setAlgorithm(algorithm);
		config.setPassword(ePassword);
		encryptor.setConfig(config);
		return encryptor;
	}

	@Transactional
	public int save(AuthUser user) {
		return userDao.saveUser(user);
	}

	public AuthUser findUserById(String userId) {
		return userDao.findByUserId(userId);
	}

	public boolean checkUserPermission(HttpServletRequest request,
			long[] requiredPerms) {
		throw new UnsupportedOperationException("unsupported!");
	}
}

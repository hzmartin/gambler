package gambler.examples.webapp2.service;

import gambler.examples.webapp2.constant.AuthConstants;
import gambler.examples.webapp2.dao.AuthDao;
import gambler.examples.webapp2.domain.auth.Permission;
import gambler.examples.webapp2.domain.auth.RolePermission;
import gambler.examples.webapp2.domain.auth.User;
import gambler.examples.webapp2.domain.auth.UserPermission;
import gambler.examples.webapp2.domain.auth.UserRole;
import gambler.examples.webapp2.vo.Account;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("authUserService")
public class AuthUserService extends AbstractService {

	private static final Logger logger = Logger
			.getLogger(AuthUserService.class);

	private static final int DEFAULT_LOGIN_ACCESS_EXPIRY = 24 * 60 * 60;

	private static final String COOKIE_LOGIN = "GAMBLER_ID";

	private static final String COOKIE_ACCESS_TOKEN = "GAMBLER_ACCESS_TOKEN";

	private static final String SESSION_USER_KEY = "sgambler";

	@Autowired
	private AuthDao userDao;

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
		User user = userDao.find(userId);
		if (user == null) {
			return null;
		}
		if (!isCorrentPassword(password, user.getPassword(), userId)) {
			return null;
		}
		user.setLastLogin(new Timestamp(System.currentTimeMillis()));
		userDao.update(user);

		Account account = new Account(user);
		HttpSession session = request.getSession();
		session.setAttribute(SESSION_USER_KEY, account);
		return account;
	}

	public boolean isCorrentPassword(String rawPass, String dbPass,
			String userId) {
		if (rawPass == null || rawPass.isEmpty())
			return false;
		return getSaltedPassword(rawPass, userId).equals(dbPass);
	}

	public String getSaltedPassword(String rawPass, String userId) {
		String md5Hex = DigestUtils.md5Hex(rawPass + userId + "Gkx*&#F-j93+");
		return md5Hex;
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
	public int saveAsSystemUser(User user) {
		int count = userDao.save(user);
		if (count == 1) {
			User dbUser = userDao.find(user.getUserId());
			UserRole userRole = new UserRole();
			userRole.setUid(dbUser.getUid());
			userRole.setRid(AuthConstants.SYSTEM_USER.getRid());
			userDao.createUserRole(userRole);
		}
		return count;
	}

	public User findUserById(String userId) {
		return userDao.find(userId);
	}

	public int updateUser(User user) {
		return userDao.update(user);
	}

	@Transactional
	public int deleteUser(String userId) {
		User user = userDao.find(userId);
		userDao.delUserPermissions(user.getUid());
		userDao.delUserRoles(user.getUid());
		return userDao.delete(userId);
	}

	public List<UserPermission> getUserPermissions(String userId) {
		User user = userDao.find(userId);
		return userDao.getUserPermissions(user.getUid());
	}

	public List<UserRole> getUserRoles(String userId) {
		User user = userDao.find(userId);
		return userDao.getUserRoles(user.getUid());
	}

	public int createUserPermission(long uid, long pid) {
		UserPermission up = new UserPermission();
		up.setUid(uid);
		up.setPid(pid);
		return userDao.createUserPermission(up);
	}

	public int delUserPermission(long uid, long pid) {
		UserPermission up = new UserPermission();
		up.setUid(uid);
		up.setPid(pid);
		return userDao.delUserPermission(up);
	}

	public boolean checkUserPermission(String userId, long... pids) {
		User user = findUserById(userId);
		if (user.getIsactive() == 0) {
			return false;
		}
		if (user.getIssuper() == 1) {
			return true;
		}

		List<Long> needCheckRolePermId = new ArrayList<Long>();
		for (long pid : pids) {
			UserPermission up = new UserPermission();
			up.setUid(user.getUid());
			up.setPid(pid);
			UserPermission userPermission = userDao.getUserPermission(up);
			if (userPermission == null) {
				needCheckRolePermId.add(pid);
			}
		}

		// userperm check fail, need to check role perm
		if (CollectionUtils.isNotEmpty(needCheckRolePermId)) {
			List<UserRole> userRoles = userDao.getUserRoles(user.getUid());
			for (long pid : needCheckRolePermId) {
				boolean authorized = false;
				for (UserRole userRole : userRoles) {
					List<Permission> rolePermission = AuthConstants
							.getRolePermissions(userRole.getRid());
					for (Permission permission : rolePermission) {
						if (pid == permission.getPid()) {
							authorized = true;
						}
					}
				}
				if (!authorized) {
					// no permission
					return false;
				}
			}
		}

		return true;
	}

	public List<Permission> getAllPermissions() {
		return userDao.getAllPermissions();
	}

	public List<RolePermission> getAllRolePermissions() {
		return userDao.getAllRolePermissions();
	}

	public int updatePassword(User user) {
		return userDao.updatePassword(user);
	}

	public int updateUserActiveFlag(User user) {
		return userDao.updateUserActiveFlag(user);
	}

	public int updateUserSuperFlag(User user) {
		return userDao.updateUserSuperFlag(user);
	}

}

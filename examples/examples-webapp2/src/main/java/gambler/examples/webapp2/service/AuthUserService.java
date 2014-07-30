package gambler.examples.webapp2.service;

import gambler.examples.webapp2.annotation.LogMethod;
import gambler.examples.webapp2.constant.AuthConstants;
import gambler.examples.webapp2.dao.AuthDao;
import gambler.examples.webapp2.domain.auth.Permission;
import gambler.examples.webapp2.domain.auth.RolePermission;
import gambler.examples.webapp2.domain.auth.User;
import gambler.examples.webapp2.domain.auth.UserPermission;
import gambler.examples.webapp2.domain.auth.UserRole;
import gambler.examples.webapp2.dto.AccountDto;
import gambler.examples.webapp2.exception.ActionException;
import gambler.examples.webapp2.resp.ResponseStatus;

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
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("authUserService")
public class AuthUserService extends AbstractService {

	private static final int DEFAULT_LOGIN_ACCESS_EXPIRY = 24 * 60 * 60;

	private static final String COOKIE_LOGIN = "GAMBLER_ID";

	private static final String COOKIE_ACCESS_TOKEN = "GAMBLER_ACCESS_TOKEN";

	private static final String SESSION_USER_KEY = "sgambler";

	@Autowired
	private AuthDao authDao;

	/**
	 * check session and cookie
	 * 
	 * @return true if logined
	 */
	public boolean checkLogin(HttpServletRequest request) {
		return null != getLoginUser(request) || cookieLogin(request) != null;
	}

	/**
	 * login, set session attribute and cookie if remme is true
	 * 
	 * @return login account
	 */
	public AccountDto login(final HttpServletRequest request,
			final HttpServletResponse response, String userId, String password,
			boolean remme) {
		AccountDto loginUser = verifyLogin(request, userId, password);
		if (loginUser == null) {
			return null;
		}
		if (remme) {
			remmCookie(response, userId, password);
		}
		return loginUser;
	}

	private AccountDto verifyLogin(final HttpServletRequest request,
			String userId, String password) {
		User user = authDao.find(userId);
		if (user == null) {
			return null;
		}
		if (!isCorrentPassword(password, user.getPassword(), userId)) {
			return null;
		}
		user.setLastLogin(new Timestamp(System.currentTimeMillis()));
		authDao.update(user);

		AccountDto account = new AccountDto(user);
		HttpSession session = request.getSession();
		session.setAttribute(SESSION_USER_KEY, account);
		return account;
	}

	public void switchUser(final HttpServletRequest request, String userId) throws ActionException {
		User user = authDao.find(userId);
		if (user == null) {
			throw new ActionException(ResponseStatus.USER_NOT_EXSIST);
		}

		AccountDto account = new AccountDto(user);
		HttpSession session = request.getSession();
		session.setAttribute(SESSION_USER_KEY, account);
	}

	/**
	 * check password is correct
	 * 
	 * @return true if equals
	 */
	public boolean isCorrentPassword(String rawPass, String dbPass,
			String userId) {
		if (rawPass == null || rawPass.isEmpty())
			return false;
		return getSaltedPassword(rawPass, userId).equals(dbPass);
	}

	/**
	 * 
	 * @return salted password which is stored in database
	 */
	public String getSaltedPassword(String rawPass, String userId) {
		String md5Hex = DigestUtils.md5Hex(rawPass + userId + "Gkx*&#F-j93+");
		return md5Hex;
	}

	public AccountDto getLoginUser(HttpServletRequest request) {
		return (AccountDto) request.getSession().getAttribute(SESSION_USER_KEY);
	}

	public AccountDto cookieLogin(HttpServletRequest request) {
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
		userId = getStringEncryptor().decrypt(userId);
		password = getStringEncryptor().decrypt(password);
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

	@LogMethod
	@Transactional
	public int saveAsSystemUser(User user) {
		int count = authDao.save(user);
		if (count == 1 && user.getIssuper() == 0) {
			User dbUser = authDao.find(user.getUserId());
			UserRole userRole = new UserRole();
			userRole.setUid(dbUser.getUid());
			userRole.setRid(AuthConstants.SYSTEM_USER_ROLE);
			authDao.createUserRole(userRole);
		}
		return count;
	}

	public User findUserById(String userId) {
		return authDao.find(userId);
	}

	public int updateUser(User user) {
		return authDao.update(user);
	}

	@Transactional
	public int deleteUser(String userId) {
		User user = authDao.find(userId);
		authDao.delUserPermissions(user.getUid());
		authDao.delUserRoles(user.getUid());
		return authDao.delete(userId);
	}

	public List<UserPermission> getUserPermissions(String userId) {
		User user = authDao.find(userId);
		return authDao.getUserPermissions(user.getUid());
	}

	public List<UserRole> getUserRoles(String userId) {
		User user = authDao.find(userId);
		return authDao.getUserRoles(user.getUid());
	}

	public int createUserPermission(long uid, long pid) {
		UserPermission up = new UserPermission();
		up.setUid(uid);
		up.setPid(pid);
		return authDao.createUserPermission(up);
	}

	public int delUserPermission(long uid, long pid) {
		UserPermission up = new UserPermission();
		up.setUid(uid);
		up.setPid(pid);
		return authDao.delUserPermission(up);
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
			UserPermission userPermission = authDao.getUserPermission(up);
			if (userPermission == null) {
				needCheckRolePermId.add(pid);
			}
		}

		// userperm check fail, need to check role perm
		if (CollectionUtils.isNotEmpty(needCheckRolePermId)) {
			List<UserRole> userRoles = authDao.getUserRoles(user.getUid());
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
		return authDao.getAllPermissions();
	}

	public List<RolePermission> getAllRolePermissions() {
		return authDao.getAllRolePermissions();
	}

	public int updatePassword(User user) {
		return authDao.updatePassword(user);
	}

	public int updateUserActiveFlag(User user) {
		return authDao.updateUserActiveFlag(user);
	}

	public int updateUserSuperFlag(User user) {
		return authDao.updateUserSuperFlag(user);
	}
	
	public int addPermission(Permission permission)
	{
		return authDao.addPermission(permission);
	}

	@Transactional
	public int delPermission(long pid)
	{
		authDao.delAllUserPermission(pid);
		authDao.delAllRolePermission(pid);
		return authDao.delPermission(pid);
	}

	public int updatePermission(Permission permission)
	{
		return authDao.updatePermission(permission);
	}
	
}

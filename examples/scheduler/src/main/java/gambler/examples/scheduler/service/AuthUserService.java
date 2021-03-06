package gambler.examples.scheduler.service;

import gambler.examples.scheduler.annotation.LogMethod;
import gambler.examples.scheduler.constant.AuthConstants;
import gambler.examples.scheduler.dao.AuthDao;
import gambler.examples.scheduler.domain.auth.Permission;
import gambler.examples.scheduler.domain.auth.Role;
import gambler.examples.scheduler.domain.auth.RolePermission;
import gambler.examples.scheduler.domain.auth.User;
import gambler.examples.scheduler.domain.auth.UserPermission;
import gambler.examples.scheduler.domain.auth.UserRole;
import gambler.examples.scheduler.dto.AccountDto;
import gambler.examples.scheduler.exception.ActionException;
import gambler.examples.scheduler.resp.ResponseStatus;

import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public void switchUser(final HttpServletRequest request, String userId)
			throws ActionException {
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

	public int createUserRole(long uid, long rid) {
		UserRole userRole = new UserRole();
		userRole.setUid(uid);
		userRole.setRid(rid);
		return authDao.createUserRole(userRole);
	}
	
	public int delUserRole(long uid, long rid) {
		UserRole userRole = new UserRole();
		userRole.setUid(uid);
		userRole.setRid(rid);
		return authDao.delUserRole(userRole);
	}

	@Transactional
	public int deleteUser(String userId) {
		User user = authDao.find(userId);
		authDao.delUserPermissions(user.getUid());
		authDao.delUserRoles(user.getUid());
		return authDao.delete(userId);
	}

	public List<UserPermission> getUserPermissions(long uid) {
		return authDao.getUserPermissions(uid);
	}

	public boolean checkUserRole(long uid, Long rid) {
		List<UserRole> userRoles = getUserRoles(uid, rid);
		return (userRoles != null && userRoles.size() == 1);
	}

	public List<UserRole> getUserRoles(long uid) {
		return getUserRoles(uid, null);
	}

	public List<UserRole> getUserRoles(long uid, Long rid) {
		UserRole ur = new UserRole();
		ur.setUid(uid);
		ur.setRid(rid);
		return authDao.getUserRole(ur);
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

	public boolean checkUserPermission(User user, long... pids) {
		if (user.getIsactive() == 0) {
			return false;
		}
		if (user.getIssuper() == 1) {
			return true;
		}

		for (long pid : pids) {
			UserPermission up = new UserPermission();
			up.setUid(user.getUid());
			up.setPid(pid);
			UserPermission userPermission = authDao.getUserPermission(up);
			if (userPermission != null) {
				continue;
			}

			List<UserPermission> userRolePermissions = authDao
					.getUserRolePermission(up);
			if (CollectionUtils.isNotEmpty(userRolePermissions)) {
				continue;
			}

			return false;
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

	public int addPermission(Permission permission) {
		return authDao.addPermission(permission);
	}

	@Transactional
	public int delPermission(long pid) {
		authDao.delAllUserPermission(pid);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pid", pid);
		authDao.delAllRolePermission(params);
		return authDao.delPermission(pid);
	}

	public int updatePermission(Permission permission) {
		return authDao.updatePermission(permission);
	}

	public List<Role> getAllRoles() {
		return authDao.getAllRoles();
	}

	public int addRole(Role role) {
		return authDao.addRole(role);
	}

	public int updateRole(Role role) {
		return authDao.updateRole(role);
	}

	public int delRole(long rid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("rid", rid);
		authDao.delAllRolePermission(params);
		return authDao.delRole(rid);
	}

	@Transactional
	public void createRolePermissions(long rid, long[] pids) {
		for (long pid : pids) {
			boolean authorized = AuthConstants.checkRolePermission(rid, pid);
			if (authorized) {
				logger.warn("role " + rid + " has already contains permission "
						+ pid);
				continue;
			}
			RolePermission rp = new RolePermission();
			rp.setRid(rid);
			rp.setPid(pid);
			authDao.createRolePermission(rp);
		}
	}

	public int delRolePermissions(long rid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("rid", rid);
		return authDao.delAllRolePermission(params);
	}

}

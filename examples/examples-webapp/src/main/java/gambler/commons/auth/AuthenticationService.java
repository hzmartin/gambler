package gambler.commons.auth;

import gambler.commons.persistence.IPersistence;
import gambler.commons.persistence.PersistenceException;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * AuthenticationManager
 * 
 * @auther Martin
 */

public class AuthenticationService {

	private Logger log = Logger.getLogger(AuthenticationService.class);

	private IPersistence persistence;

	public IPersistence getPersistence() {
		return persistence;
	}

	public void setPersistence(IPersistence persistence) {
		this.persistence = persistence;
	}

	/**
	 * get the permission object by the given parameters;
	 * 
	 * @param codename
	 * @param content
	 * @return permission object
	 * @throws AuthenticationException
	 */
	public Permission getPermission(String codename, String content)
			throws AuthenticationException {
		try {
			return (Permission) persistence.unique(
					"from Permission where codename = ? and content = ?",
					new String[] { codename, content });
		} catch (PersistenceException e) {
			log.warn(e);
			return null;
		}
	}

	/**
	 * check the permission by the given user
	 * 
	 * @param userId
	 * @param codename
	 * @param content
	 * @return true for pass the permission check
	 * @throws AuthenticationException
	 */
	public boolean checkUserPermission(String userId, String codename,
			String content) throws AuthenticationException {
		Permission perm = getPermission(codename, content);
		return checkUserPermission(userId, perm);
	}

	/**
	 * check the permission by the given user
	 * 
	 * @param userId
	 * @param perm
	 * @return true for pass the permission check
	 * @throws AuthenticationException
	 */
	public boolean checkUserPermission(String userId, Permission perm)
			throws AuthenticationException {
		try {
			User user = (User) persistence.unique(
					"from User where userId = ? ", userId);
			if (user.isActive() == 0) {
				return false;
			}
			if (user.isSuperUser() != 0) {
				return true;
			}
			List perms = persistence.find(
					"from UserPermission where userId = ? ", user.getId());
			for (Object object : perms) {
				UserPermission uPerm = (UserPermission) object;
				if (uPerm.getPermId().equals(perm.getId())) {
					return true;
				}
			}
			List roles = persistence.find("from UserRole where userId = ? ",
					user.getId());
			for (Object object : roles) {
				UserRole uRole = (UserRole) object;
				boolean hasPerm = checkRolePermission(uRole.getRoleId(), perm);
				if (hasPerm) {
					return true;
				}
			}
		} catch (PersistenceException e) {
			log.error(e);
		}
		return false;
	}

	/**
	 * check the permission by the given role
	 * 
	 * @param roleId
	 * @param codename
	 * @param content
	 * @return true for pass the permission check
	 */
	public boolean checkRolePermission(long roleId, String codename,
			String content) throws AuthenticationException {
		Permission perm = getPermission(codename, content);
		return checkRolePermission(roleId, perm);
	}

	/**
	 * check the permission by the given role
	 * 
	 * @param roleId
	 * @param perm
	 * @return true for pass the permission check
	 */
	public boolean checkRolePermission(long roleId, Permission perm)
			throws AuthenticationException {
		try {
			List perms = persistence.find(
					"from RolePermission where roleId = ? ", roleId);
			for (Object object : perms) {
				RolePermission rPerm = (RolePermission) object;
				if (rPerm.getPermId().equals(perm.getId())) {
					return true;
				}
			}
		} catch (PersistenceException e) {
			log.error(e);
		}
		return false;
	}

}

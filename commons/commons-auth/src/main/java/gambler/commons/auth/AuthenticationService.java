package gambler.commons.auth;

import gambler.commons.persistence.IPersistence;

/**
 * AuthenticationManager
 * 
 * @auther Martin
 */

public class AuthenticationService {

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
		// TODO:
		return null;
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
		// TODO:
		// check for the super user and administrator role
		// check for anyone
		// check for specific user or anonymous
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
	public boolean checkRolePermission(int roleId, String codename,
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
	public boolean checkRolePermission(int roleId, Permission perm)
			throws AuthenticationException {
		// TODO:
		return false;
	}

}

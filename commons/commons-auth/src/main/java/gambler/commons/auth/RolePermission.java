package gambler.commons.auth;

import java.io.Serializable;

/**
 * the relationship between role and permission
 * 
 * @auther Martin
 */

public class RolePermission implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2603872394551840349L;

	private Long id;

	private Long roleId;

	private Long permId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getPermId() {
		return permId;
	}

	public void setPermId(Long permId) {
		this.permId = permId;
	}

}

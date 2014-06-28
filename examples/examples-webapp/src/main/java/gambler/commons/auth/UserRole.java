package gambler.commons.auth;

import java.io.Serializable;

/**
 * the relationship between user and role
 *
 * @auther Martin
 */

public class UserRole implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1716908709595241248L;

	private Long id;
	
	private Long userId;
	
	private Long roleId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
}

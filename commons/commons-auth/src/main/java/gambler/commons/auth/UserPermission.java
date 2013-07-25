package gambler.commons.auth;

import java.io.Serializable;

/**
 * the relationship between user and permission
 *
 * @auther Martin
 */

public class UserPermission implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2763947191459836226L;

	private Long id;
	
	private Long userId;
	
	private Long permId;
	
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

	public Long getPermId() {
		return permId;
	}

	public void setPermId(Long permId) {
		this.permId = permId;
	}
}

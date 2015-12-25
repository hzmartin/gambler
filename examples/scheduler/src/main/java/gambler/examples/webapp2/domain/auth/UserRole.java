package gambler.examples.webapp2.domain.auth;

import java.io.Serializable;

/**
 * the relationship between user and role
 * 
 * @auther Martin
 */

public class UserRole implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1716908709595241248L;

	private Long id;

	private Long uid;

	private Long rid;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getRid() {
		return rid;
	}

	public void setRid(Long rid) {
		this.rid = rid;
	}

}

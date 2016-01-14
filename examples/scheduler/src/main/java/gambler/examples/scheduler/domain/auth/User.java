package gambler.examples.scheduler.domain.auth;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * User
 * 
 * @author Martin
 * @version Jul 24, 2013
 */
public class User implements Serializable {

	private static final long serialVersionUID = 2004187270602400843L;
	
	private Long uid;

	// unique
	private String userId;

	private String password;

	private String nick;

	private Timestamp dateJoined;//create time

	private Timestamp lastLogin;
	
	private Timestamp updateTime;

	private Integer isactive;
	
	private Integer issuper;

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public Timestamp getDateJoined() {
		return dateJoined;
	}

	public void setDateJoined(Timestamp dateJoined) {
		this.dateJoined = dateJoined;
	}

	public Timestamp getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Timestamp lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getIsactive() {
		return isactive;
	}

	public void setIsactive(Integer isactive) {
		this.isactive = isactive;
	}

	public Integer getIssuper() {
		return issuper;
	}

	public void setIssuper(Integer issuper) {
		this.issuper = issuper;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", nick=" + nick + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	
}

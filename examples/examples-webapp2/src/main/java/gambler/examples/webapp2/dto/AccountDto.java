package gambler.examples.webapp2.dto;

import gambler.examples.webapp2.domain.auth.User;

import java.sql.Timestamp;

public class AccountDto {

	private String userId;

	private String nick;

	private Timestamp lastLogin;

	private Integer issuper;

	private Integer isactive;

	private Timestamp dateJoined;

	public AccountDto(User user) {
		this.userId = user.getUserId();
		this.nick = user.getNick();
		this.lastLogin = user.getLastLogin();
		this.isactive = user.getIsactive();
		this.issuper = user.getIssuper();
		this.dateJoined = user.getDateJoined();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	@Override
	public String toString() {
		return "Account [userId=" + userId + ", nick=" + nick + "]";
	}

	public Timestamp getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Timestamp lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Integer getIssuper() {
		return issuper;
	}

	public void setIssuper(Integer issuper) {
		this.issuper = issuper;
	}

	public int getIsactive() {
		return isactive;
	}

	public void setIsactive(int isactive) {
		this.isactive = isactive;
	}

	public Timestamp getDateJoined() {
		return dateJoined;
	}

	public void setDateJoined(Timestamp dateJoined) {
		this.dateJoined = dateJoined;
	}

}

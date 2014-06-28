package gambler.examples.webapp2.vo;

import gambler.examples.webapp2.domain.auth.User;

import java.sql.Timestamp;

public class Account {

	private String userId;

	private String nick;

	private Timestamp lastLogin;

	private boolean issuper;

	private boolean isactive;

	private Timestamp dateJoined;

	public Account(User user) {
		this.userId = user.getUserId();
		this.nick = user.getNick();
		this.lastLogin = user.getLastLogin();
		this.isactive = user.isIsactive();
		this.issuper = user.isIssuper();
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

	public boolean isIssuper() {
		return issuper;
	}

	public void setIssuper(boolean issuper) {
		this.issuper = issuper;
	}

	public boolean isIsactive() {
		return isactive;
	}

	public void setIsactive(boolean isactive) {
		this.isactive = isactive;
	}

	public Timestamp getDateJoined() {
		return dateJoined;
	}

	public void setDateJoined(Timestamp dateJoined) {
		this.dateJoined = dateJoined;
	}

}

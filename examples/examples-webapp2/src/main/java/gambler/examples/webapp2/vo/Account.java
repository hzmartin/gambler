package gambler.examples.webapp2.vo;

import gambler.examples.webapp2.domain.auth.User;

import java.sql.Timestamp;

public class Account {

	private String userId;

	private String nick;

	private Timestamp lastLogin;
	
	public Account(User user) {
		this.userId = user.getUserId();
		this.nick = user.getNick();
		this.lastLogin = user.getLastLogin();
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

}

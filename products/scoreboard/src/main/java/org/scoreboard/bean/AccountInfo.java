package org.scoreboard.bean;

import java.sql.Timestamp;

/**
 * 
 * @author hzzhenglh
 * 
 *         2014年6月23日
 */
public class AccountInfo {
	private String openId;

	private String accountId;

	private Timestamp createTime;

	private String createIp;

	private int accountState;

	private Timestamp updateTime;

	private String accountName;

	private String accountAmount;

	private String accountIdentity;

	private String payPassword;

	private String bindMobilePhone;

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}

	public String getPayPassword() {
		return payPassword;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public String getCreateIp() {
		return createIp;
	}

	public int getAccountState() {
		return accountState;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public String getAccountName() {
		return accountName;
	}

	public String getAccountAmount() {
		return accountAmount;
	}

	public String getAccountIdentity() {
		return accountIdentity;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public void setCreateIp(String createIp) {
		this.createIp = createIp;
	}

	public void setAccountState(int accountState) {
		this.accountState = accountState;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public void setAccountAmount(String accountAmount) {
		this.accountAmount = accountAmount;
	}

	public void setAccountIdentity(String accountIdentity) {
		this.accountIdentity = accountIdentity;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getBindMobilePhone() {
		return bindMobilePhone;
	}

	public void setBindMobilePhone(String bindMobilePhone) {
		this.bindMobilePhone = bindMobilePhone;
	}

}

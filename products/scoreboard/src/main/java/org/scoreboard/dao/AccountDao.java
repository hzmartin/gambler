package org.scoreboard.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import org.scoreboard.bean.AccountInfo;

@Repository
public interface AccountDao {

	public AccountInfo getAccountInfo(Map<String, String> params);

}

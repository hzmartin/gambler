package org.scoreboard.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import org.scoreboard.annotation.DataSource;
import org.scoreboard.bean.AccountInfo;
import org.scoreboard.dao.AccountDao;
import org.scoreboard.exception.ActionException;

@Service
public class AccountService {

	@Resource
	private AccountDao accountDao;

	@DataSource(DataSource.DEFAULT_DATASOURCE)
	public AccountInfo getAccountInfo(String openId) throws ActionException {

		Map<String, String> params = new HashMap<String, String>();
		params.put("openId", openId);
		return accountDao.getAccountInfo(params);
	}

}

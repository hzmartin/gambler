package org.scoreboard.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import org.scoreboard.bean.YixinAccessToken;
import org.scoreboard.constants.IConstants;
import org.scoreboard.utils.HttpClientPool;
import org.scoreboard.utils.SpringContextHolder;

import com.alibaba.fastjson.JSON;
import com.chinatelecom.yiliao.common.framework.SysConfig;
import com.chinatelecom.yiliao.device.DeviceEnv;
import com.chinatelecom.yiliao.device.redis.RedisDevice;
import com.chinatelecom.yiliao.device.redis.RedisNodeType;

@Service
public class PublicAccountService {

	private static final String ENABLE_OAUTH_DEBUG = "EnableOauthDebug";

	private static Logger logger = Logger.getLogger(PublicAccountService.class);

	public String getOpenIdOfLoginUser(HttpServletRequest request) {
		return (String) request.getSession().getAttribute(
				IConstants.CUR_OPEN_ID_SESSION_KEY);
	}

	public final static String getAppId() {
		String yixinPaVar = SysConfig.getString("YIXIN_PA_APPID");
		if (SysConfig.getBoolean(ENABLE_OAUTH_DEBUG, false)) {
			yixinPaVar = SysConfig.getString("YIXIN_PA_DEBUG_APPID");
		}
		return yixinPaVar;
	}

	public final static String getAppSecret() {
		String yixinPaVar = SysConfig.getString("YIXIN_PA_APPSECRET");
		if (SysConfig.getBoolean(ENABLE_OAUTH_DEBUG, false)) {
			yixinPaVar = SysConfig.getString("YIXIN_PA_DEBUG_APPSECRET");
		}
		return yixinPaVar;
	}

	public final static String getOpenPlusHost() {
		String yixinPaVar = SysConfig.getString("YIXIN_PUBLIC_OPENPLUS_HOST");
		if (SysConfig.getBoolean(ENABLE_OAUTH_DEBUG, false)) {
			yixinPaVar = SysConfig
					.getString("YIXIN_PUBLIC_DEBUG_OPENPLUS_HOST");
		}
		return yixinPaVar;
	}

	public final static String getApiHost() {
		String yixinPaVar = SysConfig.getString("YIXIN_PUBLIC_API_HOST");
		if (SysConfig.getBoolean(ENABLE_OAUTH_DEBUG, false)) {
			yixinPaVar = SysConfig.getString("YIXIN_PUBLIC_DEBUG_API_HOST");
		}
		return yixinPaVar;
	}

	public final static String getAppIndexUrl() {
		String yixinPaVar = SysConfig.getString("YIXIN_PA_INDEX_URL");
		if (SysConfig.getBoolean(ENABLE_OAUTH_DEBUG, false)) {
			yixinPaVar = SysConfig.getString("YIXIN_PA_DEBUG_INDEX_URL");
		}
		return yixinPaVar;
	}

	public final void login(HttpServletResponse response) {

		try {
			String YIXIN_PA_APPID = getAppId();
			String YIXIN_PUBLIC_OAUTH_CODE_HOST = getOpenPlusHost();
			String YIXIN_PA_INDEX_URL = getAppIndexUrl();

			String yxOauthUrl = YIXIN_PUBLIC_OAUTH_CODE_HOST
					+ "/connect/oauth2/authorize?appid="
					+ YIXIN_PA_APPID
					+ "&redirect_uri="
					+ YIXIN_PA_INDEX_URL
					+ "&response_type=code&scope=snsapi_base&state=123#yixin_redirect";
			logger.debug("request oauth code: " + yxOauthUrl);
			response.sendRedirect(yxOauthUrl);
		} catch (Exception e) {
			logger.error("获取oauth code error! " + e.getMessage());
		}
	}

	public final YixinAccessToken getOauthAccessToken(String code) {
		try {
			String YIXIN_PA_APPID = getAppId();
			String YIXIN_PA_APPSECRET = getAppSecret();
			String YIXIN_PUBLIC_OAUTH_ACCESS_TOKEN_HOST = getApiHost();

			String yxOauthUrl = YIXIN_PUBLIC_OAUTH_ACCESS_TOKEN_HOST
					+ "/sns/oauth2/access_token?appid=" + YIXIN_PA_APPID
					+ "&secret=" + YIXIN_PA_APPSECRET + "&code=" + code
					+ "&grant_type=authorization_code";
			logger.debug("request access token: " + yxOauthUrl);
			String result = HttpClientPool.getInstance().getMethod(yxOauthUrl);
			logger.debug("request access token result: " + result);
			YixinAccessToken accessToken = JSON.parseObject(result,
					YixinAccessToken.class);
			return accessToken;
		} catch (Exception e) {
			logger.error("获取oauth access token error! " + e.getMessage());
		}
		return null;
	}

	public String getApiAccessToken() {
		try {
			DeviceEnv deviceEnv = SpringContextHolder.getBean("deviceEnv");
			RedisDevice redis = deviceEnv.getRedis();
			String val = redis.get(RedisNodeType.ACTIVITY,
					"YX_PUBLIC_ACCOUNT_ACCESS_TOKEN",
					SysConfig.getString("AppName") + ".access_token");
			if (StringUtils.isNotBlank(val)) {
				return val;
			}
			String url = String
					.format("%s/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
							getApiHost(), getAppId(), getAppSecret());
			logger.debug("request access token: " + url);
			String result = HttpClientPool.getInstance().getMethod(url);
			logger.info("request(" + url + ") access token result: " + result);
			YixinAccessToken accessToken = JSON.parseObject(result,
					YixinAccessToken.class);
			if (accessToken != null) {
				redis.set(RedisNodeType.ACTIVITY,
						"YX_PUBLIC_ACCOUNT_ACCESS_TOKEN",
						SysConfig.getString("AppName") + ".access_token",
						accessToken.getAccess_token());
				redis.expire(RedisNodeType.ACTIVITY,
						"YX_PUBLIC_ACCOUNT_ACCESS_TOKEN",
						SysConfig.getString("AppName") + ".access_token",
						accessToken.getExpires_in() - 100);
				return accessToken.getAccess_token();
			}
		} catch (Exception e) {
			logger.error("获取api access token error! " + e.getMessage());
		}
		return null;
	}

}

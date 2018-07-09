package org.scoreboard.controller;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.ContentType;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.scoreboard.annotation.LogRequestParam;
import org.scoreboard.annotation.SkipOauthVerify;
import org.scoreboard.exception.ActionException;
import org.scoreboard.response.ResponseStatus;
import org.scoreboard.service.PublicAccountService;
import org.scoreboard.utils.HttpClientPool;
import org.scoreboard.utils.IPUtil;
import org.scoreboard.utils.SignUtil;
import org.scoreboard.utils.SysConfig;
import org.scoreboard.utils.YixinNotifyRespUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Controller
public class PublicAccountController {

	private static Logger log = Logger.getLogger(PublicAccountController.class);

	@Resource
	private PublicAccountService publicAccountService;

	@RequestMapping(value = "/paverify", method = { RequestMethod.GET })
	@SkipOauthVerify
	public Object paverify(final HttpServletRequest request,
			final HttpServletResponse response) {

		try {
			String echostr = request.getParameter("echostr");
			String signature = request.getParameter("signature");
			String timestamp = request.getParameter("timestamp");
			String nonce = request.getParameter("nonce");
			String token = "scoreboard7";

			log.info("echostr==" + echostr);
			log.info("signature===" + signature);
			log.info("timestamp==" + timestamp);
			log.info("nonce===" + nonce);
			log.info("token===" + token);

			// 1. 将token、timestamp、nonce三个参数进行字典序排序
			String[] arrTmp = { token, timestamp, nonce };
			Arrays.sort(arrTmp);
			StringBuffer sb = new StringBuffer();
			// 2.将三个参数字符串拼接成一个字符串进行sha1加密
			for (int i = 0; i < arrTmp.length; i++) {
				sb.append(arrTmp[i]);
			}
			String expectedSignature = DigestUtils.sha1Hex(sb.toString());
			// 3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
			if (expectedSignature.equalsIgnoreCase(signature)) {
				if (!StringUtils.isEmpty(echostr)) {
					log.info("return echostr: " + echostr);
					return new ResponseEntity<String>(echostr, HttpStatus.OK);
				}
			}
		} catch (Exception e) {
			log.error("public account verify error!", e);
		}

		return new ResponseEntity<String>("", HttpStatus.OK);

	}

	@RequestMapping(value = "/paverify", method = { RequestMethod.POST })
	@SkipOauthVerify
	public void yixinnotify(final HttpServletRequest request,
			final HttpServletResponse response) {

		response.setContentType("text/plain;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		log.debug("notify query string: " + request.getQueryString());

		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String token = "godofyixin126";

		log.debug("signature===" + signature);
		log.debug("timestamp==" + timestamp);
		log.debug("nonce===" + nonce);
		log.debug("token===" + token);

		// 1. 将token、timestamp、nonce三个参数进行字典序排序
		String[] arrTmp = { token, timestamp, nonce };
		Arrays.sort(arrTmp);
		StringBuffer sb = new StringBuffer();
		// 2.将三个参数字符串拼接成一个字符串进行sha1加密
		for (int i = 0; i < arrTmp.length; i++) {
			sb.append(arrTmp[i]);
		}
		String expectedSignature = DigestUtils.sha1Hex(sb.toString());
		// 3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
		if (expectedSignature.equalsIgnoreCase(signature)) {
			Document doc = null;
			try {
				ServletInputStream is = request.getInputStream();
				String xmlStr = IOUtils.toString(is);
				IOUtils.closeQuietly(is);
				log.debug("notify body: " + xmlStr);
				doc = DocumentHelper.parseText(xmlStr);
			} catch (Exception e) {
				log.error("request error!", e);
			}
			if (doc == null) {
				return;
			}
			Element rootElt = doc.getRootElement();
			String content = rootElt.elementText("Content");
			String yidofpa = rootElt.elementText("ToUserName");
			String openid = rootElt.elementText("FromUserName");
			String msgType = rootElt.elementText("MsgType");
			log.debug("message(type=" + msgType + ") sent from " + openid
					+ " to " + yidofpa + ": " + content);
			String respStr = null;
			if ("text".equals(msgType)) {
				// TODO:
			}
			if (StringUtils.isNotBlank(respStr)) {
				String respXml = YixinNotifyRespUtil.createText(respStr,
						yidofpa, openid);
				log.debug("response xml: " + respXml);
				try {
					response.getWriter().write(respXml);
					response.getWriter().flush();
				} catch (Exception e) {
					log.error("response error!", e);
				}
			}
		}

	}

	/**
	 * openid/mobile只需要传入其中一个
	 */
	@RequestMapping(value = "/internal/addfollow")
	@SkipOauthVerify
	@ResponseBody
	public Object addfollow(
			final HttpServletRequest request,
			final HttpServletResponse response,
			@LogRequestParam("openid") @RequestParam(required = false) String openid,
			@LogRequestParam("mobile") @RequestParam(required = false) String mobile,
			@LogRequestParam("sign") @RequestParam String sign)
			throws ActionException, ClientProtocolException, IOException {
		String requestIp = IPUtil.getRequestIp(request);
		String requestAccessRule = SysConfig.getString("RequestAccessRule");
		if (StringUtils.isBlank(requestAccessRule)) {
			log.info("ignore check requestIp=" + requestIp);
		} else if (!IPUtil.checkIpAccessRule(requestAccessRule, requestIp)) {
			throw new ActionException(requestIp + " forbidden");
		}

		String accessToken = publicAccountService.getApiAccessToken();
		if (accessToken == null) {
			throw new ActionException("access token required");
		}

		if (StringUtils.isBlank(openid) && StringUtils.isBlank(mobile)) {
			throw new ActionException("openid or mobile required");
		}

		String token = SysConfig.getString("PUBLIC_ACCOUNT_VERIFY_TOKEN",
				"paexample2015");
		String expect = SignUtil.sha1OfNaturalOrder(openid, mobile, token);
		if (!expect.equalsIgnoreCase(sign)) {
			throw new ActionException(ResponseStatus.SIGN_ILLEGAL);
		}

		JSONObject params = new JSONObject();
		params.put("openid", openid);
		params.put("mobile", mobile);
		String result = HttpClientPool.getInstance().postMethod(
				String.format("%s/cgi-bin/follow/add?access_token=%s",
						PublicAccountService.getApiHost(), accessToken),
				JSON.toJSONString(params), ContentType.APPLICATION_JSON);
		log.info("add following result: " + result);
		JSONObject obj = JSON.parseObject(result, JSONObject.class);
		if (obj == null || obj.getInteger("errcode") == null) {
			throw new ActionException("返回结果为空");
		}
		if (obj.getInteger("errcode") == 400) {
			log.info("用户(" + openid + "/" + mobile + ")已经关注本公众号");
			return null;
		}
		if (obj.getInteger("errcode") != 0) {
			throw new ActionException(result);
		}
		return null;
	}

	@RequestMapping(value = "/internal/sendtxtmsg")
	@SkipOauthVerify
	@ResponseBody
	public Object sendmsg(final HttpServletRequest request,
			final HttpServletResponse response,
			@LogRequestParam("openid") @RequestParam String openid,
			@LogRequestParam("content") @RequestParam String content,
			@LogRequestParam("sign") @RequestParam String sign)
			throws ActionException, ClientProtocolException, IOException {
		String requestIp = IPUtil.getRequestIp(request);
		String requestAccessRule = SysConfig.getString("RequestAccessRule");
		if (StringUtils.isBlank(requestAccessRule)) {
			log.info("ignore check requestIp=" + requestIp);
		} else if (!IPUtil.checkIpAccessRule(requestAccessRule, requestIp)) {
			throw new ActionException(requestIp + " forbidden");
		}

		String accessToken = publicAccountService.getApiAccessToken();
		if (accessToken == null) {
			throw new ActionException("access token required");
		}

		if (StringUtils.isBlank(openid)) {
			throw new ActionException("openid required");
		}

		if (StringUtils.isBlank(content)) {
			throw new ActionException("content required");
		}

		String token = SysConfig.getString("PUBLIC_ACCOUNT_VERIFY_TOKEN",
				"paexample2015");
		String expect = SignUtil.sha1OfNaturalOrder(openid, content, token);
		if (!expect.equalsIgnoreCase(sign)) {
			throw new ActionException(ResponseStatus.SIGN_ILLEGAL);
		}

		JSONObject params = new JSONObject();
		params.put("touser", openid);
		params.put("msgtype", "text");
		JSONObject contentx = new JSONObject();
		contentx.put("content", content);
		params.put("text", contentx);
		String result = HttpClientPool.getInstance().postMethod(
				String.format("%s/cgi-bin/message/send?access_token=%s",
						PublicAccountService.getApiHost(), accessToken),
				JSON.toJSONString(params), ContentType.APPLICATION_JSON);
		log.info("sendtxtmsg result: " + result);
		JSONObject obj = JSON.parseObject(result, JSONObject.class);
		if (obj.getInteger("errcode") == null || obj.getInteger("errcode") != 0) {
			throw new ActionException(result);
		}
		return null;
	}

	@RequestMapping(value = "/internal/uploadmedia")
	@SkipOauthVerify
	@ResponseBody
	public Object uploadmedia(final HttpServletRequest request,
			final HttpServletResponse response,
			@LogRequestParam("type") @RequestParam String type,
			@LogRequestParam("filePath") @RequestParam String filePath,
			@LogRequestParam("sign") @RequestParam String sign)
			throws ActionException, ClientProtocolException, IOException {
		String requestIp = IPUtil.getRequestIp(request);
		String requestAccessRule = SysConfig.getString("RequestAccessRule");
		if (StringUtils.isBlank(requestAccessRule)) {
			log.info("ignore check requestIp=" + requestIp);
		} else if (!IPUtil.checkIpAccessRule(requestAccessRule, requestIp)) {
			throw new ActionException(requestIp + " forbidden");
		}

		String accessToken = publicAccountService.getApiAccessToken();
		if (accessToken == null) {
			throw new ActionException("access token required");
		}

		if (StringUtils.isBlank(type)) {
			throw new ActionException("type required");
		}

		if (StringUtils.isBlank(filePath)) {
			throw new ActionException("filePath required");
		}

		String token = SysConfig.getString("PUBLIC_ACCOUNT_VERIFY_TOKEN",
				"paexample2015");
		String expect = SignUtil.sha1OfNaturalOrder(type, filePath, token);
		if (!expect.equalsIgnoreCase(sign)) {
			throw new ActionException(ResponseStatus.SIGN_ILLEGAL);
		}

		String result = HttpClientPool.getInstance().postMultiFormDataMethod(
				String.format(
						"%s/cgi-bin/media/upload?access_token=%s&type=%s",
						PublicAccountService.getApiHost(), accessToken, type),
				"media", new File(filePath));
		log.info("uploadmedia result: " + result);
		JSONObject obj = JSON.parseObject(result, JSONObject.class);
		if (obj != null && obj.getInteger("errcode") != null
				&& obj.getInteger("errcode") != 0) {
			throw new ActionException(result);
		}
		return obj;
	}
}

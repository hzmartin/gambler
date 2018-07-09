package org.scoreboard.controller;

import java.util.Arrays;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.scoreboard.service.PublicAccountService;
import org.scoreboard.utils.NotifyRespUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PublicAccountController {

	private static Logger log = Logger.getLogger(PublicAccountController.class);

	@Resource
	private PublicAccountService publicAccountService;

	@RequestMapping(value = "/paverify", method = { RequestMethod.GET })
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
				String respXml = NotifyRespUtil.createText(respStr,
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
}

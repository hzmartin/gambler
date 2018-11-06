package gambler.quartz.service;

import gambler.quartz.utils.ConfigUtil;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {

	protected final Logger log = Logger.getLogger(getClass());

	@Autowired
	private JavaMailSenderImpl javaMailSender;

	private void setUsername() {
		javaMailSender.setUsername(ConfigUtil.getDeployUserAccount() + "@"
				+ ConfigUtil.getHostname());
	}

	private String getUsername() {
		return javaMailSender.getUsername();
	}

	public boolean sendMimeMessage(String[] tos, String subject, String text) {
		boolean enabled = ConfigUtil.isMailServiceEnabled();
		if (!enabled) {
			return false;
		}
		try {
			if (StringUtils.isBlank(getUsername())) {
				setUsername();
			}
			if (ArrayUtils.isEmpty(tos)) {
				throw new IllegalArgumentException("to list required!");
			}
			if (StringUtils.isEmpty(subject)) {
				throw new IllegalArgumentException("subject required!");
			}
			if (StringUtils.isEmpty(text)) {
				text = "";
			}
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
					false, "utf-8");
			mimeMessage.setContent(text, "text/html;charset=utf-8");
			helper.setTo(tos);
			helper.setSubject(subject);
			helper.setFrom(javaMailSender.getUsername());
			javaMailSender.send(mimeMessage);
			return true;
		} catch (Exception e) {
			log.error("send mail fail", e);
			return false;
		}
	}

	public boolean sendSimpleMail(String to, String subject, String text) {
		return sendSimpleMail(new String[] { to }, null, subject, text);
	}

	public boolean sendSimpleMail(String[] tos, String subject, String text) {
		return sendSimpleMail(tos, null, subject, text);
	}

	public boolean sendSimpleMail(String[] tos, String[] ccs, String subject,
			String text) {
		boolean enabled = ConfigUtil.isMailServiceEnabled();
		if (!enabled) {
			return false;
		}
		try {
			if (ArrayUtils.isEmpty(tos)) {
				throw new IllegalArgumentException("to list required!");
			}
			if (StringUtils.isEmpty(subject)) {
				throw new IllegalArgumentException("subject required!");
			}
			if (StringUtils.isEmpty(text)) {
				text = "";
			}
			if (StringUtils.isBlank(getUsername())) {
				setUsername();
			}
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setTo(tos);
			if (ArrayUtils.isNotEmpty(ccs)) {
				mailMessage.setCc(ccs);
			}
			mailMessage.setFrom(javaMailSender.getUsername());
			mailMessage.setSubject(subject);
			mailMessage.setText(text);
			log.info("sending... " + mailMessage.toString());
			javaMailSender.send(mailMessage);
			return true;
		} catch (Exception e) {
			log.error("send mail fail", e);
			return false;
		}
	}
}

package gambler.examples.webapp2.service;

import java.io.File;
import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService extends AbstractService {

	@Autowired
	private JavaMailSender javaMailSender;

	/**
	 * 发送普通邮件
	 * @param tos
	 *            收信人, 不空
	 * @param ccs
	 *            抄送给谁
	 * @param subject
	 *            主题. 不空
	 * @param encoding
	 *            邮件编码，必须
	 * @param text
	 *            邮件内容, html格式, 不空
	 */
	public void sendHtmlMail(String[] tos, String[] ccs, String subject,
			String encoding, String text) throws MessagingException,
			UnsupportedEncodingException {
		sendHtmlMail(tos, ccs, subject, encoding, text, null, null);
	}

	/**
	 * 发送带附件邮件
	 * 
	 * @param tos
	 *            收信人, 不空
	 * @param ccs
	 *            抄送给谁
	 * @param subject
	 *            主题. 不空
	 * @param encoding
	 *            邮件编码，必须
	 * @param text
	 *            邮件内容, html格式, 不空
	 * @param filePath
	 *            附件路径
	 * @param fileName
	 *            附件名称
	 */
	public void sendHtmlMail(String[] tos, String[] ccs, String subject,
			String encoding, String text, String filePath, String fileName)
			throws MessagingException, UnsupportedEncodingException {
		if (ArrayUtils.isEmpty(tos)) {
			throw new IllegalArgumentException("to list required!");
		}
		if (StringUtils.isEmpty(subject)) {
			throw new IllegalArgumentException("subject required!");
		}
		if (StringUtils.isEmpty(text)) {
			throw new IllegalArgumentException("text required!");
		}

		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(message, true,
				encoding);
		for (int i = 0; i < tos.length; i++) {
			messageHelper.addTo(tos[i]);
		}
		if (null != ccs) {
			for (int i = 0; i < ccs.length; i++) {
				messageHelper.addCc(ccs[i]);
			}
		}
		messageHelper.setFrom(((JavaMailSenderImpl) javaMailSender)
				.getUsername());
		messageHelper.setSubject(subject);
		messageHelper.setText(text, true);
		if (StringUtils.isNotBlank(filePath)
				&& StringUtils.isNotBlank(fileName)) {
			File dirFile = new File(filePath);
			if (dirFile.exists() && new File(dirFile, fileName).exists()) {
				FileSystemResource file = new FileSystemResource(dirFile);
				messageHelper.addAttachment(fileName, file);
			}
		}
		javaMailSender.send(message);
	}
}

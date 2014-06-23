package gambler.examples.webapp2.controller;

import gambler.examples.webapp2.domain.AuthUser;
import gambler.examples.webapp2.exception.ActionException;
import gambler.examples.webapp2.resp.ResponseStatus;
import gambler.examples.webapp2.service.AuthUserService;
import gambler.examples.webapp2.vo.Account;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/account")
public class LoginController {

	private static final Logger logger = Logger
			.getLogger(LoginController.class);

	@Resource
	private AuthUserService authUserService;

	@RequestMapping(value = "/login")
	@ResponseBody
	public Object login(final HttpServletRequest request,
			final HttpServletResponse response,
			@RequestParam(required = true) String userId,
			@RequestParam(required = true) String password,
			@RequestParam(required = false) boolean remme)
			throws ActionException {

		AuthUser dbUser = authUserService.findUserById(userId);
		if (dbUser == null) {
			throw new ActionException(ResponseStatus.USER_NOT_EXSIST);
		}
		Account login = authUserService.login(request, response, userId,
				password, remme);
		if (login == null) {
			throw new ActionException(ResponseStatus.LOGIN_FAILED);
		}
		return login;
	}

	@RequestMapping(value = "/createSuper")
	@ResponseBody
	public Object create(final HttpServletRequest request,
			@RequestParam(required = true) String userId,
			@RequestParam(required = true) String password)
			throws ActionException {
		AuthUser dbUser = authUserService.findUserById(userId);
		if (dbUser != null) {
			throw new ActionException(ResponseStatus.USER_ALREADY_EXSIST);
		}
		AuthUser user = new AuthUser();
		user.setUserId(userId);
		user.setSuperUser(true);
		user.setPassword(authUserService.getSaltedPassword(password, userId));
		return authUserService.save(user);
	}

	@RequestMapping(value = "/logout")
	@ResponseBody
	public Object logout(final HttpServletRequest request,
			final HttpServletResponse response) {
		Account loginUser = authUserService.getLoginUser(request);
		authUserService.logout(request, response);
		logger.info(loginUser + " 登出成功！");
		return null;
	}

}

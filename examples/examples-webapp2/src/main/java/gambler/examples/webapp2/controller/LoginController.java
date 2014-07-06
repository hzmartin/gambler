package gambler.examples.webapp2.controller;

import gambler.examples.webapp2.domain.auth.User;
import gambler.examples.webapp2.exception.ActionException;
import gambler.examples.webapp2.resp.ResponseStatus;
import gambler.examples.webapp2.service.AuthUserService;
import gambler.examples.webapp2.dto.AccountDto;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/account")
public class LoginController extends AbstractController {

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

		User dbUser = authUserService.findUserById(userId);
		if (dbUser == null) {
			throw new ActionException(ResponseStatus.USER_NOT_EXSIST);
		}
		AccountDto login = authUserService.login(request, response, userId,
				password, remme);
		if (login == null) {
			throw new ActionException(ResponseStatus.LOGIN_FAILED);
		}
		return login;
	}

	@RequestMapping(value = "/logout")
	@ResponseBody
	public Object logout(final HttpServletRequest request,
			final HttpServletResponse response) {
		AccountDto loginUser = authUserService.getLoginUser(request);
		authUserService.logout(request, response);
		logger.info(loginUser + " 登出成功！");
		return null;
	}

}

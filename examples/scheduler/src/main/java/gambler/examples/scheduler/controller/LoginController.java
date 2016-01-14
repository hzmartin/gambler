package gambler.examples.scheduler.controller;

import gambler.examples.scheduler.annotation.AuthRequired;
import gambler.examples.scheduler.annotation.LogRequestParam;
import gambler.examples.scheduler.constant.AuthConstants;
import gambler.examples.scheduler.domain.auth.User;
import gambler.examples.scheduler.dto.AccountDto;
import gambler.examples.scheduler.exception.ActionException;
import gambler.examples.scheduler.resp.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/account")
public class LoginController extends AbstractController {

	@RequestMapping(value = "/login")
	@ResponseBody
	public Object login(final HttpServletRequest request,
			final HttpServletResponse response,
			@LogRequestParam(name="userId") @RequestParam(required = true) String userId,
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

	@RequestMapping(value = "/switchUser")
	@ResponseBody
	@AuthRequired(permission = { AuthConstants.PERM_SUPER })
	public Object switchUser(final HttpServletRequest request,
			@RequestParam String userId) throws ActionException {
		AccountDto loginUser = authUserService.getLoginUser(request);
		authUserService.switchUser(request, userId);
		logger.info(loginUser + "已经切换账号至：" + userId + "成功！");
		return null;
	}
}

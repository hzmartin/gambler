package gambler.examples.webapp2.controller;

import gambler.examples.webapp2.annotation.AuthRequired;
import gambler.examples.webapp2.annotation.LogRequestParam;
import gambler.examples.webapp2.constant.AuthConstants;
import gambler.examples.webapp2.domain.auth.Permission;
import gambler.examples.webapp2.domain.auth.User;
import gambler.examples.webapp2.domain.auth.UserPermission;
import gambler.examples.webapp2.domain.auth.UserRole;
import gambler.examples.webapp2.dto.AccountDto;
import gambler.examples.webapp2.dto.PermissionDto;
import gambler.examples.webapp2.exception.AccessForbiddenException;
import gambler.examples.webapp2.exception.ActionException;
import gambler.examples.webapp2.exception.UnexpectedException;
import gambler.examples.webapp2.resp.ResponseStatus;
import gambler.examples.webapp2.util.RegexValidateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/sysmgmt")
public class SysMgmtController extends AbstractController {

	@RequestMapping(value = "/createSuper")
	@ResponseBody
	public Object createSuper(
			final HttpServletRequest request,
			@LogRequestParam(name = "userId") @RequestParam(required = true) String userId,
			@RequestParam(required = true) String password,
			@LogRequestParam(name = "nick") @RequestParam(required = false) String nick)
			throws ActionException, AccessForbiddenException {
		if (!sysconf.getBoolean("switch.enableCreateSuper", false)) {
			throw new AccessForbiddenException("switch.enableCreateSuper off");
		}
		if (!RegexValidateUtil.isValidUserId(userId)) {
			throw new ActionException(ResponseStatus.PARAM_ILLEGAL,
					"以字母开头的字母+数字+下划线，6-20位");
		}
		User dbUser = authUserService.findUserById(userId);
		if (dbUser != null) {
			throw new ActionException(ResponseStatus.USER_ALREADY_EXSIST,
					new Object[] { userId });
		}
		User user = new User();
		user.setUserId(userId);
		user.setIssuper(1);
		user.setIsactive(1);
		user.setNick(nick);
		user.setPassword(authUserService.getSaltedPassword(password, userId));
		return authUserService.saveAsSystemUser(user);
	}

	@RequestMapping(value = "/updatePassword")
	@AuthRequired
	@ResponseBody
	public Object updatePassword(final HttpServletRequest request,
			@RequestParam String userId, @RequestParam String oldPass,
			@RequestParam String newPass) throws ActionException {
		AccountDto loginUser = authUserService.getLoginUser(request);

		if (loginUser.getIssuper() == 0) {
			if (!loginUser.getUserId().equals(userId)) {
				throw new ActionException(
						ResponseStatus.NO_PERMISSION,
						loginUser
								+ " is only allowed to change your own password！");
			}
			User user = authUserService.findUserById(userId);
			if (!authUserService.isCorrentPassword(oldPass, user.getPassword(),
					user.getUserId())) {
				throw new ActionException(ResponseStatus.PASSWORD_ERROR);
			}
		}
		User newUser = new User();
		newUser.setUserId(userId);
		newUser.setPassword(authUserService.getSaltedPassword(newPass, userId));
		int count = authUserService.updatePassword(newUser);
		logger.info(loginUser + " updated password! affected count=" + count);
		return count;
	}

	@RequestMapping(value = "/createOrUpdateUser")
	@AuthRequired(permission = { AuthConstants.PERM_SYSTEM.PERM_CREATE_UPDATE_USER })
	@ResponseBody
	public Object createOrUpdateUser(
			final HttpServletRequest request,
			@LogRequestParam(name = "userId") @RequestParam(required = true) String userId,
			@LogRequestParam(name = "nick") @RequestParam(required = false) String nick)
			throws UnexpectedException, ActionException {
		if (!RegexValidateUtil.isValidUserId(userId)) {
			throw new ActionException(ResponseStatus.PARAM_ILLEGAL,
					"以字母开头的字母+数字+下划线，6-20位");
		}
		AccountDto loginUser = authUserService.getLoginUser(request);
		User user = authUserService.findUserById(userId);
		User newUser = new User();
		newUser.setUserId(userId);
		newUser.setIsactive(1);
		newUser.setIssuper(0);
		newUser.setNick(nick);
		newUser.setPassword(authUserService.getSaltedPassword(
				DigestUtils.md5Hex("123456"), userId));
		if (user != null) {
			int updateCount = authUserService.updateUser(newUser);
			if (updateCount == 1) {
				logger.info(loginUser + " updated user " + user);
				return updateCount;
			}
		} else {
			int updateCount = authUserService.saveAsSystemUser(newUser);
			if (updateCount == 1) {
				logger.info(loginUser + " created user " + user);
				return updateCount;
			}
		}
		throw new UnexpectedException(loginUser + " create/upate failed!");

	}

	@RequestMapping(value = "/delUser")
	@ResponseBody
	@AuthRequired(permission = { AuthConstants.PERM_SYSTEM.PERM_DEL_USER })
	public Object delSysUsers(
			final HttpServletRequest request,
			@LogRequestParam(name = "userId") @RequestParam(required = true) String userId)
			throws UnexpectedException, ActionException {
		if (!RegexValidateUtil.isValidUserId(userId)) {
			throw new ActionException(ResponseStatus.PARAM_ILLEGAL,
					"以字母开头的字母+数字+下划线，6-20位");
		}
		AccountDto loginUser = authUserService.getLoginUser(request);
		if (loginUser.getUserId().equals(userId)) {
			throw new ActionException(ResponseStatus.NO_PERMISSION, loginUser
					+ " is not allowed to delete your own account！");
		}
		int delUserCount = authUserService.deleteUser(userId);
		if (delUserCount == 1) {
			logger.info(loginUser + " delete user " + userId);
			return delUserCount;
		} else {
			throw new UnexpectedException(loginUser + " delete user " + userId
					+ " failed!");
		}

	}

	@RequestMapping(value = "/getUser")
	@ResponseBody
	@AuthRequired(permission = { AuthConstants.PERM_SYSTEM.PERM_LIST_USER })
	public Object getUser(
			final HttpServletRequest request,
			@LogRequestParam(name = "userId") @RequestParam(required = true) String userId)
			throws ActionException {
		if (!RegexValidateUtil.isValidUserId(userId)) {
			throw new ActionException(ResponseStatus.PARAM_ILLEGAL,
					"以字母开头的字母+数字+下划线，6-20位");
		}
		User user = authUserService.findUserById(userId);
		if (user == null) {
			throw new ActionException(ResponseStatus.USER_NOT_EXSIST);
		} else {
			return new AccountDto(user);
		}

	}

	@RequestMapping(value = "/getUserPermission")
	@ResponseBody
	@AuthRequired(permission = { AuthConstants.PERM_SYSTEM.PERM_LIST_USERPERM })
	public Object getUserPermission(
			final HttpServletRequest request,
			@LogRequestParam(name = "userId") @RequestParam(required = true) String userId)
			throws ActionException {
		if (!RegexValidateUtil.isValidUserId(userId)) {
			throw new ActionException(ResponseStatus.PARAM_ILLEGAL,
					"以字母开头的字母+数字+下划线，6-20位");
		}
		User user = authUserService.findUserById(userId);
		if (user == null) {
			throw new ActionException(ResponseStatus.USER_NOT_EXSIST);
		}
		List<UserPermission> userPerms = authUserService
				.getUserPermissions(user.getUserId());
		List<UserRole> userRoles = authUserService.getUserRoles(user
				.getUserId());
		List<PermissionDto> result = new ArrayList<PermissionDto>();
		List<Permission> allPerms = AuthConstants.getAllPermissions();
		for (Permission perm : allPerms) {
			PermissionDto pvo = new PermissionDto();
			pvo.setPid(perm.getPid());
			pvo.setName(perm.getName());
			pvo.setRemark(perm.getRemark());
			for (UserPermission userPerm : userPerms) {
				if (userPerm.getPid() == perm.getPid()) {
					pvo.setUserHave(true);
					break;
				}
			}
			for (UserRole userRole : userRoles) {
				List<Permission> rolePermissions = AuthConstants
						.getRolePermissions(userRole.getRid());
				for (Permission permission : rolePermissions) {
					if (permission.getPid() == perm.getPid()) {
						pvo.setRoleHave(true);
						break;
					}
				}
				if (pvo.isRoleHave()) {
					break;
				}
			}
			result.add(pvo);
		}
		return new Object[] { new AccountDto(user), result };

	}

	/**
	 * 
	 * @param request
	 * @param userId
	 * @param config
	 *            - json format {"1":true,xxx}, 1 is pid - true: grant the
	 *            permission, false: revoke the permission
	 * @return
	 * @throws ActionException
	 */
	@RequestMapping(value = "/updateUserPermission")
	@ResponseBody
	@AuthRequired(permission = { AuthConstants.PERM_SYSTEM.PERM_UPDATE_USERPERM })
	public Object updateUserPermission(
			final HttpServletRequest request,
			@LogRequestParam(name = "userId") @RequestParam(required = true) String userId,
			@LogRequestParam(name = "config") @RequestParam(required = true) String config)
			throws ActionException {
		if (!RegexValidateUtil.isValidUserId(userId)) {
			throw new ActionException(ResponseStatus.PARAM_ILLEGAL,
					"以字母开头的字母+数字+下划线，6-20位");
		}
		if (StringUtils.isBlank(config)) {
			return null;
		}
		JSONObject map = JSONObject.fromObject(config);
		if (map.isEmpty()) {
			return null;
		}
		AccountDto loginUser = authUserService.getLoginUser(request);
		if (loginUser.getUserId().equals(userId)) {
			throw new ActionException(ResponseStatus.NO_PERMISSION, loginUser
					+ " can't grant permission to yourself！");
		}
		User user = authUserService.findUserById(userId);
		@SuppressWarnings("unchecked")
		Set<String> keySet = map.keySet();
		for (String key : keySet) {
			if (map.getBoolean(key)) {
				// grant
				int createCount = authUserService.createUserPermission(
						user.getUid(), Long.parseLong(key));
				logger.info(loginUser + " grant user permission " + key
						+ "! affected count=" + createCount);
			} else {
				// revoke
				int delCount = authUserService.delUserPermission(user.getUid(),
						Long.parseLong(key));
				logger.info(loginUser + " revoke user permission " + key
						+ "! affected count=" + delCount);
			}
		}
		return null;
	}
}

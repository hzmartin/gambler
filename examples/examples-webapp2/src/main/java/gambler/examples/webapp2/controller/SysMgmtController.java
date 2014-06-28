package gambler.examples.webapp2.controller;

import gambler.examples.webapp2.annotation.AuthRequired;
import gambler.examples.webapp2.annotation.LogRequestParam;
import gambler.examples.webapp2.constant.AuthConstants;
import gambler.examples.webapp2.domain.auth.Permission;
import gambler.examples.webapp2.domain.auth.User;
import gambler.examples.webapp2.domain.auth.UserPermission;
import gambler.examples.webapp2.domain.auth.UserRole;
import gambler.examples.webapp2.exception.AccessForbiddenException;
import gambler.examples.webapp2.exception.ActionException;
import gambler.examples.webapp2.exception.UnexpectedException;
import gambler.examples.webapp2.resp.ResponseStatus;
import gambler.examples.webapp2.service.AuthUserService;
import gambler.examples.webapp2.util.RegexUtil;
import gambler.examples.webapp2.vo.Account;
import gambler.examples.webapp2.vo.PermissionVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/sysmgmt")
public class SysMgmtController extends AbstractController {

	private static Logger logger = Logger.getLogger(SysMgmtController.class);

	@Resource
	private AuthUserService authUserService;

	@RequestMapping(value = "/createSuper")
	@ResponseBody
	public Object createSuper(
			final HttpServletRequest request,
			@LogRequestParam(name = "userId") @RequestParam(required = true) String userId,
			@RequestParam(required = true) String password)
			throws ActionException, AccessForbiddenException {
		if (sysconf.getBoolean("switch.accessCreateSuper", false)) {
			throw new AccessForbiddenException("switch.accessCreateSuper off");
		}
		if (!RegexUtil.isValidUserId(userId)) {
			throw new ActionException(ResponseStatus.PARAM_ILLEGAL,
					"userId illegal");
		}
		User dbUser = authUserService.findUserById(userId);
		if (dbUser != null) {
			throw new ActionException(ResponseStatus.USER_ALREADY_EXSIST);
		}
		User user = new User();
		user.setUserId(userId);
		user.setIssuper(1);
		user.setIsactive(1);
		user.setPassword(authUserService.getSaltedPassword(password, userId));
		return authUserService.saveAsSystemUser(user);
	}

	@RequestMapping(value = "/updatePassword")
	@AuthRequired
	@ResponseBody
	public Object updatePassword(final HttpServletRequest request,
			@RequestParam String userId, @RequestParam String oldPass,
			@RequestParam String newPass) throws ActionException {
		Account loginUser = authUserService.getLoginUser(request);

		if (loginUser.getIssuper() == 0) {
			if (loginUser.getUserId().equals(userId)) {
				throw new ActionException(ResponseStatus.NO_PERMISSION,
						loginUser + "不允许修改别人的密码！");
			}
			User user = authUserService.findUserById(userId);
			if (!authUserService.isCorrentPassword(oldPass, user.getPassword(),
					user.getUserId())) {
				throw new ActionException(ResponseStatus.PASSWORD_ERROR,
						"密码校验失败!");
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
	@AuthRequired(requiredPerms = { AuthConstants.PERM_SYSTEM.PERM_CREATE_UPDATE_USER })
	@ResponseBody
	public Object createOrUpdateUser(
			final HttpServletRequest request,
			@LogRequestParam(name = "userId") @RequestParam(required = true) String userId,
			@LogRequestParam(name = "nick") @RequestParam(required = false) String nick)
			throws UnexpectedException, ActionException {
		if (!RegexUtil.isValidUserId(userId)) {
			throw new ActionException(ResponseStatus.PARAM_ILLEGAL,
					"userId illegal");
		}
		Account loginUser = authUserService.getLoginUser(request);
		User user = authUserService.findUserById(userId);
		User newUser = new User();
		newUser.setUserId(userId);
		newUser.setIsactive(1);
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
	@AuthRequired(requiredPerms = { AuthConstants.PERM_SYSTEM.PERM_DEL_USER })
	public Object delSysUsers(
			final HttpServletRequest request,
			@LogRequestParam(name = "userId") @RequestParam(required = true) String userId)
			throws UnexpectedException, ActionException {
		if (!RegexUtil.isValidUserId(userId)) {
			throw new ActionException(ResponseStatus.PARAM_ILLEGAL,
					"userId illegal");
		}
		Account loginUser = authUserService.getLoginUser(request);
		if (loginUser.getUserId().equals(userId)) {
			throw new ActionException(ResponseStatus.NO_PERMISSION, loginUser
					+ "无法删除自己的账号！");
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
	@AuthRequired(requiredPerms = { AuthConstants.PERM_SYSTEM.PERM_LIST_USER })
	public Object getUser(
			final HttpServletRequest request,
			@LogRequestParam(name = "userId") @RequestParam(required = true) String userId)
			throws ActionException {
		if (!RegexUtil.isValidUserId(userId)) {
			throw new ActionException(ResponseStatus.PARAM_ILLEGAL,
					"userId illegal");
		}
		User user = authUserService.findUserById(userId);
		if (user == null) {
			throw new ActionException(ResponseStatus.USER_NOT_EXSIST);
		} else {
			return new Account(user);
		}

	}

	@RequestMapping(value = "/getUserPermission")
	@ResponseBody
	@AuthRequired(requiredPerms = { AuthConstants.PERM_SYSTEM.PERM_LIST_USERPERM })
	public Object getUserPermission(
			final HttpServletRequest request,
			@LogRequestParam(name = "userId") @RequestParam(required = true) String userId)
			throws ActionException {
		if (!RegexUtil.isValidUserId(userId)) {
			throw new ActionException(ResponseStatus.PARAM_ILLEGAL,
					"userId illegal");
		}
		User user = authUserService.findUserById(userId);
		if (user == null) {
			throw new ActionException(ResponseStatus.USER_NOT_EXSIST);
		}
		List<UserPermission> userPerms = authUserService
				.getUserPermissions(user.getUserId());
		List<UserRole> userRoles = authUserService.getUserRoles(user
				.getUserId());
		List<PermissionVO> result = new ArrayList<PermissionVO>();
		List<Permission> allPerms = AuthConstants.getAllPermissions();
		for (Permission perm : allPerms) {
			PermissionVO pvo = new PermissionVO();
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
		return new Object[] { new Account(user), result };

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
	@AuthRequired(requiredPerms = { AuthConstants.PERM_SYSTEM.PERM_UPDATE_USERPERM })
	public Object updateUserPermission(
			final HttpServletRequest request,
			@LogRequestParam(name = "userId") @RequestParam(required = true) String userId,
			@LogRequestParam(name = "config") @RequestParam(required = true) String config)
			throws ActionException {
		if (!RegexUtil.isValidUserId(userId)) {
			throw new ActionException(ResponseStatus.PARAM_ILLEGAL,
					"userId illegal");
		}
		if (StringUtils.isBlank(config)) {
			return null;
		}
		JSONObject map = JSONObject.fromObject(config);
		if (map.isEmpty()) {
			return null;
		}
		Account loginUser = authUserService.getLoginUser(request);
		if (loginUser.getUserId().equals(userId)) {
			throw new ActionException(ResponseStatus.NO_PERMISSION, loginUser
					+ "无法给自己分配权限！");
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

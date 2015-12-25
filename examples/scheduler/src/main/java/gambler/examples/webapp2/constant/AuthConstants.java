package gambler.examples.webapp2.constant;

import gambler.examples.webapp2.domain.auth.Permission;
import gambler.examples.webapp2.domain.auth.Role;
import gambler.examples.webapp2.domain.auth.RolePermission;
import gambler.examples.webapp2.service.AuthUserService;
import gambler.examples.webapp2.util.SpringContextHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

public class AuthConstants {

	public static final long PERM_SUPER = -1L; // 不存在这个权限ID, only super can
												// access

	public static interface PERM_SYSTEM {

		long PERM_LIST_USER = 100L; // 查看系统用户

		long PERM_CREATE_UPDATE_USER = 101L; // 创建和更新系统用户

		long PERM_DEL_USER = 102L; // 删除系统用户

		long PERM_LIST_USERPERM = 103L; // 查看用户权限信息

		long PERM_UPDATE_USERPERM = 104L;// 更新用户权限信息

		long PERM_CREATE_UPDATE_ROLE = 105L;//创建和更新系统角色

		long PERM_DEL_ROLE = 106;//删除系统角色

		long PERM_ASSIGN_ROLE = 107;//分配系统角色

		long PERM_ASSIGN_ROLE_PERM = 108;//分配角色权限

	}

	public static interface PERM_SCHEDULER {
		long PERM_VIEW_SCHEDULE_INFO = 400;
		long PERM_VIEW_JOB_INFO = 401;
		long PERM_VIEW_TRIGGER_INFO = 402;
		long PERM_ADD_JOB = 403;
		long PERM_EXEC_JOB = 404;
		long PERM_DEL_JOB = 405;
	}

	public static interface PERM_MISC {
		long PERM_ENTER_HOME_PAGE = 1000; // 进入主页
		long PERM_TEST_ANY = 1001;// 测试

	}

	public static final long SYSTEM_USER_ROLE = 0;// 系统普通用户：拥有系统默认权限配置

	private static Map<Long, Permission> permMap = new HashMap<Long, Permission>();

	private static Map<Long, List<Permission>> rolePermsMap = new HashMap<Long, List<Permission>>();

	private static Map<Long, Role> roleMap = new HashMap<Long, Role>();

	public static void reloadAllPermissions() {
		Map<Long, Permission> permMap = new HashMap<Long, Permission>();
		AuthUserService authService = SpringContextHolder
				.getBean("authUserService");
		List<Permission> perms = authService.getAllPermissions();
		for (Permission p : perms) {
			permMap.put(p.getPid(), p);
		}
		AuthConstants.permMap = permMap;
	}

	public static void reloadAllRoles() {
		Map<Long, Role> roleMap = new HashMap<Long, Role>();
		AuthUserService authService = SpringContextHolder
				.getBean("authUserService");
		List<Role> rs = authService.getAllRoles();
		for (Role r : rs) {
			roleMap.put(r.getRid(), r);
		}
		AuthConstants.roleMap = roleMap;
	}

	public static void reloadAllRolePermissions() {
		Map<Long, List<Permission>> rolePermsMap = new HashMap<Long, List<Permission>>();
		AuthUserService authService = SpringContextHolder
				.getBean("authUserService");
		List<RolePermission> allRolePermissions = authService
				.getAllRolePermissions();
		for (RolePermission p : allRolePermissions) {
			List<Permission> perms = rolePermsMap.get(p.getRid());
			if (perms == null) {
				perms = new ArrayList<Permission>();
				rolePermsMap.put(p.getRid(), perms);
			}
			Permission permission = getPermission(p.getPid());
			if (permission != null) {
				perms.add(permission);
			}
		}
		AuthConstants.rolePermsMap = rolePermsMap;
	}

	public static final boolean checkRolePermission(long rid, long pid) {
		List<Permission> rolePermission = getRolePermissions(rid);
		if (CollectionUtils.isEmpty(rolePermission)) {
			return false;
		}
		for (Permission permission : rolePermission) {
			if (pid == permission.getPid()) {
				return true;
			}
		}
		return false;
	}

	public static final Collection<Role> getAllRoles() {
		return roleMap.values();
	}

	public static final Collection<Permission> getAllPermissions() {
		return permMap.values();
	}

	public static final Permission getPermission(long pid) {
		return permMap.get(pid);
	}

	public static final List<Permission> getRolePermissions(long rid) {
		return rolePermsMap.get(rid);
	}

	public static final Role getRole(long rid) {
		return roleMap.get(rid);
	}
}

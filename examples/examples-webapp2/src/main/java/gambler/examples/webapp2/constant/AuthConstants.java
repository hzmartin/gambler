package gambler.examples.webapp2.constant;

import gambler.examples.webapp2.domain.auth.Permission;
import gambler.examples.webapp2.domain.auth.Role;
import gambler.examples.webapp2.domain.auth.RolePermission;
import gambler.examples.webapp2.service.AuthUserService;
import gambler.examples.webapp2.util.SpringContextHolder;

import java.util.ArrayList;
import java.util.List;

public class AuthConstants {

	public static final long PERM_SUPER = -1L; // 不存在这个权限ID, only super can
												// access

	/**
	 * 系统管理专区：1-100
	 */
	public static interface PERM_SYSTEM {

		long PERM_ENTER_SYSUSER_MGMT = 1L; // 进入系统用户管理系统

		long PERM_CREATE_UPDATE_USER = 2L; // 创建和更新系统用户

		long PERM_LIST_USER = 3L; // 查看系统用户

		long PERM_DEL_USER = 4L; // 删除系统用户

		long PERM_ENTER_SYSPERM_MGMT = 5L; // 进入权限管理系统

		long PERM_LIST_USERPERM = 6L; // 查看用户权限信息

		long PERM_UPDATE_USERPERM = 7L;// 更新用户权限信息

	}

	/**
	 * 其他：1000+
	 */
	public static interface PERM_MISC {
		long PERM_ENTER_HOME_PAGE = 1000; // 进入主页

	}

	public static final Role SYSTEM_USER = new Role(0);// 系统普通用户：拥有系统默认权限配置

	public static final Role SYSTEM_MANAGER = new Role(1);// 系统管理员：管理系统用户，操作日志等权限

	public static final Role PERMISSION_MANAGER = new Role(2);// 权限管理员：授权，回收，角色分配等权限

	private static List<Permission> permissions = new ArrayList<Permission>();
	private static List<RolePermission> rolePerms = new ArrayList<RolePermission>();

	public static void reloadAllPermissions() {
		AuthUserService authService = SpringContextHolder
				.getBean("authUserService");
		permissions = authService.getAllPermissions();
	}

	public static void reloadAllRolePermissions() {
		AuthUserService authService = SpringContextHolder
				.getBean("authUserService");
		rolePerms = authService.getAllRolePermissions();
	}

	public static final List<Permission> getAllPermissions() {
		return permissions;
	}

	public static final Permission getPermission(long pid) {
		for (Permission p : permissions) {
			if (p.getPid() == pid) {
				return p;
			}
		}
		return null;
	}

	public static final List<Permission> getRolePermissions(long rid) {
		List<Permission> perms = new ArrayList<Permission>();
		for (RolePermission p : rolePerms) {
			if (p.getRid() == rid) {
				Permission permission = getPermission(p.getPid());
				if (permission != null) {
					perms.add(permission);
				}
			}
		}
		return perms;
	}
}

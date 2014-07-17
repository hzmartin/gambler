package gambler.examples.webapp2.constant;

import gambler.examples.webapp2.domain.auth.Permission;
import gambler.examples.webapp2.domain.auth.RolePermission;
import gambler.examples.webapp2.service.AuthUserService;
import gambler.examples.webapp2.util.SpringContextHolder;

import java.util.ArrayList;
import java.util.List;

public class AuthConstants {

	public static final long PERM_SUPER = -1L; // 不存在这个权限ID, only super can
												// access

	public static interface PERM_SYSTEM {


		long PERM_LIST_USER = 100L; // 查看系统用户
		
		long PERM_CREATE_UPDATE_USER = 101L; // 创建和更新系统用户

		long PERM_DEL_USER = 102L; // 删除系统用户

		long PERM_LIST_USERPERM = 103L; // 查看用户权限信息

		long PERM_UPDATE_USERPERM = 104L;// 更新用户权限信息

	}

	public static interface PERM_SCHEDULER {
		long PERM_VIEW_SCHEDULE_INFO = 400;
		long PERM_VIEW_JOB_INFO = 401;
		long PERM_VIEW_TRIGGER_INFO = 402;
		long PERM_ADD_JOB = 403;
		long PERM_EXEC_NATIVE_JOB = 500;
	}

	public static interface PERM_MISC {
		long PERM_ENTER_HOME_PAGE = 1000; // 进入主页
		long PERM_TEST_ANY = 1001;// 测试

	}

	public static final long SYSTEM_USER_ROLE = 0;// 系统普通用户：拥有系统默认权限配置

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

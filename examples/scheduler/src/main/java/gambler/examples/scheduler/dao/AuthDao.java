package gambler.examples.scheduler.dao;

import gambler.examples.scheduler.domain.auth.Permission;
import gambler.examples.scheduler.domain.auth.Role;
import gambler.examples.scheduler.domain.auth.RolePermission;
import gambler.examples.scheduler.domain.auth.User;
import gambler.examples.scheduler.domain.auth.UserPermission;
import gambler.examples.scheduler.domain.auth.UserRole;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface AuthDao {

	User find(String userId);

	int save(User user);

	int updatePassword(User user);
	
	int updateUserActiveFlag(User user);
	
	int updateUserSuperFlag(User user);
	
	int update(User user);

	int delete(String userId);

	List<Permission> getAllPermissions();
	
	List<Role> getAllRoles();

	List<UserRole> getUserRole(UserRole ur);

	List<RolePermission> getAllRolePermissions();

	List<UserPermission> getUserPermissions(long uid);

	UserPermission getUserPermission(UserPermission up);

	List<UserPermission> getUserRolePermission(UserPermission up);
	
	int createUserRole(UserRole ur);

	int createUserPermission(UserPermission up);

	int delUserPermission(UserPermission up);

	int delUserPermissions(long uid);

	int delUserRoles(long uid);
	
	int addPermission(Permission permission);
	
	int delPermission(long pid);
	
	int updatePermission(Permission permission);
	
	int delAllUserPermission(long pid);
	
	int delAllRolePermission(Map<String, Object> params);

	int addRole(Role role);
	
	int delRole(long rid);
	
	int updateRole(Role role);

	int createRolePermission(RolePermission rp);

	int delUserRole(UserRole userRole);

}

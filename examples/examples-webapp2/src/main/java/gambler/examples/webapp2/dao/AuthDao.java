package gambler.examples.webapp2.dao;

import gambler.examples.webapp2.domain.auth.Permission;
import gambler.examples.webapp2.domain.auth.RolePermission;
import gambler.examples.webapp2.domain.auth.User;
import gambler.examples.webapp2.domain.auth.UserPermission;
import gambler.examples.webapp2.domain.auth.UserRole;

import java.util.List;

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

	List<UserRole> getUserRoles(long uid);

	List<RolePermission> getAllRolePermissions();

	List<UserPermission> getUserPermissions(long uid);

	UserPermission getUserPermission(UserPermission up);

	int createUserRole(UserRole ur);

	int createUserPermission(UserPermission up);

	int delUserPermission(UserPermission up);

	int delUserPermissions(long uid);

	int delUserRoles(long uid);
	
	int addPermission(Permission permission);
	
	int delPermission(long pid);
	
	int updatePermission(Permission permission);
	
	int delAllUserPermission(long pid);
	
	int delAllRolePermission(long pid);
}

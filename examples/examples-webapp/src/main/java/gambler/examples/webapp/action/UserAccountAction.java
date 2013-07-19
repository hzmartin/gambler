package gambler.examples.webapp.action;

import gambler.commons.persistence.PersistenceException;
import gambler.examples.webapp.domain.UserAccount;
import gambler.examples.webapp.exception.UserAccountAlreadyExistException;
import gambler.examples.webapp.exception.UserAccountNotFoundException;
import gambler.examples.webapp.service.UserAccountService;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ActionSupport;

@ParentPackage("json-default")
@Namespace("/user")
public class UserAccountAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	private String username;

	private String password;

	@Resource
	private UserAccountService userAccountService;

	@Action(value = "/create", results = {
			@Result(name = SUCCESS, type = "json"),
			@Result(name = INPUT, location = "/WEB-INF/dashboard/user/create.jsp") })
	public String create() {
		UserAccount user = constructUser();
		try {
			user = userAccountService.save(user);
		} catch (UserAccountAlreadyExistException e) {
			return INPUT;
		} catch (PersistenceException e) {
			return INPUT;
		} catch (Exception e) {
			return INPUT;
		}
		return SUCCESS;
	}

	@Action(value = "/login", results = {
			@Result(name = SUCCESS, location = "/WEB-INF/dashboard/user/success.jsp"),
			@Result(name = ERROR, location = "/WEB-INF/dashboard/user/error.jsp"),
			@Result(name = INPUT, location = "/WEB-INF/dashboard/user/index.jsp") })
	public String login() {
		UserAccount user = constructUser();
		try {
			boolean isValid = userAccountService.validate(user);
			if (isValid) {
				return SUCCESS;
			}
		} catch (PersistenceException e) {
			return INPUT;
		} catch (UserAccountNotFoundException e) {
			return INPUT;
		}

		return INPUT;
	}

	private UserAccount constructUser() {
		UserAccount user = new UserAccount();
		user.setName(getUsername());
		user.setPassword(getPassword());
		return user;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
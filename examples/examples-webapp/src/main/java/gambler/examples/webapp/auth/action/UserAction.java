package gambler.examples.webapp.auth.action;

import gambler.commons.auth.User;
import gambler.commons.persistence.PersistenceException;
import gambler.examples.webapp.GBActionSupport;
import gambler.examples.webapp.auth.UserAlreadyExistException;
import gambler.examples.webapp.auth.UserNotFoundException;
import gambler.examples.webapp.auth.service.UserService;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

@Namespace("/user")
public class UserAction extends GBActionSupport {

	private static final long serialVersionUID = 1L;

	private User user;

	private String nextUrl;

	@Resource
	private UserService userService;

	@Action(value = "/create", results = {
			@Result(name = SUCCESS, location = "/WEB-INF/dashboard/user/success.jsp"),
			@Result(name = SYSERROR, location = "/error500.jsp"),
			@Result(name = INPUT, location = "/WEB-INF/dashboard/user/create.jsp") })
	public String create() {
		if (getUser() == null) {
			return INPUT;
		}
		try {
			userService.save(getUser());
		} catch (UserAlreadyExistException e) {
			addActionError(getText("user.alreadyexist",
					new String[] { getUser().getUserId() }));
			return INPUT;
		} catch (PersistenceException e) {
			addActionError(getText("unexpected.error"));
			return SYSERROR;
		} catch (Exception e) {
			addActionError(getText("unexpected.error"));
			return SYSERROR;
		}
		return SUCCESS;
	}

	@Action(value = "/login", results = {
			@Result(name = SUCCESS, location = "/WEB-INF/dashboard/user/success.jsp"),
			@Result(name = SYSERROR, location = "/error500.jsp"),
			@Result(name = INPUT, location = "/WEB-INF/dashboard/user/index.jsp") })
	public String login() {
		try {
			boolean isValid = userService.validate(getUser());
			if (isValid) {
				return SUCCESS;
			}
		} catch (PersistenceException e) {
			addActionError(getText("unexpected.error"));
			return SYSERROR;
		} catch (UserNotFoundException e) {
			addActionError(getText("user.notfound", new String[] { getUser()
					.getUserId() }));
			return INPUT;
		} catch (Exception e) {
			addActionError(getText("unexpected.error"));
			return SYSERROR;
		}

		return INPUT;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getNextUrl() {
		return nextUrl;
	}

	public void setNextUrl(String nextUrl) {
		this.nextUrl = nextUrl;
	}

}
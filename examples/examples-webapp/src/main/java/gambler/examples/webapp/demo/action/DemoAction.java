package gambler.examples.webapp.demo.action;

import gambler.examples.webapp.GBActionSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

@SuppressWarnings("serial")
@ParentPackage("json-default")
@Namespace("/demo")
public class DemoAction extends GBActionSupport {

	private String username;
	
	private String errorMessage;

	@Action(value = "/json", results = {
			@Result(name = SUCCESS, type = "json"),
			@Result(name = ERROR, type = "json") })
	public String validateUsername() {
		if (StringUtils.isEmpty(getUsername())) {
			setErrorMessage("user name required!");
			return ERROR;
		}
		return SUCCESS;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
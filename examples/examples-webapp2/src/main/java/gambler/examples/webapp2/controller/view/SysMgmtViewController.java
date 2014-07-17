package gambler.examples.webapp2.controller.view;

import gambler.examples.webapp2.annotation.AuthRequired;
import gambler.examples.webapp2.constant.AuthConstants;
import gambler.examples.webapp2.controller.AbstractController;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Class MiscController
 * 
 * @author hzwangqh
 * @version 2013-10-18
 */
@Controller
@RequestMapping("/sysmgmtview")
public class SysMgmtViewController extends AbstractController {

	@RequestMapping(value = "/sysmgmt")
	@AuthRequired(permission = { AuthConstants.PERM_SYSTEM.PERM_LIST_USER })
	public Object sysmgmt(final HttpServletRequest request) {
		return new ModelAndView();
	}
	
	@RequestMapping(value = "/listusers")
	@AuthRequired(permission = { AuthConstants.PERM_SYSTEM.PERM_LIST_USER })
	public Object listusers(final HttpServletRequest request) {
		return new ModelAndView();
	}

}

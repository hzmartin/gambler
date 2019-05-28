package gambler.examples.scheduler.controller.view;

import java.util.ArrayList;
import java.util.List;

import gambler.examples.scheduler.annotation.AuthRequired;
import gambler.examples.scheduler.constant.AuthConstants;
import gambler.examples.scheduler.controller.AbstractController;
import gambler.examples.scheduler.domain.auth.User;
import gambler.examples.scheduler.dto.AccountDto;
import gambler.examples.scheduler.dto.NaviItemDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Class MiscController
 * 
 * @author hzwangqh
 * @version 2013-10-18
 */
@Controller
public class MiscViewController extends AbstractController {

	@RequestMapping(value = "/signin")
	public Object signin(final HttpServletRequest request,
			final HttpServletResponse response, String nextUrl) {
		return new ModelAndView("signin", "nextUrl",
				nextUrl == null ? "index.do" : nextUrl);
	}

	@RequestMapping(value = "/index")
	@AuthRequired(permission = { AuthConstants.PERM_MISC.PERM_ENTER_HOME_PAGE })
	public Object index(final HttpServletRequest request,
			final HttpServletResponse response) {
		ModelAndView result1 = new ModelAndView("index");
		result1.getModel().put("environment", sysconf.getString("environment"));
		AccountDto loginUser = authUserService.getLoginUser(request);
		User user = authUserService.findUserById(loginUser.getUserId());
		if (loginUser != null) {
			List<NaviItemDto> menus = new ArrayList<NaviItemDto>();
			int count = sysconf.getInteger("mainnav.count", 0);
			for (int nidx = 1; nidx <= count; nidx++) {
				String name = sysconf.getString("mainnav.name." + nidx);
				String url = sysconf.getString("mainnav.url." + nidx);
				if (StringUtils.isBlank(name) || StringUtils.isBlank(url)) {
					continue;
				}
				Long pid = sysconf.getLong("mainnav.perm." + nidx);
				if (pid != null) {
					// check nav item perm
					boolean hasThisPerm = authUserService.checkUserPermission(
							user, pid);
					if (hasThisPerm) {
						// passed
						menus.add(new NaviItemDto(name, url));
					}
				} else {
					// no perm required
					menus.add(new NaviItemDto(name, url));
				}
			}

			result1.getModel().put("mainnav", menus);
		}
		return result1;
	}

	@RequestMapping(value = "/view")
	public ModelAndView view(final HttpServletRequest request,
			@RequestParam(required = false) String name) {
		return new ModelAndView(name);
	}

	@RequestMapping(value = "/404")
	public ModelAndView pagenotfound(final HttpServletRequest request) {
		return new ModelAndView("404");
	}

	@RequestMapping(value = "/403")
	public ModelAndView forbidden(final HttpServletRequest request) {
		return new ModelAndView("403");
	}

	@RequestMapping(value = "/500")
	public ModelAndView serverbusy(final HttpServletRequest request) {
		return new ModelAndView("500");
	}

}

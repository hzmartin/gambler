package gambler.examples.webapp2.controller;

import gambler.examples.webapp2.domain.AuthUser;
import gambler.examples.webapp2.service.AuthUserService;
import gambler.examples.webapp2.util.SpringContextHolder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/demo")
public class DemoController {

	@Resource
	private AuthUserService userService;

	@RequestMapping("/welcome")
	public ModelAndView welcome(final HttpServletRequest request,
			@RequestParam(required = true) String message) {
		return new ModelAndView("welcome", "message", message);
	}

	/**
	 * sample request: /demo/echo.do?message=xxx
	 */
	@RequestMapping(value = "/echo", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ResponseEntity<String> echo(final HttpServletRequest request,
			@RequestParam(required = true) String message) {
		return new ResponseEntity<String>("[echo]" + message, HttpStatus.OK);
	}

	/**
	 * sample request: /demo/find.do?userId=xxx
	 * 
	 */
	@RequestMapping("/hello")
	public ModelAndView find(final HttpServletRequest request, Model model,
			@RequestParam(required = true) String userId) {
		AuthUser user = userService.findUserById(userId);
		model.addAttribute("firstName", user.getFirstName());
		return new ModelAndView("demo");
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseBody
	public Object search(final HttpServletRequest request,
			final HttpServletResponse response, AuthUser user) {
		AuthUserService userService = SpringContextHolder
				.getBean("authUserService");
		return userService.findUserById(user.getUserId());
	}

	@RequestMapping(value = "/save", method = RequestMethod.GET)
	@ResponseBody
	public Object save(final HttpServletRequest request,
			final HttpServletResponse response, AuthUser user) {
		return userService.save(user);
	}
}
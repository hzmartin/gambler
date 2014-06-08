package gambler.examples.webapp2.controller;

import gambler.examples.webapp2.domain.User;
import gambler.examples.webapp2.service.UserService;

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
public class HelloController {

	@Resource
	private UserService userService;

	@RequestMapping("/hello")
	public ModelAndView helloWorld() {
		String message = "Hello World<br/>";
		return new ModelAndView("hello", "message", message);
	}

	/**
	 * sample request: /demo/echo.do?message=xxx
	 */
	@RequestMapping(value = "/echo", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ResponseEntity<String> echo(
			@RequestParam(required = true) String message) {
		return new ResponseEntity<String>("[echo]" + message, HttpStatus.OK);
	}

	/**
	 * sample request: /demo/find.do?userId=xxx
	 * 
	 */
	@RequestMapping("/find")
	public String find(Model model, User user)  {
		userService.findUserById(user.getUserId());
		return "welcome";
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseBody
	public Object search(final HttpServletRequest request,
			final HttpServletResponse response, User user)
			 {
		return userService.findUserById(user.getUserId());
	}

	@RequestMapping(value = "/save", method = RequestMethod.GET)
	@ResponseBody
	public Object save(final HttpServletRequest request,
			final HttpServletResponse response, User user)  {
		userService.save(user);
		return "OK";
	}
}
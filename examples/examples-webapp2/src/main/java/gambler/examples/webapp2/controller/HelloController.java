package gambler.examples.webapp2.controller;

import gambler.commons.auth.User;
import gambler.examples.webapp2.dao.UserDao;
import gambler.examples.webapp2.dwr.DemoMessage;

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
	private UserDao userDao;

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
	 */
	@RequestMapping("/find")
	public String find(Model model, User user) {
		User foundUser = userDao.findUserById(user.getUserId());
		model.addAttribute("message",
				foundUser.getUserId() + ":" + foundUser.getFirstName());
		return "welcome";
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseBody
	public Object search(final HttpServletRequest request,
			final HttpServletResponse response) {
		return new DemoMessage("demo json response");
	}
}
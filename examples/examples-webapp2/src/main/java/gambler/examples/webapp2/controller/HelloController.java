package gambler.examples.webapp2.controller;

import gambler.commons.auth.User;
import gambler.examples.webapp2.dao.UserDao;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/demo")
public class HelloController {

	@Resource
	private UserDao userDao;

	@RequestMapping("/welcome")
	public ModelAndView helloWorld() {
		String message = "Hello World<br/>";
		return new ModelAndView("welcome", "message", message);
	}

	@RequestMapping(value = "/echo", method = { RequestMethod.POST,
			RequestMethod.GET })
	public ResponseEntity<String> echo(
			@RequestParam(required = true) String message) {
		return new ResponseEntity<String>("[echo]" + message, HttpStatus.OK);
	}

	@RequestMapping("/findUser")
	public String findUserById(Model model, User user) {
		User foundUser = userDao.findUserById(user.getUserId());
		model.addAttribute("message", foundUser.getUserId() + ":" + foundUser.getFirstName());
		return "welcome";
	}
}
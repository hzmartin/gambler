package gambler.examples.webapp2.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

	public static final String USERNAME_SESSION_KEY = "username";

	@RequestMapping("/signin")
	public ModelAndView signin() {
		return new ModelAndView("signin");
	}

	@RequestMapping(value = "/login")
	public ModelAndView login(final HttpSession session,
			@RequestParam(required = true) String username,
			@RequestParam(required = true) String password) {
		if ("yixin".equals(username) && "yixin1016".equals(password)) {
			session.setAttribute(USERNAME_SESSION_KEY, username);
			return new ModelAndView("index", "user", username);
		} else {
			return new ModelAndView("signin");
		}
	}
}
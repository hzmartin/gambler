package gambler.examples.webapp2.controller;

import gambler.examples.webapp2.annotation.AuthRequired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
public class MiscViewController {

	@RequestMapping(value = "/signin")
	public Object signin(final HttpServletRequest request,
			final HttpServletResponse response, String nextUrl) {
		return new ModelAndView("signin", "nextUrl",
				nextUrl == null ? "index.do" : nextUrl);
	}

	@RequestMapping(value = "/index")
	@AuthRequired()
	public Object index(final HttpServletRequest request,
			final HttpServletResponse response) {
		return new ModelAndView("index");
	}

	@RequestMapping(value = "/404")
	public ModelAndView pagenotfound(final HttpServletRequest request,
			final String name) {
		return new ModelAndView("404");
	}

	@RequestMapping(value = "/403")
	public ModelAndView forbidden(final HttpServletRequest request,
			final String name) {
		return new ModelAndView("403");
	}

	@RequestMapping(value = "/500")
	public ModelAndView serverbusy(final HttpServletRequest request,
			final String name) {
		return new ModelAndView("500");
	}

	@RequestMapping(value = "/view")
	@AuthRequired()
	public ModelAndView view(final HttpServletRequest request, final String name) {
		return new ModelAndView(name);
	}

}

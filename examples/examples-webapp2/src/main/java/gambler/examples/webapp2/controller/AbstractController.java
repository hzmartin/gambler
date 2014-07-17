package gambler.examples.webapp2.controller;

import javax.annotation.Resource;

import gambler.commons.advmap.XMLMap;
import gambler.examples.webapp2.service.AuthUserService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractController {

	protected final Logger logger = Logger.getLogger(getClass());

	@Autowired
	protected XMLMap sysconf;
	
	@Resource
	protected AuthUserService authUserService;
}

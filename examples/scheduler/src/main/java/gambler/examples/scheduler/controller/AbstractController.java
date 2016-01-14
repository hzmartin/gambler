package gambler.examples.scheduler.controller;

import javax.annotation.Resource;

import gambler.commons.advmap.XMLMap;
import gambler.examples.scheduler.service.AuthUserService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractController {

	protected final Logger logger = Logger.getLogger(getClass());

	@Autowired
	protected XMLMap sysconf;
	
	@Resource
	protected AuthUserService authUserService;
}

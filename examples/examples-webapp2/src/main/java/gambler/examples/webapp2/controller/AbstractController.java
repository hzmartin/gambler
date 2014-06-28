package gambler.examples.webapp2.controller;

import gambler.commons.advmap.XMLMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractController {

	protected final Logger logger = Logger.getLogger(getClass());

	@Autowired
	protected XMLMap sysconf;
}

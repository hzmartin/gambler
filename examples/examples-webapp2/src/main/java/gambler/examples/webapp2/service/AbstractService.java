package gambler.examples.webapp2.service;

import gambler.commons.advmap.XMLMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractService {

	protected static final Logger logger = Logger
			.getLogger(AuthUserService.class);

	@Autowired
	protected XMLMap sysconf;
}
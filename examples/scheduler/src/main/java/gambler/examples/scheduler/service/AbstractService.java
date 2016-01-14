package gambler.examples.scheduler.service;

import gambler.commons.advmap.XMLMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractService {

	protected static final Logger logger = Logger
			.getLogger(AbstractService.class);

	@Autowired
	protected XMLMap sysconf;
}

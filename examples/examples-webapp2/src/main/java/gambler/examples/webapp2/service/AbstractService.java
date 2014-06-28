package gambler.examples.webapp2.service;

import gambler.commons.advmap.XMLMap;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractService {

	@Autowired
	protected XMLMap sysconf;
}

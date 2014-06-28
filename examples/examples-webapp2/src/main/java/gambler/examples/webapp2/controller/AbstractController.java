package gambler.examples.webapp2.controller;

import gambler.commons.advmap.XMLMap;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractController {

	@Autowired
	protected XMLMap sysconf;
}

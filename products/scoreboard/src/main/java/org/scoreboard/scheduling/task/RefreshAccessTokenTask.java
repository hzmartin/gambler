package org.scoreboard.scheduling.task;

import org.apache.log4j.Logger;

public class RefreshAccessTokenTask {

	private static final Logger logger = Logger
			.getLogger(RefreshAccessTokenTask.class);

	public final void refresh() {
		try {
			logger.info("refresh access token");
		} catch (Exception e) {
			logger.error("refresh access token error!", e);
		}
	}

}

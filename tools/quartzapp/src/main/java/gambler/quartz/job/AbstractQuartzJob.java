/**
 * @(#)AbstractQuartzJob.java, 2014-3-25.
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package gambler.quartz.job;

import org.apache.log4j.Logger;
import org.quartz.Job;

/**
 * @author hzwangqh
 */
public abstract class AbstractQuartzJob implements Job {

	protected final Logger log = Logger.getLogger(getClass());

}

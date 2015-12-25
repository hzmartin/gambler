package jobs;

import gambler.commons.advmap.AdvancedKey;

import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;

public class LoadSysConfigJob extends AbstractJob {

	private final Logger log = Logger.getLogger(getClass());

	@Override
	protected void process(JobExecutionContext jobexecutioncontext)
			throws Exception {
		sysconf.load();
		log.info("sysconf loaded");
		Set<Entry<AdvancedKey, String>> entrySet = sysconf.entrySet();
		for (Entry<AdvancedKey, String> entry : entrySet) {
			log.info(entry.getKey() + "=" + entry.getValue());
		}
	}

}

package gambler.quartz.job;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import gambler.quartz.utils.TimeTagUtil;

/**
 * 定期清理文件系统
 * 
 * @author hzwangqh
 */
public class FSCleanupJob extends AbstractQuartzJob {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		final JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		int count = jobDataMap.getIntValue("fscleanup.dir.count");
		for (int j = 0; j < count; j++) {
			int index = j + 1;
			String dirpath = jobDataMap.getString("fscleanup.dir." + index);

			String extension = jobDataMap.getString("fscleanup.dir." + index + ".ext");
			if (StringUtils.isBlank(extension)) {
				log.info("please config ext first, for any, set fscleanup.dir." + index + ".ext"
						+ " = * ");
				continue;
			}
			List<String> exts = new ArrayList<String>();
			if (StringUtils.isNotBlank(extension)) {
				String[] es = extension.trim().split(",");
				for (String e : es) {
					exts.add(e.trim());
				}
			}

			int expireDays = jobDataMap.getIntValue("fscleanup.expire.days");
			if (StringUtils.isBlank(dirpath)) {
				continue;
			}
			File dirRoot = new File(dirpath);
			if (!dirRoot.exists()) {
				log.info("ignore dir " + dirpath + "!");
				continue;
			}
			File[] subfiles = dirRoot.listFiles();
			for (File file : subfiles) {
				if (!extension.trim().equals("*")) {
					boolean skip = true;
					for (String ext : exts) {
						if (file.getName().endsWith(ext)) {
							skip = false;
						}
					}
					if (skip) {
						log.info("skip file " + file.getAbsolutePath() + ", ext restrict: " + extension);
						continue;
					}
				}
				if (System.currentTimeMillis() - file.lastModified() > TimeTagUtil.ONE_DAY * expireDays) {
					if (file.isDirectory()) {
						deleteDirRecursively(file);
					} else {
						file.delete();
						log.info(file.getAbsolutePath() + " deleted!");
					}

				}
			}
		}

		log.info("fs cleanup done!");

	}

	private void deleteDirRecursively(File targetDir) {
		if (!targetDir.isDirectory()) {
			return;
		}
		log.info("processing dir " + targetDir.getAbsolutePath() + " ... ");
		File[] outputs = targetDir.listFiles();
		for (File file : outputs) {
			if (file.isDirectory()) {
				deleteDirRecursively(file);
			} else {
				file.delete();
				log.info(file.getAbsolutePath() + " deleted!");
			}
		}
		targetDir.delete();
		log.info(targetDir.getAbsolutePath() + " deleted!");
	}

}

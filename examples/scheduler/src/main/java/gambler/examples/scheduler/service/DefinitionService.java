package gambler.examples.scheduler.service;

import gambler.examples.scheduler.dto.JobDefinitionDto;
import gambler.examples.scheduler.dto.JobParameterDto;

import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class DefinitionService extends AbstractService {

    private Map<String, JobDefinitionDto> definitionMap = new TreeMap<String, JobDefinitionDto>();

    public DefinitionService() {

    }

    public JobDefinitionDto getDefinition(String jobName) {
        return (JobDefinitionDto) definitionMap.get(jobName);
    }

    public Map<String, JobDefinitionDto> getDefinitionMap() {
        Map<String, JobDefinitionDto> localJobDefMap = new TreeMap<String, JobDefinitionDto>();
        int jobCount = sysconf.getInteger("job_definition.count", 0);
        for (int jobIndex = 1; jobIndex <= jobCount; jobIndex++) {
            String jobName = sysconf.getString("job_definition.job_name."
                    + jobIndex);
            String jobClass = sysconf.getString("job_definition.job_class."
                    + jobIndex);
            if (StringUtils.isBlank(jobName)
                    || StringUtils.isBlank(jobClass)) {
                continue;
            }
            Long pid = sysconf.getLong("job_definition.job_perm." + jobIndex);
            String jobDesc = sysconf.getString("job_definition.job_description."
                    + jobIndex);
            JobDefinitionDto def = new JobDefinitionDto(jobName, jobClass, pid, jobDesc);
            int paramCount = sysconf.getInteger("job_definition.job_param_count." + jobIndex, 0);
            for (int paramIndex = 1; paramIndex <= paramCount; paramIndex++) {
                String paramName = sysconf.getString("job_definition.job_param_name_of_" + jobName + "."
                        + paramIndex);
                if (StringUtils.isBlank(paramName)) {
                    continue;
                }
                String paramDesc = sysconf.getString("job_definition.job_param_description_of_" + jobName + "."
                        + paramIndex);
                Boolean paramRequired = sysconf.getBoolean("job_definition.job_param_required_of_" + jobName + "."
                        + paramIndex, false);
                JobParameterDto param = new JobParameterDto(paramName, paramDesc, paramRequired);
                def.addParameter(param);
            }
            localJobDefMap.put(jobName, def);
        }
        this.definitionMap = localJobDefMap;
        return this.definitionMap;
    }

}

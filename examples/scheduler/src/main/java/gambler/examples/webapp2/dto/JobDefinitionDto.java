package gambler.examples.webapp2.dto;

import java.util.ArrayList;
import java.util.List;

public class JobDefinitionDto {

    private String name;
    private String description;
    private String className;
    private Long pid;

    private final List<JobParameterDto> parameters = new ArrayList<JobParameterDto>();

    public JobDefinitionDto() {

    }

    public JobDefinitionDto(String jobName, String cName, Long pid, String desc) {
        this.name = jobName;
        this.className = cName;
        this.pid = pid;
        this.description = desc;

    }

    /**
     * @return
     */
    public String getClassName() {
        return className;
    }

    /**
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return
     */
    public List<JobParameterDto> getParameters() {
        return parameters;
    }

    /**
     * @param string
     */
    public void setClassName(String string) {
        className = string;
    }

    /**
     * @param string
     */
    public void setDescription(String string) {
        description = string;
    }

    public void addParameter(JobParameterDto param) {
        this.parameters.add(param);

    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @param string
     */
    public void setName(String string) {
        name = string;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }
}

package gambler.examples.webapp2.service;

import gambler.examples.webapp2.dto.JobDefinitionDto;

import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

@Service
public class DefinitionService extends AbstractService{

	private Map<String, JobDefinitionDto> definitionMap = new TreeMap<String, JobDefinitionDto>();

	public DefinitionService() {
		
	}

	public JobDefinitionDto getDefinition(String jobName) {
		return (JobDefinitionDto) definitionMap.get(jobName);
	}
	
	public Map<String, JobDefinitionDto> getDefinitionMap() {
		return definitionMap;
	}
	

}

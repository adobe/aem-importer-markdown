package io.adobe.udp.markdownimporter;

import java.util.Map;

public class TemplateMapper {
	
	private Map<String, TemplateMapping> mappings;
	private TemplateMapping defaultMapping;
	
	public TemplateMapper(Map<String, TemplateMapping> mappings, TemplateMapping defaultMapping) {
		this.mappings = mappings;
		this.defaultMapping = defaultMapping;
	}
	
	public TemplateMapping getMapping(String name) {
		TemplateMapping mapping = mappings.get(name);
		if(mapping != null) {
			return mapping;
		}
		return defaultMapping;
	}

}

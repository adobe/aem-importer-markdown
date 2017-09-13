package io.adobe.udp.markdownimporter;

public class TemplateMapping {
	
	private String name;
	private String template;
	private String resourceType;
	
	public TemplateMapping(String name, String template, String resourceType) {
		this.name = name;
		this.template = template;
		this.resourceType = resourceType;
	}

	public String getName() {
		return name;
	}

	public String getTemplate() {
		return template;
	}

	public String getResourceType() {
		return resourceType;
	}

}

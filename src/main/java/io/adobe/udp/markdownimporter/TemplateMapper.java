/**
 * Copyright 2017 Adobe Systems Incorporated. All rights reserved.
 * This file is licensed to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy
 * of the License at http://www.apache.org/licenses/LICENSE-2.0
 */
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

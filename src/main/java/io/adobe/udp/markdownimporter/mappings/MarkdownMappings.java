/**
 * Copyright 2017 Adobe Systems Incorporated. All rights reserved.
 * This file is licensed to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy
 * of the License at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.adobe.udp.markdownimporter.mappings;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import com.vladsch.flexmark.ast.Node;

public  class MarkdownMappings {
	
	public static final Map<Class, MarkdownNodeMapper> markdownMapping = new HashMap<>();
	private static final MarkdownNodeMapper defaultMapper = new ParagraphsMergingMapper();
	
	public static void configure(Map<String, String> mappings) {
		for(Map.Entry<String, String> entry : mappings.entrySet()) {
			try {
				Constructor<MarkdownNodeMapper> mappingConstructor = (Constructor<MarkdownNodeMapper>) Class.forName(entry.getValue()).getConstructor();
				MarkdownMappings.markdownMapping.put(Class.forName(entry.getKey()), mappingConstructor.newInstance());
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	public static MarkdownNodeMapper getMarkdownNodeMapper(com.vladsch.flexmark.ast.Node node) {
		MarkdownNodeMapper mappedMapper = markdownMapping.get(node.getClass());
		if( mappedMapper != null) {
			return mappedMapper;
		}
		return MarkdownMappings.defaultMapper;
	}
	
	public static  boolean hasMapping(Node node) {
		return markdownMapping.get(node.getClass()) != null;
	}


}

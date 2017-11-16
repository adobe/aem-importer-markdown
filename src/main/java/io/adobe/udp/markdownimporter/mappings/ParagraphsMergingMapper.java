/**
 * Copyright 2017 Adobe Systems Incorporated. All rights reserved.
 * This file is licensed to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy
 * of the License at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.adobe.udp.markdownimporter.mappings;

import io.adobe.udp.markdownimporter.MarkdownPageData;
import io.adobe.udp.markdownimporter.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.jcr.RepositoryException;

import org.apache.jackrabbit.JcrConstants;

import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;


public class ParagraphsMergingMapper implements MarkdownNodeMapper {
	
	@SuppressWarnings({ "rawtypes", "serial" })
	public static final List<Class> NON_PARAGRAPH_NODES = new ArrayList<Class>() {
		{
			add(com.vladsch.flexmark.ast.Heading.class);
			add(com.vladsch.flexmark.ast.FencedCodeBlock.class);
			add(com.vladsch.flexmark.ext.tables.TableBlock.class);
		}
	};

	public Node mapToComponen(Node markdownNode, MarkdownPageData pageData,
			Parser parser, HtmlRenderer renderer) throws RepositoryException {
		StringBuilder paragraphHtml = new StringBuilder();
		while(markdownNode != null && isParagraphNode(markdownNode)) {
			String elementHtml = renderer.render(parser.parse(markdownNode.getChars()));
			paragraphHtml.append(elementHtml);
			markdownNode = markdownNode.getNext();
		}
		HashMap<String, String> component = new HashMap<String, String>();
		component.put(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_UNSTRUCTURED);
		component.put("text", paragraphHtml.toString());
		component.put(Constants.SLING_RESOURCE_TYPE, "wcm/foundation/components/text");
		component.put("textIsRich", "true");
		pageData.getComponents().add(component);
		return markdownNode;
	}

	private boolean isParagraphNode(Node markdownNode) {
		return !MarkdownMappings.hasMapping(markdownNode);
	}

	

}

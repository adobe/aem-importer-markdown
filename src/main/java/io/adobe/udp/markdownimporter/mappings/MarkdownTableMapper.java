/**
 * Copyright 2017 Adobe Systems Incorporated. All rights reserved.
 * This file is licensed to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy
 * of the License at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.adobe.udp.markdownimporter.mappings;

import java.util.HashMap;

import io.adobe.udp.markdownimporter.MarkdownPageData;
import io.adobe.udp.markdownimporter.utils.Constants;

import javax.jcr.RepositoryException;

import org.apache.jackrabbit.JcrConstants;

import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;

public class MarkdownTableMapper implements MarkdownNodeMapper {

	public Node mapToComponen(Node markdownNode, MarkdownPageData pageData, Parser parser, HtmlRenderer renderer) throws RepositoryException {
		String elementHtml = renderer.render(parser.parse(markdownNode.getChars()));
		HashMap<String, String> component = new HashMap<>();
		component.put(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_UNSTRUCTURED);
		component.put("tableDataHTML", elementHtml);
		component.put(Constants.SLING_RESOURCE_TYPE, "dev/components/table");
		pageData.getComponents().add(component);
		return markdownNode.getNext();
	}

}

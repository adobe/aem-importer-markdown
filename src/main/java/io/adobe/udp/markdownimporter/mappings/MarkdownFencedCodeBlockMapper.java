/**
 * Copyright 2017 Adobe Systems Incorporated. All rights reserved.
 * This file is licensed to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy
 * of the License at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.adobe.udp.markdownimporter.mappings;

import io.adobe.udp.markdownimporter.MarkdownPageData;
import io.adobe.udp.markdownimporter.utils.Constants;

import java.util.HashMap;

import javax.jcr.RepositoryException;

import org.apache.jackrabbit.JcrConstants;

import com.vladsch.flexmark.ast.FencedCodeBlock;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;

public class MarkdownFencedCodeBlockMapper implements MarkdownNodeMapper {

	public com.vladsch.flexmark.ast.Node mapToComponen(com.vladsch.flexmark.ast.Node markdownNode,
			MarkdownPageData pageData, Parser parser, HtmlRenderer renderer) throws RepositoryException {
		HashMap<String, String> component = new HashMap<>();
		String elementHtml = renderer.render(parser.parse(markdownNode.getChars()));
		elementHtml = elementHtml.replaceFirst("<pre><code.*>", "").replace("</code></pre>", "");
		component.put(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_UNSTRUCTURED);
		component.put("codeBlock", elementHtml);
		component.put("language", ((FencedCodeBlock) markdownNode).getInfo().toString());
		component.put(Constants.SLING_RESOURCE_TYPE, "udp/components/content/highlightedcodeblock");
		pageData.getComponents().add(component);
		return markdownNode.getNext();
	}

}

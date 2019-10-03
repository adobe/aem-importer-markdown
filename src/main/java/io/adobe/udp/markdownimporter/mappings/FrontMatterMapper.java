/**
 * Copyright 2017 Adobe Systems Incorporated. All rights reserved.
 * This file is licensed to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy
 * of the License at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.adobe.udp.markdownimporter.mappings;

import io.adobe.udp.markdownimporter.MarkdownPageData;
import io.adobe.udp.markdownimporter.utils.GithubConstants;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.RepositoryException;

import org.apache.commons.lang3.math.NumberUtils;

import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.front.matter.YamlFrontMatterNode;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;

public class FrontMatterMapper implements MarkdownNodeMapper {

	public Node mapToComponen(Node markdownNode, MarkdownPageData pageData,
			Parser parser, HtmlRenderer renderer) throws RepositoryException {
		List<String> properties = new ArrayList<>();
		Node entry = markdownNode.getFirstChild();
		while(entry != null) {
			YamlFrontMatterNode keyValue = (YamlFrontMatterNode) entry;
			if(keyValue.getValues().size() > 0) {
				if(keyValue.getKey().equals("title")) {
					pageData.setTitle(keyValue.getValues().toArray(new String[1])[0]);
				}
				if(keyValue.getKey().equals("template")) {
					pageData.setTemplateFromYaml(keyValue.getValues().toArray(new String[1])[0]);
				}
				if(keyValue.getKey().equals(GithubConstants.NAV_ORDER_PROPERTY)) {
					pageData.setNavOrder(NumberUtils.toLong(keyValue.getValues().toArray(new String[1])[0]));
				}
				properties.add(keyValue.getKey() + "=" + keyValue.getValues());
			}
			entry = entry.getNext();
		}
		if(properties.size() > 0) {
			pageData.getYamlProperties().addAll(properties);
		}
		return markdownNode.getNext();
	}
}

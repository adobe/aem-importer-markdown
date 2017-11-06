/**
 * Copyright 2017 Adobe Systems Incorporated. All rights reserved.
 * This file is licensed to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy
 * of the License at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.adobe.udp.markdownimporter.mappings;

import io.adobe.udp.markdownimporter.MarkdownPageData;

import javax.jcr.RepositoryException;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;

public interface MarkdownNodeMapper {
	
	com.vladsch.flexmark.ast.Node mapToComponen(com.vladsch.flexmark.ast.Node markdownNode,
			MarkdownPageData pageData, Parser parser, HtmlRenderer renderer) throws RepositoryException;

}

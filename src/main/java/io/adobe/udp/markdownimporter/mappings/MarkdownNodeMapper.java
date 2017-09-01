package io.adobe.udp.markdownimporter.mappings;

import io.adobe.udp.markdownimporter.MarkdownPageData;

import javax.jcr.RepositoryException;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;

public interface MarkdownNodeMapper {
	
	com.vladsch.flexmark.ast.Node mapToComponen(com.vladsch.flexmark.ast.Node markdownNode,
			MarkdownPageData pageData, Parser parser, HtmlRenderer renderer) throws RepositoryException;

}

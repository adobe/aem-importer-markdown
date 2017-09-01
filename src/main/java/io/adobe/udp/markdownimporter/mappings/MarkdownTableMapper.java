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
		HashMap<String, String> component = new HashMap<String, String>();
		component.put(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_UNSTRUCTURED);
		component.put("tableDataHTML", elementHtml);
		component.put(Constants.SLING_RESOURCE_TYPE, "dev/components/table");
		pageData.getComponents().add(component);
		return markdownNode.getNext();
	}

}

package io.adobe.udp.markdownimporter.mappings;

import java.util.HashMap;

import io.adobe.udp.markdownimporter.MarkdownPageData;
import io.adobe.udp.markdownimporter.utils.Constants;

import javax.jcr.RepositoryException;

import org.apache.jackrabbit.JcrConstants;
import org.jsoup.Jsoup;

import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;

public class MarkdownHeadlineMapper implements MarkdownNodeMapper {

	public Node mapToComponen(Node markdownNode, MarkdownPageData pageData, Parser parser, HtmlRenderer renderer) throws RepositoryException {
		String elementHtml = Jsoup.parse(renderer.render(markdownNode)).text();
		HashMap<String, String> component = new HashMap<String, String>();
		component.put(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_UNSTRUCTURED);
		component.put("jcr:title", elementHtml);
		component.put("type", "h" + ((Heading) markdownNode).getLevel() );
		component.put(Constants.SLING_RESOURCE_TYPE, "udp/components/content/title");
		pageData.getComponents().add(component);
		return markdownNode.getNext();
	}

}

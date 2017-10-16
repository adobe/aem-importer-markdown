package io.adobe.udp.markdownimporter.mappings;

import io.adobe.udp.markdownimporter.MarkdownPageData;
import io.adobe.udp.markdownimporter.utils.Constants;

import java.util.HashMap;

import javax.jcr.RepositoryException;

import org.apache.jackrabbit.JcrConstants;

import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;

public class MarkdownHeadlineMapper implements MarkdownNodeMapper {

	public Node mapToComponen(Node markdownNode, MarkdownPageData pageData, Parser parser, HtmlRenderer renderer) throws RepositoryException {
		HashMap<String, String> component = new HashMap<String, String>();
		component.put(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_UNSTRUCTURED);
		component.put("jcr:title", ((Heading) markdownNode).getText().toString());
		component.put("type", "h" + ((Heading) markdownNode).getLevel() );
		component.put("anchorId", ((Heading) markdownNode).getAnchorRefId());
		component.put(Constants.SLING_RESOURCE_TYPE, "udp/components/content/title");
		pageData.getComponents().add(component);
		return markdownNode.getNext();
	}

}

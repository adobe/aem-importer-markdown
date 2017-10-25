package io.adobe.udp.markdownimporter.mappings;

import io.adobe.udp.markdownimporter.MarkdownPageData;
import io.adobe.udp.markdownimporter.flexmarkExtensions.ImageUrlExtension;
import io.adobe.udp.markdownimporter.flexmarkExtensions.UrlPrefixer;
import io.adobe.udp.markdownimporter.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;

import javax.jcr.RepositoryException;

import org.apache.jackrabbit.JcrConstants;
import org.jsoup.Jsoup;

import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.ast.Link;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.anchorlink.AnchorLink;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.DataKey;

public class MarkdownHeadlineMapper implements MarkdownNodeMapper {

	public Node mapToComponen(Node markdownNode, MarkdownPageData pageData, Parser parser, HtmlRenderer renderer) throws RepositoryException {
		HashMap<String, String> component = new HashMap<String, String>();
		String content = getHeadlineContent(markdownNode, renderer, parser);
		component.put(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_UNSTRUCTURED);
		component.put("jcr:title", content);
		component.put("type", "h" + ((Heading) markdownNode).getLevel() );
		component.put("anchorId", ((Heading) markdownNode).getAnchorRefId());
		component.put(Constants.SLING_RESOURCE_TYPE, "udp/components/content/title");
		pageData.getComponents().add(component);
		return markdownNode.getNext();
	}

	private String getHeadlineContent(Node markdownNode, HtmlRenderer renderer, Parser parser) {
		Node anchor = markdownNode.getFirstChild();
		if(anchor != null && anchor instanceof AnchorLink) {
			Node link = anchor.getFirstChild();
			if(link != null && link instanceof Link) {
				String html = renderer.render(parser.parse(link.getChars()));
				return Jsoup.parse(html).select("a").get(0).outerHtml();
			}
		}
		return ((Heading) markdownNode).getText().toString();
	}

}

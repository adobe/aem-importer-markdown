package io.adobe.udp.markdownimporter.mappings;

import io.adobe.udp.markdownimporter.MarkdownPageData;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.RepositoryException;

import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.front.matter.YamlFrontMatterNode;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;

public class FrontMatterMapper implements MarkdownNodeMapper {

	public Node mapToComponen(Node markdownNode, MarkdownPageData pageData,
			Parser parser, HtmlRenderer renderer) throws RepositoryException {
		List<String> properties = new ArrayList<String>();
		Node entry = markdownNode.getFirstChild();
		while(entry != null) {
			YamlFrontMatterNode keyValue = (YamlFrontMatterNode) entry;
			if(keyValue.getValues().size() > 0) {
				if(keyValue.getKey().equals("title")) {
					pageData.setTitle(keyValue.getValues().toArray(new String[1])[0]);
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

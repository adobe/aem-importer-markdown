package io.adobe.udp.markdownimporter.services;

import io.adobe.udp.markdownimporter.MarkdownPageData;
import io.adobe.udp.markdownimporter.flexmarkExtensions.GithubHostedImagePrefixer;
import io.adobe.udp.markdownimporter.flexmarkExtensions.ImageUrlExtension;
import io.adobe.udp.markdownimporter.flexmarkExtensions.ImageVisitor;
import io.adobe.udp.markdownimporter.flexmarkExtensions.UdpUrlExtension;
import io.adobe.udp.markdownimporter.mappings.FrontMatterMapper;
import io.adobe.udp.markdownimporter.mappings.MarkdownMappings;
import io.adobe.udp.markdownimporter.mappings.MarkdownNodeMapper;

import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.jcr.RepositoryException;

import org.apache.commons.io.IOUtils;

import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.ast.util.HeadingCollectingVisitor;
import com.vladsch.flexmark.ext.anchorlink.AnchorLinkExtension;
import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.front.matter.YamlFrontMatterBlock;
import com.vladsch.flexmark.ext.front.matter.YamlFrontMatterExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataHolder;
import com.vladsch.flexmark.util.options.MutableDataSet;


public class MarkdownParserServiceImpl implements MarkdownParserService {
	

	public  MarkdownPageData parseMarkdownFile(Reader file, MarkdownPageData pageData, List<String> urls,
			GithubHostedImagePrefixer urlPrefixer) {
		try {
			new HashMap<String, String>();
			 MutableDataHolder options = new MutableDataSet();
	        options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), ImageUrlExtension.create(), YamlFrontMatterExtension.create(),
	        	StrikethroughExtension.create(), AutolinkExtension.create(), UdpUrlExtension.create(), AnchorLinkExtension.create()));
	        options.set(ImageUrlExtension.URL_CHANGER, urlPrefixer);
	        options.set(TablesExtension.COLUMN_SPANS, false)
	        .set(TablesExtension.APPEND_MISSING_COLUMNS, true)
	        .set(TablesExtension.DISCARD_EXTRA_COLUMNS, true)
	        .set(TablesExtension.HEADER_SEPARATOR_COLUMN_MATCH, true);
			Parser parser = Parser.builder(options).build();
			HtmlRenderer renderer = HtmlRenderer.builder(options).build();
			String content = IOUtils.toString(file);
	        com.vladsch.flexmark.ast.Node document = parser.parse(content);
	        collectImages(document, urls);
	        System.out.println(renderer.render(document));
	        convertDocumentToComponents(document, pageData, renderer, parser);
	        return pageData;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return pageData;
		}
		
	}
		
	private void collectImages(com.vladsch.flexmark.ast.Node document, List<String> urls) {
		ImageVisitor visitor = new ImageVisitor(urls);
        visitor.collectImages(document);
	}

	private void convertDocumentToComponents(
			com.vladsch.flexmark.ast.Node document, MarkdownPageData pageData, HtmlRenderer renderer, Parser parser) throws RepositoryException {
		com.vladsch.flexmark.ast.Node element = document.getFirstChild();
		//extracts title from firts headline if there is no metadata
		extractTitle(element, pageData);
		int index = 0;
		while(element != null) {
			MarkdownNodeMapper mapper = MarkdownMappings.getMarkdownNodeMapper(element);
			if(!(mapper instanceof FrontMatterMapper)) {
				element = mapper.mapToComponen(element, pageData, parser, renderer);
			} else {
				element = mapper.mapToComponen(element, pageData, parser, renderer);
			}
			index++;
		}
		
	}

	private void extractTitle(com.vladsch.flexmark.ast.Node element, MarkdownPageData pageData) throws RepositoryException {
		if(element == null) {
			return;
		}
		if(element instanceof YamlFrontMatterBlock) {
			return;
		}
		HeadingCollectingVisitor headingVisitor = new HeadingCollectingVisitor();
		List<Heading> headings = headingVisitor.collectAndGetHeadings(element.getDocument());
		if(headings.size() > 0) {
			pageData.setTitle(headings.get(0).getText().toString());
		}		
	}



}

package io.adobe.udp.markdownimporter.services;

import io.adobe.udp.markdownimporter.MarkdownPageData;
import io.adobe.udp.markdownimporter.flexmarkExtensions.GithubHostedImagePrefixer;

import java.io.Reader;
import java.util.List;

public interface MarkdownParserService {
	
	public MarkdownPageData parseMarkdownFile(Reader file, MarkdownPageData pageData, List<String> urls,
			GithubHostedImagePrefixer urlPrefixer);

}

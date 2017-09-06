package io.adobe.udp.markdownimporter;

import java.io.File;
import java.util.Map;

public interface MarkdownImporter {

	public void processGithubPage();
	public Map<String, PageData> getPageData();
	public Map<String, File> getImages();
}

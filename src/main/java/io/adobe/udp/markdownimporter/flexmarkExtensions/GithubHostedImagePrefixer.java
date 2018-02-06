/**
 * Copyright 2017 Adobe Systems Incorporated. All rights reserved.
 * This file is licensed to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy
 * of the License at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.adobe.udp.markdownimporter.flexmarkExtensions;

import io.adobe.udp.markdownimporter.BranchRootInfo;
import io.adobe.udp.markdownimporter.GithubData;
import io.adobe.udp.markdownimporter.utils.Constants;
import io.adobe.udp.markdownimporter.utils.GithubConstants;
import io.adobe.udp.markdownimporter.utils.IONodeUtils;

import java.net.URI;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class GithubHostedImagePrefixer implements UrlPrefixer {
	
	private String urlPrefix;
	private String pagePath;
	private String originalPath;
	private String githubPageUrl;
	private GithubData githubData;
	private BranchRootInfo branchRootInfo;
	private Set<String> allFiles;
	
	public GithubHostedImagePrefixer(String urlPrefix, GithubData githubData, BranchRootInfo rootInfo, String pagePath,
			String githubPageUrl, Set<String> allFiles, String originalPath) {
		this.urlPrefix = urlPrefix;
		this.pagePath = pagePath;
		this.githubPageUrl = githubPageUrl;
		this.githubData = githubData;
		this.branchRootInfo = rootInfo;
		this.allFiles = allFiles;
		this.originalPath = originalPath;
	}

	@Override
	public String prefix(String path) {
		if(!path.startsWith(Constants.HTTP_PREFIX) && !path.startsWith(Constants.HTTPS_PREFIX) && !path.startsWith("#") && !path.startsWith("mailto:")) {
			if(StringUtils.isNotBlank(path) && path.endsWith(GithubConstants.MARKDOWN_EXTENSION)) {
				return rewritePathToMarkdown(path);
			}
			return urlPrefix + IONodeUtils.removeFirstSlash(path);
		}
		return path;
	}

	private String rewritePathToMarkdown(String path) {
		String originalLinkPath = getOriginalLinkPath(path);
		String internalPath = originalLinkPath;
		if(StringUtils.isNotBlank(githubData.getBlobPrefix())) {
			internalPath = path.replaceFirst(githubData.getBlobPrefix(), "");
		}
		internalPath = IONodeUtils.removeFirstSlash(branchRootInfo.getInternalPath(internalPath));
		if(allFiles.contains(originalLinkPath)) {
			return IONodeUtils.replaceDotsInPath(aemFilePath(internalPath)) + ".html";
		}
		return fileGithubUrl(path);
		
	}

	/**
	 * return path to the link on github or file system (relative to root)
	 * @param path
	 * @return
	 */
	private String getOriginalLinkPath(String path) {
		URI uri = null;
		try {
			if(!path.startsWith("/") ) {
				uri = new URI(getParentPage(originalPath) + "/" + path);
				return uri.normalize().getPath();
			}
			return path;
		} catch(Exception e) {
			System.out.println("error processing path: " + path);
			return StringUtils.EMPTY;
		}
	}

	private String aemFilePath(String path) {
		if(StringUtils.equals(path, branchRootInfo.getRootGithubFileName())) {
			return branchRootInfo.getBranchRootPath();
		}
		return branchRootInfo.getDocumentationRootPath() + "/" + branchRootInfo.getBranchPageNameWithSuffix() 
			+ "/" + path;
	}
	
	private String getParentPage(String page) {
		return page.substring(0, page.lastIndexOf("/"));
	}
	private String fileGithubUrl(String path) {
		if(isAbsolute(path)) {
			return githubData.getBranchRootUrl() + path;
		}
		
		if(path.startsWith("./")) {
			return githubPageUrl + "/." +  path;
		} else {
			return githubPageUrl + "/" + path;
		}
	}

	private boolean isAbsolute(String path) {
		return path.startsWith("/");
	}

}

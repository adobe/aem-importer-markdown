/**
 * Copyright 2017 Adobe Systems Incorporated. All rights reserved.
 * This file is licensed to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy
 * of the License at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.adobe.udp.markdownimporter.services;

import io.adobe.udp.markdownimporter.GithubData;
import io.adobe.udp.markdownimporter.InputConfig;
import io.adobe.udp.markdownimporter.utils.Constants;
import io.adobe.udp.markdownimporter.utils.GithubConstants;
import io.adobe.udp.markdownimporter.utils.GithubRequests;
import io.adobe.udp.markdownimporter.utils.IONodeUtils;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Joiner;

public class GithubLinkServiceImpl implements GithubLinkService {
	

	private static final String TREES = "git/trees";
	
	public String getRepositoryApiUrl(String repositoryUrl, InputConfig config) throws MalformedURLException {
		URL url = new URL(repositoryUrl);
		String path = url.getPath();
		String[] segments = path.split("/");
		// repository url /owner/repository
		if (segments.length == 3) {
				return url.toString().replace(url.getPath(), GithubConstants.REPOS + url.getPath())
						.replaceFirst(url.getHost(), config.getGithubApiUrl());
		} 		
		return repositoryUrl;
	}
	
	public String getReadmeUrl(GithubData githubData) throws MalformedURLException {
		URL url = new URL(githubData.getRepository());
		String path = url.getPath();
		String[] segments = path.split("/");
		// repository url /owner/repository
		if (segments.length == 3) {
			return url.toString().replace(url.getPath(), GithubConstants.REPOS + url.getPath() 
					 + GithubConstants.README_PATH).replaceFirst(url.getHost(), githubData.getApiUrl()) + "?ref=" + githubData.getRepositoryBranch();
		}
		return null;
		
	}
	
	public String getBranchesUrl(String repositoryUrl, InputConfig config) throws MalformedURLException {
		URL url = new URL(repositoryUrl);
		String path = url.getPath();
		String[] segments = path.split("/");
		// repository url /owner/repository
		if (segments.length == 3) {
				return url.toString().replace(url.getPath(), GithubConstants.REPOS + url.getPath() 
						 + GithubConstants.BRANCHES + "?per_page=100").replaceFirst(url.getHost(), config.getGithubApiUrl());
		}
		return null;
	}
	
	public String getDiffUrl(GithubData githubData, String sha) throws MalformedURLException {
		URL url = new URL(githubData.getRepository());
		String path = url.getPath();
		String[] segments = path.split("/");
		// repository url /owner/repository
		if (segments.length == 3) {
			return url.toString().replace(url.getPath(), GithubConstants.REPOS + url.getPath() 
					 + GithubConstants.COMPARE).replace(url.getHost(), githubData.getApiUrl())
					 + "/" + sha + "..." + githubData.getSha();
		}
		return null;
	}
	
	public String getContentUrl(String repositoryUrl, String ref) {
		// TODO for images and github hosted files
		return null;
	}

	public String mapPathToUrl(String path, GithubData githubData) throws MalformedURLException {
		Joiner joiner = Joiner.on("/").skipNulls();
		if(!githubData.isCorp()) {
			if(!path.startsWith(Constants.HTTP_PREFIX) && !path.startsWith(Constants.HTTPS_PREFIX)) {
				return joiner.join(githubData.getContentUrl(), githubData.getRepositoryOwner(), githubData.getReposiotryName(),
						githubData.getRepositoryBranch(), path);
			} else {
				URL url = new URL(path);
				String urlPath = url.getPath();
				return githubData.getContentUrl() + urlPath;
			}
		} else {
			String contentsUrl = Constants.HTTPS_PREFIX + githubData.getApiUrl() + GithubConstants.REPOS + "/" +
					githubData.getRepositoryOwner() + "/" + githubData.getReposiotryName() + GithubConstants.CONTENTS + 
					"/" + path + "?ref=" + githubData.getRepositoryBranch();
			return GithubRequests.getFileUrl(contentsUrl, githubData.getToken());
		}
	}

	public GithubData getGithubData(String repositoryUrl, String branch, InputConfig config) {
		GithubData githubData = new GithubData();
		try {
			URL url = new URL(repositoryUrl);
			String requestPath = url.getPath();
			String[] segments = requestPath.split("/");
			if(segments.length < 3) {
				return null;
			}
			githubData.setCorp(config.isPrivateRepository());
			githubData.setGithubUrl(config.getGithubUrl());
			githubData.setApiUrl(config.getGithubApiUrl());
			githubData.setContentUrl(config.getGithubContentUrl());
			githubData.setToken(config.getApiToken());

			githubData.setRepositoryOwner(segments[1]);
			githubData.setReposiotryName(segments[2]);
			githubData.setRepositoryBranch(branch);
			githubData.setRepository(repositoryUrl);
			githubData.setBlobPrefix("/" + segments[1] + "/" + segments[2] + "/" + GithubConstants.BLOB + "/" + branch + "/");
			githubData.setTreePrefix("/" + segments[1] + "/" + segments[2] + "/" + GithubConstants.TREE + "/" + branch + "/");
			githubData.setRawPrefix("/" + segments[1] + "/" + segments[2] + "/" + GithubConstants.RAW + "/" + branch + "/");
			githubData.setBranchRootUrl(Constants.HTTPS_PREFIX + githubData.getGithubUrl() + githubData.getBlobPrefix());
			githubData.setRawContentBranchRootUrl(Constants.HTTPS_PREFIX + githubData.getGithubUrl() + githubData.getRawPrefix());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return githubData;
	}

	public String getGithubTreeUrl(String path, GithubData githubData) throws MalformedURLException {
		Joiner joiner = Joiner.on("/").skipNulls();
		String repositoryPath;
		if(!path.startsWith(Constants.HTTP_PREFIX) && !path.startsWith(Constants.HTTPS_PREFIX)) {
			repositoryPath = stripFromExtension(path);
		} else {
			repositoryPath = stripFromExtension(getPathFromUrl(path, githubData));
		}
		return Constants.HTTPS_PREFIX + joiner.join(getWithoutSlahAtEnd(githubData.getApiUrl()), IONodeUtils.removeFirstSlash(GithubConstants.REPOS),
				githubData.getRepositoryOwner(), githubData.getReposiotryName(), TREES, githubData.getRepositoryBranch() + getTreeSuffix(repositoryPath));
	}

	public String getPathFromUrl(String path, GithubData githubData) throws MalformedURLException {
		URL url = new URL(path);
		String requestPath = url.getPath();
		return extractPath(requestPath, githubData);
	}
	
	private String getWithoutSlahAtEnd(String text) {
		if(!text.endsWith("/")) {
			return text;
		}
		return text.substring(0, text.length() - 1);
	}

	private String extractPath(String path, GithubData githubData) {
		if(path.startsWith(githubData.getBlobPrefix())) {
			return path.replaceFirst(githubData.getBlobPrefix(), "");
		}
		if(path.startsWith(githubData.getTreePrefix())) {
			return path.replaceFirst(githubData.getTreePrefix(), "");
		}
		return path.replaceFirst("/" + githubData.getRepositoryOwner() + "/" + githubData.getReposiotryName() + "/", "");
		
	}

	public String getPathInRepository(String path, GithubData githubData)  {
		try {
			if(!path.startsWith(Constants.HTTP_PREFIX) && !path.startsWith(Constants.HTTPS_PREFIX)) {
				return path;
			} else {
				return getPathFromUrl(path, githubData);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return path;
	}
	
	private String stripFromExtension(String path) {
		if(path.toLowerCase().endsWith(GithubConstants.MARKDOWN_EXTENSION)) {
			return path.substring(0, path.toLowerCase().lastIndexOf(GithubConstants.MARKDOWN_EXTENSION));
		}
		return path;
	}
	
	private String getTreeSuffix(String repositoryPath) {
		if(StringUtils.isNotBlank(repositoryPath)) {
			return ":" + repositoryPath;
		}
		return StringUtils.EMPTY;
	}

	public String getCommitUrl(GithubData githubData, String sha) {
		return Constants.HTTPS_PREFIX + githubData.getApiUrl() + GithubConstants.REPOS + "/"
				+ githubData.getRepositoryOwner() + "/" + githubData.getReposiotryName() + "/"
				+ GithubConstants.COMMITS + "/" + sha;
	}

	public String getFileBlobUrl(GithubData githubData, String githubFilePath) {
		if(StringUtils.isNotBlank(githubData.getRepository())) {
			return githubData.getBranchRootUrl() + IONodeUtils.removeFirstSlash(githubFilePath);
		}
		return StringUtils.EMPTY;
	}

}

/**
 * Copyright 2017 Adobe Systems Incorporated. All rights reserved.
 * This file is licensed to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy
 * of the License at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.adobe.udp.markdownimporter.services;

import io.adobe.udp.markdownimporter.GithubData;
import io.adobe.udp.markdownimporter.InputConfig;

import java.net.MalformedURLException;
import java.util.Map;

import javax.jcr.RepositoryException;

public interface GithubLinkService {
	
	String getRepositoryApiUrl(String repositoryUrl, InputConfig config) throws MalformedURLException;
	
	String getReadmeUrl(GithubData githubData) throws MalformedURLException;
	
	String getBranchesUrl(String repositoryUrl, InputConfig config) throws MalformedURLException;
	
	String getDiffUrl(GithubData githubData, String sha) throws MalformedURLException;
	
	String getContentUrl(String repositoryUrl, String ref);
	
	String mapPathToUrl(String path, GithubData githubData, InputConfig config) throws MalformedURLException;
	
	String getGithubTreeUrl(String path, GithubData githubData) throws MalformedURLException;
	
	String getPathInRepository(String path, GithubData githubData);
	
	String getCommitUrl(GithubData githubData, String sha);

	GithubData getGithubData(String repositoryUrl, String branch, InputConfig config);
	
	String getFileBlobUrl(GithubData githubData, String githubFilePath);

}

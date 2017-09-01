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
	
	String mapPathToUrl(String path, GithubData githubData) throws MalformedURLException;
	
	String getGithubTreeUrl(String path, GithubData githubData) throws MalformedURLException;
	
	String getPathInRepository(String path, GithubData githubData);
	
	String getCommitUrl(GithubData githubData, String sha);

	GithubData getGithubData(String repositoryUrl, String branch, InputConfig config);
	
	String getFileBlobUrl(GithubData githubData, String githubFilePath);

}

package io.adobe.udp.markdownimporter.services;

import java.net.URL;

import io.adobe.udp.markdownimporter.GithubData;
import io.adobe.udp.markdownimporter.InputConfig;
import io.adobe.udp.markdownimporter.utils.Constants;
import io.adobe.udp.markdownimporter.utils.GithubConstants;

public class FileSystemPathServiceImpl implements FileSystemPathService {

	@Override
	public String getLocalFilePath(String filePath, String dirPath) {
		return dirPath + "/" + filePath;
	}

	@Override
	public String mapPathToLocation(String string, String dirPath) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFileBlobUrl(InputConfig config, String rootPath,
			String dirPath) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GithubData createGithubData(String branch, InputConfig config) {
		GithubData githubData = new GithubData();
		try {
			URL url = new URL(config.getRepositoryUrl());
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
			githubData.setRepository(config.getRepositoryUrl());
			githubData.setBlobPrefix("/" + segments[1] + "/" + segments[2] + "/" + GithubConstants.BLOB + "/" + branch + "/");
			githubData.setTreePrefix("/" + segments[1] + "/" + segments[2] + "/" + GithubConstants.TREE + "/" + branch + "/");
			githubData.setRawPrefix("/" + segments[1] + "/" + segments[2] + "/" + GithubConstants.RAW + "/" + branch + "/");
			githubData.setBranchRootUrl(Constants.HTTPS_PREFIX + url.getHost() + githubData.getBlobPrefix());
			githubData.setRawContentBranchRootUrl(Constants.HTTPS_PREFIX + githubData.getGithubUrl() + githubData.getRawPrefix());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return githubData;
	}

}

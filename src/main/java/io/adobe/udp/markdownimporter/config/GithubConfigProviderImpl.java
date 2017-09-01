package io.adobe.udp.markdownimporter.config;

import org.apache.commons.lang3.StringUtils;


public class GithubConfigProviderImpl implements GithubConfigProvider {
	
	
	private String githubApiUrl;
	private String githubContentUrl;
	private String githubCorpApiUrl;
	private String githubCorpContentUrl;
	private String githubToken;
	private String githubCorpToken;
	private String githubRepoPrefix;
	private String githubPublicUrl;
	private String corporateGithubUrl;
	private int timeLimit;
	
	
	public String getApiUrl() {
		return this.githubApiUrl;
	}

	public String getContentUrl() {
		return getWithEndSlash(this.githubContentUrl);
	}

	public String getGithubToken(boolean publicRepository) {
		if(publicRepository) {
			return this.githubToken;
		} else {
			return this.githubCorpToken;
		}
	}

	public String getApiRepoPrefix() {
		return this.githubRepoPrefix;
	}
	
	public String getPublicGithubUrl() {
		return this.githubPublicUrl;
	}
	
	private String getWithEndSlash(String stringValue) {
		if(StringUtils.isNotBlank(stringValue)) {
			if(stringValue.endsWith("/")) {
				return stringValue;
			} else {
				return stringValue + "/";
			}
		}
		return StringUtils.EMPTY;
	}
	
	private String getWithoutStartSlash(String stringValue) {
		if(StringUtils.isNotBlank(stringValue)) {
			if(stringValue.startsWith("/")) {
				return stringValue.substring(1);
			} else {
				return stringValue;
			}
		}
		return StringUtils.EMPTY;
	}

	public int getTimeLimit() {
		return timeLimit;
	}

	public String getCorpApiUrl() {
		return this.githubCorpApiUrl;
	}


	public String getCorpContentUrl() {
		return this.githubCorpContentUrl;
	}

	public String getCorporateGithubUrl() {
		return this.corporateGithubUrl;
	}

}

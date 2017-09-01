package io.adobe.udp.markdownimporter.config;

public interface GithubConfigProvider {
		
	String getApiUrl();
	
	String getCorpApiUrl();
	
	String getContentUrl();
	
	String getCorpContentUrl();
	
	String getGithubToken(boolean publicRepository);
		
	String getApiRepoPrefix();
	
	String getPublicGithubUrl();
	
	String getCorporateGithubUrl();
	
	int getTimeLimit();

}

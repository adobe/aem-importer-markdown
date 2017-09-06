package io.adobe.udp.markdownimporter;

import java.util.List;
import java.util.Map;

public class InputConfig {
	
	private String githubUrl;
	private String githubContentUrl;
	private String githubApiUrl;
	private String apiToken;
	private int commitTime;
	private boolean privateRepository;
	
	private String repositoryUrl;
	private String rootPath;
	private List<String> branches;
	private List<String> workingDirs;
	private List<String> pages;
	
	private Map<String, String> componentMappings;
	private String rootTemplate;
	private String rootPageResourceType;
	private String pageTemplate;
	private String pageResourceType;
	private String designPath;
	private String rootTitle;
	private String packageName;
	private String group;
	private String version;
	private boolean localCheckout;
	
	public String getGithubUrl() {
		return githubUrl;
	}
	public String getGithubContentUrl() {
		return githubContentUrl;
	}
	public String getGithubApiUrl() {
		return githubApiUrl;
	}
	public String getApiToken() {
		return apiToken;
	}
	public int getCommitTime() {
		return commitTime;
	}
	public String getRepositoryUrl() {
		return repositoryUrl;
	}
	public String getRootPath() {
		return rootPath;
	}
	public List<String> getBranches() {
		return branches;
	}
	public List<String> getPages() {
		return pages;
	}
	public Map<String, String> getComponentMappings() {
		return componentMappings;
	}
	public String getRootTemplate() {
		return rootTemplate;
	}
	public String getPageTemplate() {
		return pageTemplate;
	}
	public void setGithubUrl(String githubUrl) {
		this.githubUrl = githubUrl;
	}
	public void setGithubContentUrl(String githubContentUrl) {
		this.githubContentUrl = githubContentUrl;
	}
	public void setGithubApiUrl(String githubApiUrl) {
		this.githubApiUrl = githubApiUrl;
	}
	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}
	public void setCommitTime(int commitTime) {
		this.commitTime = commitTime;
	}
	public void setRepositoryUrl(String repositoryUrl) {
		this.repositoryUrl = repositoryUrl;
	}
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}
	public void setBranches(List<String> branches) {
		this.branches = branches;
	}
	public void setPages(List<String> pages) {
		this.pages = pages;
	}
	public void setComponentMappings(Map<String, String> componentMappings) {
		this.componentMappings = componentMappings;
	}
	public void setRootTemplate(String rootTemplate) {
		this.rootTemplate = rootTemplate;
	}
	public void setPageTemplate(String pageTemplate) {
		this.pageTemplate = pageTemplate;
	}
	public String getRootTitle() {
		return rootTitle;
	}
	public void setRootTitle(String rootTitle) {
		this.rootTitle = rootTitle;
	}
	public boolean isPrivateRepository() {
		return privateRepository;
	}
	public void setPrivateRepository(boolean privateRepository) {
		this.privateRepository = privateRepository;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getRootPageResourceType() {
		return rootPageResourceType;
	}
	public void setRootPageResourceType(String rootPageResourceType) {
		this.rootPageResourceType = rootPageResourceType;
	}
	public String getPageResourceType() {
		return pageResourceType;
	}
	public void setPageResourceType(String pageResourceType) {
		this.pageResourceType = pageResourceType;
	}
	public String getDesignPath() {
		return designPath;
	}
	public void setDesignPath(String designPath) {
		this.designPath = designPath;
	}
	public List<String> getWorkingDirs() {
		return workingDirs;
	}
	public void setWorkingDirs(List<String> workingDirs) {
		this.workingDirs = workingDirs;
	}
	public boolean isLocalCheckout() {
		return this.workingDirs != null;
	}

}

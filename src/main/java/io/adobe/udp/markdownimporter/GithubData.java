package io.adobe.udp.markdownimporter;

public class GithubData {
	
	private String githubUrl;
	
	private String apiUrl;
	
	private String contentUrl;
	
	private String token;
	
	private String repositoryOwner;
	
	private String reposiotryName;
	
	private String repositoryBranch;
	
	private String repositorySha;
	
	private String repository;
	
	private String treePrefix;
	
	private String blobPrefix;
	
	private String rawPrefix;
	
	private String sha;
	
	private String branchRootUrl;
	
	private String rawContentBranchRootUrl;
	
	private boolean corp;

	public String getGithubUrl() {
		return githubUrl;
	}

	public void setGithubUrl(String githubUrl) {
		this.githubUrl = githubUrl;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getContentUrl() {
		return contentUrl;
	}

	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRepositoryOwner() {
		return repositoryOwner;
	}

	public void setRepositoryOwner(String repositoryOwner) {
		this.repositoryOwner = repositoryOwner;
	}

	public String getReposiotryName() {
		return reposiotryName;
	}

	public void setReposiotryName(String reposiotryName) {
		this.reposiotryName = reposiotryName;
	}

	public String getRepositoryBranch() {
		return repositoryBranch;
	}

	public void setRepositoryBranch(String repositoryBranch) {
		this.repositoryBranch = repositoryBranch;
	}

	public String getRepositorySha() {
		return repositorySha;
	}

	public void setRepositorySha(String repositorySha) {
		this.repositorySha = repositorySha;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public String getTreePrefix() {
		return treePrefix;
	}

	public void setTreePrefix(String treePrefix) {
		this.treePrefix = treePrefix;
	}

	public String getBlobPrefix() {
		return blobPrefix;
	}

	public void setBlobPrefix(String blobPrefix) {
		this.blobPrefix = blobPrefix;
	}

	public String getSha() {
		return sha;
	}

	public void setSha(String sha) {
		this.sha = sha;
	}

	public String getBranchRootUrl() {
		return branchRootUrl;
	}

	public void setBranchRootUrl(String branchRootUrl) {
		this.branchRootUrl = branchRootUrl;
	}

	public String getRawPrefix() {
		return rawPrefix;
	}

	public void setRawPrefix(String rawPrefix) {
		this.rawPrefix = rawPrefix;
	}

	public String getRawContentBranchRootUrl() {
		return rawContentBranchRootUrl;
	}

	public void setRawContentBranchRootUrl(String rawContentBranchRootUrl) {
		this.rawContentBranchRootUrl = rawContentBranchRootUrl;
	}

	public boolean isCorp() {
		return corp;
	}

	public void setCorp(boolean corp) {
		this.corp = corp;
	}
	
}

/**
 * Copyright 2017 Adobe Systems Incorporated. All rights reserved.
 * This file is licensed to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy
 * of the License at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.adobe.udp.markdownimporter;

import io.adobe.udp.markdownimporter.utils.IONodeUtils;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class BranchRootInfo {
	
	public static final String COMMON_PREFIX = "commonPrefix";
	
	private String documentationRootPath;
	private String rootGithubFile;
	private String rootPath;
	private String rootPrefix;
	private String commonPrefix;
	private String branchPageName;
	private String branch;
	private boolean first;
	
	public BranchRootInfo(String documentationRootPath, String rooPath, String rootPrefix, String commonPrefix, String branch,
			boolean first) {
		this.documentationRootPath = documentationRootPath;
		this.rootGithubFile = rooPath;
		this.rootPath = io.adobe.udp.markdownimporter.utils.IONodeUtils.stripFromExtension(rooPath);
		this.commonPrefix = commonPrefix;
		this.first = first;
		this.branchPageName = extractName();
		this.branch = branch;
	}
	
	public static BranchRootInfo createBranchRootInfo(String rootPage,
			List<String> configPages, String branch,
			Set<String> pages, boolean first, boolean hasPages) {
		String rootPath = branch;
		if(hasPages && pages.size() > 0) {
			rootPath = pages.iterator().next();
		}
		String commonPrefix = StringUtils.getCommonPrefix(pages.toArray(new String[pages.size()]));
		if(!hasPages) {
			return new BranchRootInfo(rootPage, branch, null, null, branch,
					 first);
		}
		String rootPrefix = rootPath.contains("/") ? rootPath.substring(0, rootPath.lastIndexOf("/"))
				: rootPath;
		return new BranchRootInfo(rootPage, rootPath, rootPrefix, commonPrefix, branch,
				 first);
	}


	private String extractName() {
		if(!rootPath.contains("/")) {
			return rootPath;
		}
		return rootPath.substring(rootPath.lastIndexOf("/") + 1);
	}

	public String getDocumentationRootPath() {
		return documentationRootPath;
	}

	public String getRootPath() {
		return rootPath;
	}

	public String getRootPrefix() {
		return rootPrefix;
	}

	public String getCommonPrefix() {
		return commonPrefix;
	}

	public boolean isFirst() {
		return first;
	}
	
	public String getBranchPathFirstElement() {
		String result;
		if(rootPath.contains("/")) {			
			result = rootPath.substring(rootPath.lastIndexOf("/") + 1);
		} else {
			result = rootPath;
		}
		return IONodeUtils.replaceDotsInPath(IONodeUtils.removeFirstSlash(result));
	}
	
	
	public String getInternalPath(String githubPath) {
		String internalPath = IONodeUtils.removeFirstSlash(githubPath);
		if(StringUtils.isNoneBlank(rootPath) && rootPath.contains("/")) {
			internalPath = internalPath.replaceFirst(rootPath.substring(0, rootPath.lastIndexOf("/")), "");
		}
		if(StringUtils.isNotBlank(commonPrefix)) {
			internalPath = internalPath.replaceFirst(commonPrefix, "");
		}
		return internalPath;
	}

	public String getBranchPageName() {
		return branchPageName;
	}
	
	public String getBranchPageNameWithSuffix() {
		return branchPageName + "_" + branch;
	}

	public String getRootGithubFile() {
		return rootGithubFile;
	}

	public String getBranchRootPath() {
		return documentationRootPath + "/" + IONodeUtils.replaceDotsInPath(getBranchPageNameWithSuffix());
	}
	
	public String getRootGithubFileName() {
		if(rootGithubFile.contains("/")) {
			return rootGithubFile.substring(rootGithubFile.indexOf("/") + 1);
		}
		return rootGithubFile;
	}

}

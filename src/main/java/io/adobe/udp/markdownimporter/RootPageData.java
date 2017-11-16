/**
 * Copyright 2017 Adobe Systems Incorporated. All rights reserved.
 * This file is licensed to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy
 * of the License at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.adobe.udp.markdownimporter;

import io.adobe.udp.markdownimporter.utils.GithubConstants;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.jcr.resource.JcrResourceConstants;

import com.day.cq.wcm.api.NameConstants;

public class RootPageData implements PageData {
	
	private InputConfig inputConfig;
	
	public RootPageData(InputConfig config) {
		this.inputConfig = config;
	}

	@Override
	public Map<String, Object> toContent() {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> githubData = new HashMap<String, Object>();
		result.put(JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY, inputConfig.getRootPageResourceType());
		result.put(NameConstants.PN_DESIGN_PATH, inputConfig.getDesignPath());
		result.put(NameConstants.PN_TEMPLATE, inputConfig.getRootTemplate());
		result.put("imported", true);
		result.put("isFolder", true);
		result.put("isDocumentationRoot", "true");
		if(inputConfig.getBranches() != null) {
			githubData.put(GithubConstants.BRANCH, inputConfig.getBranches().toArray());
		}
		if(inputConfig.getPages() != null) {
			githubData.put("pages", inputConfig.getPages().toArray());
		}
		githubData.put(GithubConstants.URL, inputConfig.getRepositoryUrl());
		result.put("github", githubData);
		return result;
	}

}

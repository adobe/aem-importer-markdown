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
		githubData.put(GithubConstants.BRANCH, inputConfig.getBranches().toArray());
		githubData.put("pages", inputConfig.getPages().toArray());
		githubData.put(GithubConstants.URL, inputConfig.getRepositoryUrl());
		result.put("github", githubData);
		return result;
	}

}

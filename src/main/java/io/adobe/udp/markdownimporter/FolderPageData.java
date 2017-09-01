package io.adobe.udp.markdownimporter;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.jcr.resource.JcrResourceConstants;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.NameConstants;

public class FolderPageData implements PageData {
		
	private String resourceType;
	private String template;
	private String designPath;
	private String title;
	
	public FolderPageData(String resourceType, String template, String designPath) {
		this.resourceType = resourceType;
		this.template = template;
		this.designPath = designPath;
	}

	@Override
	public Map<String, Object> toContent() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY, resourceType);
		result.put(NameConstants.PN_DESIGN_PATH, designPath);
		result.put(NameConstants.PN_TEMPLATE, template);
		result.put("imported", true);
		result.put("isFolder", true);
		result.put(JcrConstants.JCR_TITLE, title);
		return result;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}

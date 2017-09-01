package io.adobe.udp.markdownimporter.utils;

import io.adobe.udp.markdownimporter.BranchRootInfo;
import io.adobe.udp.markdownimporter.FolderPageData;
import io.adobe.udp.markdownimporter.GithubData;
import io.adobe.udp.markdownimporter.PageData;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.jcr.resource.JcrResourceConstants;

import com.day.cq.commons.jcr.JcrUtil;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;

public class IONodeUtils {
	
	public static void removeNodesChildren(Node node, boolean save) throws RepositoryException {
		if(node != null) {
			NodeIterator iterator = node.getNodes();
			while(iterator.hasNext()) {
				iterator.nextNode().remove();
			}
			if(save) {
				node.getSession().save();
			}
		}
	}
	
	public static void removeChildPages(Page page) throws RepositoryException {
		Iterator<Page> children = page.listChildren();
		while(children.hasNext()) {
			children.next().adaptTo(Node.class).remove();;
		}
	}
	
	public static Node getOrCreateGithubDocPath(String path, Session session) throws RepositoryException {
		return JcrUtil.createPath(replaceDotsInPath(path), "cq:Page", "cq:Page", session, false);
	}

	public static void removeIfExists(String rootPath, String file,
			Session session) throws RepositoryException {
			String path = replaceDotsInPath(rootPath) + replaceDotsInPath(file);
			if(JcrUtils.getNodeIfExists(path, session) != null) {
				session.removeItem(path);
			}	
	}
	
	public static String replaceDotsInPath(String path) {
		return path.replace(".", "_");
	}
	
	public static String trimName(String name) {
		if(name.startsWith("/")) {
			return name.substring(1);
		}
		return name;
		
	}
	
	public static String removeParams(String path) {
		if(path.contains("?")) {
			return path.substring(0, path.lastIndexOf("?"));
		}
		return path;
	}
	
	public static String getBranchPagePath(String branch, BranchRootInfo rootInfo) {
		String rootWithoutFirstSlash = removeFirstSlash(rootInfo.getRootPath()).replaceFirst(rootInfo.getCommonPrefix(), "");
		if(rootInfo.isFirst()) {
				return trimName(rootWithoutFirstSlash);
		} else {
			return branch + "/" + rootWithoutFirstSlash;
		}
	}
	
	public static String getBranchPageName(BranchRootInfo rootInfo) {
				return replaceDotsInPath(trimName(rootInfo.getBranchPageName()));
	}
	
	public static String getBranchPageName(String branch) {
		return replaceDotsInPath(trimName(branch).replace("/", "_"));
	}
	
	public static void populatePath(String path, Session session) {
		
	}

	public static void addPlaceHolderTemplate(Node leaf) throws RepositoryException {
		if(leaf != null && leaf.getDepth() > Constants.UDP_HOME_LEVEL && leaf.hasNode(JcrConstants.JCR_CONTENT)) {
			if(isGithubPage(leaf)) {
				return;
			}
		} else if (leaf.getDepth() > Constants.UDP_HOME_LEVEL && !leaf.hasNode(JcrConstants.JCR_CONTENT)){
			fillContent(leaf);
		}
		addPlaceHolderTemplate(leaf.getParent());
	}
	
	public static void addPlaceHolderTemplate(String rootPath, String filePath, String githubFilePath, Set<String> files, Map<String, PageData> pages, FolderPageData pageData) {
		String parentPath = getParentPath(filePath);
		if(!rootPath.equals(filePath) && !rootPath.equals(parentPath)) {
			if(!files.contains(getParentPath(githubFilePath) + GithubConstants.MARKDOWN_EXTENSION)) {
				pageData.setTitle(extractName(parentPath));
				pages.put(parentPath, pageData);
			}
		}
	}
	

	public static String getBranchRootPath(String githubRoot, String branch) {
		return githubRoot + "/" + getBranchPageName(branch);
	}
	
	public static String removeFirstSlash(String path) {
		if(path.startsWith("/")) {
			return path.substring(1);
		}
		return path;
	}
	
	public static String getFileFolder(GithubData githubData, String filePath) {
		return githubData.getRawContentBranchRootUrl() + getFileFolder(filePath);
	}

	public static String stripFromExtension(String path) {
		String result = new String(path);
		if(path.toLowerCase().endsWith(GithubConstants.MARKDOWN_EXTENSION)) {
			result = path.substring(0, path.toLowerCase().lastIndexOf(GithubConstants.MARKDOWN_EXTENSION));
		}
		if (result.endsWith("/")) {
			return result.substring(0, result.lastIndexOf("/"));
		} 
		return result;
	}
	
	private static boolean isGithubPage(Node node) throws RepositoryException {
		Node content = node.getNode(JcrConstants.JCR_CONTENT);
		if(content.hasProperty(JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY)) {
			if(GithubConstants.GITHUB_DOC_ROOT_RESOURCE_TYPE.equals(
					content.getProperty(JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY).getString())) {
				return true;
			}
		}
		return false;
	}
	
	public static void markToRemove(String path, Session session) throws RepositoryException {
		Node contentNode = JcrUtils.getNodeIfExists(replaceDotsInPath(path) + "/" + JcrConstants.JCR_CONTENT, session);
		if(contentNode != null) {
			contentNode.setProperty(GithubConstants.TO_REMOVE, "true");
		}
	}
	
	public static String findBranchInTree(Page page) {
    	if(page != null && page.hasContent() && 
    			(page.getContentResource().isResourceType(GithubConstants.IMPORTED_GITHUB_PAGE_RESOURE_TYPE)
    			 || page.getContentResource().isResourceType(GithubConstants.GITHUB_PAGE_RESOURE_TYPE))) {
    		
    		Resource content = page.getContentResource();
    		String contentBranch = (String) content.getValueMap().get(GithubConstants.BRANCH);
    		if(contentBranch != null) {
    			return contentBranch;
    		} else if(page.listChildren() != null) { 
    			return findBranchInTree(page.listChildren().next());
    		}
    	}
    	return null;
    }
	
	public static String findBranchPath(Page githubDocPage, String branch, ResourceResolver resourceResolver) {
		Iterator<Page> branches = githubDocPage.listChildren();
		while(branches.hasNext()) {
			Page branchPage = branches.next();
			Resource contentResource = branchPage.getContentResource();
			String branchName = contentResource.adaptTo(ValueMap.class).get(GithubConstants.BRANCH, String.class);
			if(StringUtils.isNotBlank(branchName) && branch.equals(branchName)){
				return branchPage.getPath();
			}
		}
		return null;
	}
	
	public static String findFirstGithubBranch(Page githubDocPage, ResourceResolver resourceResolver) {
		Iterator<Page> branches = githubDocPage.listChildren();
		while(branches.hasNext()) {
			Page branchPage = branches.next();
			Resource contentResource = branchPage.getContentResource();
			String branchName = contentResource.adaptTo(ValueMap.class).get(GithubConstants.BRANCH, String.class);
			if(StringUtils.isNotBlank(branchName)) {
				return branchPage.getPath();
			}
		}
		return null;
	}

	private static void fillContent(Node node) throws RepositoryException {
		String nodeTitle = node.getName().replaceFirst("[0-9_]*", "");
		Node contentNode = node.addNode(JcrConstants.JCR_CONTENT);
		contentNode.setProperty(NameConstants.NN_TEMPLATE, GithubConstants.IMPORTED_GITHUB_TEMPLATE);
		contentNode.setProperty(JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY, GithubConstants.IMPORTED_GITHUB_PAGE_RESOURE_TYPE);
		contentNode.setProperty(Constants.CQ_DESIGN_PATH, Constants.UDP_DESIGN_PATH);
		contentNode.setProperty(Constants.JCR_TITLE, nodeTitle);
		contentNode.setProperty(GithubConstants.PAGE_IMPORTED, true);
		contentNode.setProperty(GithubConstants.IS_FOLDER, true);
	}
	
	private static String getFileFolder(String filePath) {
		if(filePath.contains("/")) {
			return removeFirstSlash(filePath.substring(0, filePath.lastIndexOf("/") + 1));
		}
		return StringUtils.EMPTY;
	}
	
	
	public static String removeSlashAtEnd(String path) {
		if(path.endsWith("/")) {
			return path.substring(0, path.length() - 1);
		}
		return path;
	}
	
	public static boolean isBranch(Resource resource, String branch) {
		ValueMap vm = resource.adaptTo(ValueMap.class);
		String resourceBranch = vm.get(GithubConstants.BRANCH, String.class);
		if(StringUtils.isNotBlank(resourceBranch)) {
			return resourceBranch.equals(branch);
		}
		return false;
	}
	
	public static String getBranchPageName(String rootPath, String branchName) {
		return rootPath + "/" + IONodeUtils.getBranchPageName(branchName);
	}
	
	private static String getParentPath(String path) {
		if(StringUtils.isBlank(path) || !path.contains("/")) {
			return StringUtils.EMPTY;
		}
		return path.substring(0, path.lastIndexOf("/"));
	}
	
	public static String extractName(String path) {
		if(StringUtils.isBlank(path)) {
			return StringUtils.EMPTY;
		}
		if(!path.contains("/")) {
			return path;
		}
		return path.substring(path.lastIndexOf("/") + 1);
	}
}

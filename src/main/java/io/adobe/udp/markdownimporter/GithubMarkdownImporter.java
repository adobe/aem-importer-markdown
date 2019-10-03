/**
 * Copyright 2017 Adobe Systems Incorporated. All rights reserved.
 * This file is licensed to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy
 * of the License at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.adobe.udp.markdownimporter;

import io.adobe.udp.markdownimporter.flexmarkExtensions.GithubHostedImagePrefixer;
import io.adobe.udp.markdownimporter.services.GithubLinkService;
import io.adobe.udp.markdownimporter.services.GithubLinkServiceImpl;
import io.adobe.udp.markdownimporter.services.MarkdownParserService;
import io.adobe.udp.markdownimporter.utils.GithubConstants;
import io.adobe.udp.markdownimporter.utils.GithubRequests;
import io.adobe.udp.markdownimporter.utils.IONodeUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.jcr.RepositoryException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

public class GithubMarkdownImporter implements MarkdownImporter {

	private static final String PAGES_PROPERTY = "pages";

	private MarkdownParserService markdownParserService;
	
	private GithubLinkService githubLinkService;	
	
	private InputConfig config;
	
	private Map<String, PageData> pages;
	private Map<String, File> images;
	
	public GithubMarkdownImporter(MarkdownParserService markdownParserService, GithubLinkService githubLinkService, InputConfig config) {
		this.markdownParserService = markdownParserService;
		this.githubLinkService = githubLinkService;
		this.pages = new HashMap<>();
		this.images = new HashMap<>();
		this.config = config;
	}
	

	public void processGithubPage() {
		boolean branchSuccess = true;
		boolean pageSuccess = true;
		try {
			System.out.println("processing import");
			if (StringUtils.isNotEmpty(config.getGithubUrl())) {
				String repositoryUrl = config.getRepositoryUrl();
				List<String> branches = config.getBranches();
				for(String branch : branches) {
					boolean first = true;
					GithubLinkService githubLinkService = new GithubLinkServiceImpl();
					GithubData githubData = githubLinkService.getGithubData(repositoryUrl, branch, config);
					if(githubData == null) {
						System.out.println("Wrong configuration");
						continue;
					}
//					if(!isImportValid(githubData, shaMap.get(branch))) {
//						continue;
//					}
					List<String> configPages = getConfigPages(config);
					boolean hasPages = configPages.size() > 0;
					Set<String> allFiles = getAllFiles(configPages, githubData, githubLinkService);
					BranchRootInfo branchRootInfo = BranchRootInfo.createBranchRootInfo(config.getRootPath(), configPages, githubData.getRepositoryBranch(),
							allFiles, first, hasPages);
					String branchPath = IONodeUtils.getBranchPageName(config.getRootPath(), branchRootInfo.getBranchPageName() + "_" + githubData.getRepositoryBranch());
					Set<String> files = getFiles(branchPath, configPages, githubData, configPages, branchRootInfo, githubLinkService);
					branchSuccess = createBranchPage(config.getRootPath(), githubData, branchRootInfo, hasPages, githubLinkService, config, allFiles);
					files.remove(branchRootInfo.getRootPath() + GithubConstants.MARKDOWN_EXTENSION);
					pageSuccess = saveGithubPages(branchPath, files,  githubData,  branchRootInfo, config, allFiles);
//					first= false;
				}
//	
//				processResult(githubPageNode, branchSuccess, pageSuccess);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
	
	private boolean createBranchPage(String rootPage, GithubData githubData, BranchRootInfo rootInfo,
				boolean hasPages, GithubLinkService githubLinkService, InputConfig config, Set<String> allFiles) throws RepositoryException, IOException {
		boolean success;
		String branchPage = getBranchPagePath( rootPage, githubData.getRepositoryBranch(), rootInfo,
				hasPages);
		String readmeFileUrl;
		if(hasPages) {
			readmeFileUrl = githubLinkService.mapPathToUrl(rootInfo.getRootPath() + GithubConstants.MARKDOWN_EXTENSION, githubData);			
		} else {
			String getReadmeApiUrl = githubLinkService.getReadmeUrl(githubData);
			readmeFileUrl = GithubRequests.getFileUrl(getReadmeApiUrl, githubData.getToken());
		}
		InputStreamReader reader = saveMarkdownFile(branchPage, addCacheKiller(readmeFileUrl));
		List<String> imagesList = new ArrayList<>();
		String pageUrl = githubLinkService.getFileBlobUrl(githubData, rootInfo.getRootPath());
		GithubHostedImagePrefixer urlPrefixer = createUrlPrefixer(branchPage + "/" + GithubConstants.IMAGES, githubData,
				rootInfo, branchPage, pageUrl, allFiles, rootInfo.getRootPath());
		MarkdownPageData pageData = new MarkdownPageData(config.getPageResourceType(), config.getPageTemplate(), config.getTemplateMapper(), config.getDesignPath());
		pageData = markdownParserService.parseMarkdownFile(reader, pageData, imagesList, urlPrefixer);
		pageData.setGithubUrl(pageUrl);
		pageData.setBranch(githubData.getRepositoryBranch());
		if(StringUtils.isBlank(pageData.getTitle())) {
			pageData.setTitle(IONodeUtils.extractName(branchPage));
		}
        this.pages.put(branchPage, pageData);
		collectImages(branchPage, githubData, imagesList,"", images);
//		setImported(branchPage, githubData, rootInfo.getRootGithubFile());

//		return success;
		return true;
		
	}
//
	private void collectImages(String rootPage, GithubData githubData,
			List<String> images, String filePath, Map<String, File> imagesMap) throws RepositoryException, IOException {
		Map<String, String> pathToUrl = new HashMap<>();
		if(images != null && images.size() > 0) {
			new File(rootPage + "/images").mkdirs();
		}
		for(String path : images) {
			String rawPath = IONodeUtils.removeParams(path);
			pathToUrl.put(GithubConstants.IMAGES + rawPath, getImageUrl(rawPath, githubData, filePath));
		}
		for(Map.Entry<String, String> pathEntry : pathToUrl.entrySet()) {
			saveImage(rootPage, pathEntry.getKey(), pathEntry.getValue(), imagesMap);
		}
		
	}

	private String getImageUrl(String path, GithubData githubData, String filePath) throws MalformedURLException, RepositoryException {
			if(path.startsWith("/")) {
				return githubData.getGithubUrl() + path;
			} else {
				String imagePath = getImagePath(filePath) + path;
				return githubLinkService.mapPathToUrl(imagePath, githubData);
			}
	}
	
	private String getImagePath(String path) {
		if(StringUtils.isNotBlank(path)) {
			String file = IONodeUtils.removeFirstSlash(path);
			if(!file.contains("/")) {
				return StringUtils.EMPTY;
			}
			return file.substring(0,file.lastIndexOf("/") + 1);
		}
		return path;
	}
	
	private void saveImage(String rootPage, String filePath, String fileUrl, Map<String, File> imagesMap) throws RepositoryException {
		try {
			URL file = new URL(fileUrl);
			File tmpImage = File.createTempFile("mdimage", ".tmp");
			FileUtils.copyURLToFile(file, tmpImage);
			imagesMap.put(rootPage + "/" + filePath, tmpImage);

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	private String getBranchPagePath(String rootPage,
			String branch, BranchRootInfo rootInfo, boolean hasPages) {
		String branchPath;
		if(hasPages) {
			branchPath =IONodeUtils. getBranchPageName(rootPage, rootInfo.getBranchPageName() + "_" + branch);
		} else {
			branchPath = IONodeUtils.getBranchPageName(rootPage, branch + "_" + branch);
		}
		String parent = IONodeUtils.replaceDotsInPath(branchPath.substring(0, branchPath.lastIndexOf("/")));
		String branchPageName = IONodeUtils.getBranchPageName(rootInfo) + "_" + branch;
		return parent + "/" + branchPageName;
	}

	private List<String> getConfigPages(InputConfig config) {
		List<String> paths = new ArrayList<>();
			for(String path : config.getPages()) {
				paths.add(IONodeUtils.removeFirstSlash(path));
			}
			return paths;
		}
	
	private Set<String> getFiles(String branchPath, List<String> paths, GithubData githubData, List<String> configPages, 
			BranchRootInfo rootInfo, GithubLinkService githubLinkService) throws MalformedURLException{
		Set<String> filesToImport = new TreeSet<>();
			for(String startPath : paths) {
				if(startPath.contains("*")) {
					addGlobbedPaths(filesToImport, githubLinkService.getPathInRepository(startPath.replaceAll("\\*", ""), githubData), githubData, githubLinkService);
				} else {
					filesToImport.add(githubLinkService.getPathInRepository(startPath, githubData));
				}
			} 
		return filesToImport;
	}
	
	private Set<String> getAllFiles(List<String> paths, GithubData githubData, GithubLinkService githubLinkService) {
		Set<String> files = new TreeSet<>();
		if(!config.isLocalCheckout()) {
			for(String startPath : paths) {
				if(startPath.contains("*")) {
					addGlobbedPaths(files, githubLinkService.getPathInRepository(startPath.replaceAll("\\*", ""), githubData), githubData, githubLinkService);
				} else {
					files.add(githubLinkService.getPathInRepository(startPath, githubData));
				}
			}
		} else {
			for(String startPath : paths) {
				if(startPath.contains("*")) {
					addGlobbedPaths(files, githubLinkService.getPathInRepository(startPath.replaceAll("\\*", ""), githubData), githubData, githubLinkService);
				} else {
					files.add(githubLinkService.getPathInRepository(startPath, githubData));
				}
			}
		}
		return files;
	}

	private void addGlobbedPaths(Set<String> filesToImport, String startPath,
			GithubData githubData, GithubLinkService githubLinkService) {
		try {
			String gitTreeRequest = githubLinkService.getGithubTreeUrl(startPath, githubData);
			JSONObject treeResponse = GithubRequests.executeTreeRequest(gitTreeRequest, githubData.getToken(), true);
			collectPagesFromTree(treeResponse, githubData, filesToImport, IONodeUtils.stripFromExtension(startPath));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}

	private void collectPagesFromTree(JSONObject treeResponse,
			GithubData githubData, Set<String> filesToImport, String startPath) {
		try {
			if(treeResponse.getBoolean("truncated")) {
				JSONArray tree = treeResponse.getJSONArray("tree");
				collectDirectChildren(tree, githubData, filesToImport, startPath);
				for(int i = 0; i < tree.length(); i++) {
					JSONObject entry = tree.getJSONObject(i);
					if(isEligibleToIterate(entry)) {
						String url = entry.getString(GithubConstants.URL);
						String path = startPath + "/" + entry.getString("path");
						JSONObject jsonResponse = GithubRequests.executeTreeRequest(url, githubData.getToken(), true);
						collectPagesFromTree(jsonResponse, githubData, filesToImport, path);
					}
				}	
			} else {
					collectTreeItems(treeResponse.getJSONArray("tree"), githubData, filesToImport, startPath);
			}
		} catch (JSONException e) {
			System.out.println(e.getMessage());
		}
		
	}

	private boolean isEligibleToIterate(JSONObject entry) throws JSONException {
		String path = entry.getString("path");
		String type = entry.getString("type");
		return type.equals("tree") && !path.contains("/");
	}

	private void collectDirectChildren(JSONArray tree, GithubData githubData,
			Set<String> filesToImport, String startPath) throws JSONException {
		for(int i = 0; i < tree.length(); i++) {
			JSONObject entry = tree.getJSONObject(i);
			String path = entry.getString("path");
			if(!path.contains("/") && path.toLowerCase().endsWith(".md")) {
				filesToImport.add(startPath + "/" + path);
			}
		}
		
	}

	private void collectTreeItems(JSONArray tree,
			GithubData githubData, Set<String> filesToImport, String startPath) throws JSONException {
		for(int i = 0; i < tree.length(); i++) {
			JSONObject entry = tree.getJSONObject(i);
			String path = entry.getString("path");
			if(path.toLowerCase().endsWith(".md")) {
				filesToImport.add(startPath + "/" + path);
			}
		}
		
	}

	private boolean saveGithubPages(String rootPath, Set<String> files,  GithubData githubData,
			 BranchRootInfo rootInfo, InputConfig config, Set<String> allFiles) throws RepositoryException {
		boolean success = true;
		for(String file : files) {
			try {
				boolean saved = createGithubPage(rootPath, file, rootInfo, githubData, files, config, allFiles);
				if(!saved) {
					success = false;
				}
			} catch(Exception e) {
				System.out.println(e.getMessage());
				success = false;
			}
		}
		return success;
		
	}

	private boolean createGithubPage(String rootPath, String githubFilePath, BranchRootInfo rootInfo, GithubData githubData, Set<String> files,
			InputConfig config, Set<String> allFiles) throws IOException, RepositoryException {
		boolean success;
		githubFilePath = IONodeUtils.removeFirstSlash(githubFilePath);
		String internalFilePath = rootInfo.getInternalPath(githubFilePath);
		internalFilePath = IONodeUtils.removeFirstSlash(internalFilePath);
		String parentPath = IONodeUtils.replaceDotsInPath(rootPath + "/" + internalFilePath.substring(0, internalFilePath.lastIndexOf("/") + 1));
		String pageName = IONodeUtils.replaceDotsInPath(internalFilePath.substring(internalFilePath.lastIndexOf("/") + 1));
		String url = githubLinkService.mapPathToUrl(githubFilePath, githubData);
		String filePath = parentPath + IONodeUtils.trimName(pageName);
		InputStreamReader reader = saveMarkdownFile(filePath, addCacheKiller(url));
		FolderPageData folderPageData = new FolderPageData(config.getPageResourceType(), config.getPageTemplate(), config.getDesignPath());
		IONodeUtils.addPlaceHolderTemplate(rootPath, filePath, githubFilePath, files, pages, config);
		List<String> imagesList = new ArrayList<>();
		String fileBlobUrl = githubLinkService.getFileBlobUrl(githubData, githubFilePath);
		GithubHostedImagePrefixer urlPrefixer = createUrlPrefixer(filePath + "/" + GithubConstants.IMAGES, githubData,
				rootInfo, filePath, fileBlobUrl, allFiles, githubFilePath);
		MarkdownPageData pageData = new MarkdownPageData(config.getPageResourceType(), config.getPageTemplate(), config.getTemplateMapper(), config.getDesignPath());
		pageData = markdownParserService.parseMarkdownFile(reader, pageData, imagesList, urlPrefixer);
		pageData.setGithubUrl(fileBlobUrl);
		if(StringUtils.isBlank(pageData.getTitle())) {
			pageData.setTitle(IONodeUtils.removeMDExtensionFromPath(pageName));
		}
		collectImages(filePath, githubData, imagesList, githubFilePath, this.images);
		this.pages.put(filePath, pageData);
//		setImported(filePath, githubData, githubFilePath);
		return true;
	}

	private GithubHostedImagePrefixer createUrlPrefixer(String urlPrefix, GithubData githubData, 
			BranchRootInfo rootInfo, String pagePath, String fileBlobUrl, Set<String> allFiles, String originalFilePath)  {
		return new GithubHostedImagePrefixer(urlPrefix, githubData, rootInfo, pagePath, fileBlobUrl, allFiles, originalFilePath);
	}

	private InputStreamReader saveMarkdownFile(String pagePath, String url) throws IOException {
		URL file = new URL(url);
//		File markdownFile = new File(pagePath + "/_jcr_content/markdown");
//		FileUtils.copyURLToFile(file, markdownFile);
		return new InputStreamReader(file.openStream());
	}

	private String addCacheKiller(String url) {
		if(url.contains("?")) {
			return url + "&ck=" + Calendar.getInstance().getTimeInMillis();
		} else {
			return url + "?ck=" + Calendar.getInstance().getTimeInMillis();
		}
	}
	

	public Map<String, PageData> getPageData() {
		return this.pages;
	}

	public Map<String, File> getImages() {
		return images;
	}
}

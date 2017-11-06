/**
 * Copyright 2017 Adobe Systems Incorporated. All rights reserved.
 * This file is licensed to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy
 * of the License at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.adobe.udp.markdownimporter;

import io.adobe.udp.markdownimporter.flexmarkExtensions.GithubHostedImagePrefixer;
import io.adobe.udp.markdownimporter.services.FileSystemPathService;
import io.adobe.udp.markdownimporter.services.FileSystemPathServiceImpl;
import io.adobe.udp.markdownimporter.services.GithubLinkService;
import io.adobe.udp.markdownimporter.services.MarkdownParserService;
import io.adobe.udp.markdownimporter.utils.GithubConstants;
import io.adobe.udp.markdownimporter.utils.IONodeUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.jcr.RepositoryException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.lang3.StringUtils;

public class WorkdirMarkdownImporter implements MarkdownImporter  {

	private MarkdownParserService markdownParserService;
	
	private GithubLinkService githubLinkService;	
	
	private InputConfig config;
	
	private FileSystemPathService pathService;
	
	private Map<String, PageData> pages;
	private Map<String, File> images;
	
	public WorkdirMarkdownImporter(MarkdownParserService markdownParserService, GithubLinkService githubLinkService,
			FileSystemPathService pathService, InputConfig config) {
		this.markdownParserService = markdownParserService;
		this.githubLinkService = githubLinkService;
		this.pathService = pathService; 
		this.pages = new HashMap<String, PageData>();
		this.images = new HashMap<String, File>();
		this.config = config;
	}
	

	public void processGithubPage() {
		boolean branchSuccess = true;
		boolean pageSuccess = true;
		try {
			System.out.println("processing import");
				List<String> dirs = config.getWorkingDirs();
				for(String dir : dirs) {
					boolean first = true;
					String[] dirData = dir.split(":",2);
					if(dirData == null || dirData.length < 2) {
						System.out.println("Wrong dir entry, it needs to contain path and corresponding branch separated with semicolon");
						continue;
					}
					String branch = dirData[0];
					String dirPath = dirData[1];
					FileSystemPathService pathService = new FileSystemPathServiceImpl();
					List<String> configPages = getConfigPages(config);
					boolean hasPages = configPages.size() > 0;
					BranchRootInfo branchRootInfo = BranchRootInfo.createBranchRootInfo(config.getRootPath(), configPages, branch,
							getAllFiles(configPages, dirPath, pathService), first, hasPages);
					String branchPath = IONodeUtils.getBranchPageName(config.getRootPath(), branchRootInfo.getBranchPageName() + "_" + branch);
					Set<String> files = getAllFiles(configPages, dirPath, pathService);
					GithubData githubData = pathService.createGithubData(branch, config);
					branchSuccess = createBranchPage(config.getRootPath(), githubData, dirPath, branch, branchRootInfo, hasPages, pathService, config);
					files.remove(branchRootInfo.getRootPath() + GithubConstants.MARKDOWN_EXTENSION);
					pageSuccess = saveGithubPages(branchPath, files,  githubData,  branchRootInfo, dirPath, config);
//					first= false;
				}
//	
//				processResult(githubPageNode, branchSuccess, pageSuccess);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
	
	private boolean createBranchPage(String rootPage, GithubData githubData,  String dirPath, String branch, BranchRootInfo rootInfo,
				boolean hasPages, FileSystemPathService pathService, InputConfig config) throws RepositoryException, IOException {
		boolean success;
		String branchPage = getBranchPagePath( rootPage, branch, rootInfo,
				hasPages);
		String readmeFileLocation;
		if(hasPages) {
			readmeFileLocation = pathService.getLocalFilePath(rootInfo.getRootPath() + GithubConstants.MARKDOWN_EXTENSION, dirPath);			
		} else {
			readmeFileLocation = dirPath + "/README.md"; 
		}
		InputStreamReader reader = saveMarkdownFile(readmeFileLocation);
		List<String> imagesList = new ArrayList<String>();
		String pageUrl = pathService.getFileBlobUrl(config, rootInfo.getRootPath(), dirPath);
		GithubHostedImagePrefixer urlPrefixer = createUrlPrefixer(branchPage + "/" + GithubConstants.IMAGES, githubData,
				rootInfo, branchPage, pageUrl);
		MarkdownPageData pageData = new MarkdownPageData(config.getPageResourceType(), config.getPageTemplate(), config.getTemplateMapper(), config.getDesignPath());
		pageData = markdownParserService.parseMarkdownFile(reader, pageData, imagesList, urlPrefixer);
		pageData.setGithubUrl(pageUrl);
		pageData.setBranch(branch);
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
		Map<String, String> pathToUrl = new HashMap<String, String>();
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
		String branchPath = null;
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
		List<String> paths = new ArrayList<String>();
		if(config.getPages() != null) {
			for(String path : config.getPages()) {
				paths.add(IONodeUtils.removeFirstSlash(path));
			}
		}
			return paths;
		}
	
	private Set<String> getAllFiles(List<String> paths, String dirPath, FileSystemPathService pathService) {
		Set<String> files = new TreeSet<String>();
		for(String startPath : paths) {
			if(startPath.contains("*")) {
				addGlobbedPaths(files, pathService.getLocalFilePath(startPath.replaceAll("\\*", ""), dirPath), dirPath);
			} else {
				files.add(IONodeUtils.escapeBackslash(startPath));
			}
		}
		return files;
	}

	private void addGlobbedPaths(Set<String> filesToImport, String startPath, String dirPath) {
		try {
			IOFileFilter fileFilter = new SuffixFileFilter("md", IOCase.INSENSITIVE);
			Iterator<File> files = FileUtils.iterateFiles(new File(startPath), fileFilter, DirectoryFileFilter.DIRECTORY);
			while(files.hasNext()) {
				filesToImport.add(IONodeUtils.escapeBackslash(files.next().getPath().replace(dirPath + File.separator, "")));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}

	private boolean saveGithubPages(String rootPath, Set<String> files,  GithubData githubData,
			 BranchRootInfo rootInfo, String dirPath, InputConfig config) throws RepositoryException {
		boolean success = true;
		for(String file : files) {
			try {
				boolean saved = createGithubPage(rootPath, file, rootInfo, githubData, files, dirPath, config);
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

	private boolean createGithubPage(String rootPath, String githubFilePath, BranchRootInfo rootInfo, GithubData githubData,
			Set<String> files, String dirPath, InputConfig config) throws IOException, RepositoryException {
		boolean success;
		githubFilePath = IONodeUtils.removeFirstSlash(githubFilePath);
		String internalFilePath = rootInfo.getInternalPath(githubFilePath);
		internalFilePath = IONodeUtils.removeFirstSlash(internalFilePath);
		String parentPath = IONodeUtils.replaceDotsInPath(rootPath + "/" + internalFilePath.substring(0, internalFilePath.lastIndexOf("/") + 1));
		String pageName = IONodeUtils.replaceDotsInPath(internalFilePath.substring(internalFilePath.lastIndexOf("/") + 1));
		String filePath = parentPath + IONodeUtils.trimName(pageName);
		InputStreamReader reader = saveMarkdownFile(pathService.getLocalFilePath(githubFilePath, dirPath));
		IONodeUtils.addPlaceHolderTemplate(rootPath, filePath, githubFilePath, files, pages, config);
		List<String> imagesList = new ArrayList<String>();
		String fileBlobUrl = githubLinkService.getFileBlobUrl(githubData, githubFilePath);
		GithubHostedImagePrefixer urlPrefixer = createUrlPrefixer(filePath + "/" + GithubConstants.IMAGES, githubData,
				rootInfo, filePath, fileBlobUrl);
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
			BranchRootInfo rootInfo, String pagePath, String fileBlobUrl)  {
		return new GithubHostedImagePrefixer(urlPrefix, githubData, rootInfo, pagePath, fileBlobUrl);
	}

	private InputStreamReader saveMarkdownFile(String location) throws IOException {
		return new InputStreamReader(new FileInputStream(location));
	}

	public Map<String, PageData> getPageData() {
		return this.pages;
	}

	public Map<String, File> getImages() {
		return images;
	}
}

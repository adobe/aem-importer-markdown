package io.adobe.udp.markdownimporter.services;

import io.adobe.udp.markdownimporter.GithubData;
import io.adobe.udp.markdownimporter.InputConfig;

public interface FileSystemPathService {

	String getLocalFilePath(String replaceAll, String dirPath);

	String mapPathToLocation(String string, String dirPath);

	String getFileBlobUrl(InputConfig config, String rootPath, String dirPath);

	GithubData createGithubData(String branch, InputConfig config);

}

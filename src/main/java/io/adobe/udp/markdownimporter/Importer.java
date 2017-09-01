package io.adobe.udp.markdownimporter;

import io.adobe.udp.markdownimporter.mappings.MarkdownMappings;
import io.adobe.udp.markdownimporter.services.GithubLinkService;
import io.adobe.udp.markdownimporter.services.GithubLinkServiceImpl;
import io.adobe.udp.markdownimporter.services.MarkdownParserService;
import io.adobe.udp.markdownimporter.services.MarkdownParserServiceImpl;
import io.wcm.tooling.commons.contentpackagebuilder.ContentPackage;
import io.wcm.tooling.commons.contentpackagebuilder.ContentPackageBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class Importer 
{
    public static void main( String[] args ) throws IOException
    {
       if(args.length < 1) {
    	   System.out.println("Missing config file parameter");
    	   return;
       }
       
       File configFile = new File(args[0]);
       if(!configFile.exists()) {
    	   System.out.println("Config file does not exist");
       }
       FileInputStream fis = new FileInputStream(configFile);
       Yaml yaml = new Yaml(new Constructor(InputConfig.class));
       InputConfig inputConfig = (InputConfig) yaml.load(fis);
       MarkdownMappings.configure(inputConfig.getComponentMappings());
       MarkdownParserService markdownParserService = new MarkdownParserServiceImpl();
       GithubLinkService githubLinkService = new GithubLinkServiceImpl();
       MarkdownFileImportScheduler importer = new MarkdownFileImportScheduler(markdownParserService, githubLinkService);
       importer.processGithubPage(inputConfig);
       Map<String, PageData> pages = importer.getPageData();
       Map<String, File> images = importer.getImages();
       RootPageData root = new RootPageData(inputConfig);
       pages.put(inputConfig.getRootPath(), root);
       generatePackage(pages, images, inputConfig);
      System.out.println(inputConfig.getApiToken());
    }
    
    
    private static void generatePackage(Map<String, PageData> pages, Map<String, File> images, InputConfig config) throws IOException {
    	File zipFile = new File(config.getPackageName() +".zip");
    	
    	Map<String, Map<String, Object>> content = toContent(pages);

    	ContentPackageBuilder builder = new ContentPackageBuilder()
    	    .name(config.getPackageName())
    	    .group(config.getGroup())
    	    .version(config.getVersion())
    	    .rootPath(config.getRootPath());
    	try {
			builder.build(zipFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	try (ContentPackage contentPackage = builder.build(zipFile)) {
    		for(Map.Entry<String, Map<String, Object>> entry : content.entrySet()) {
    			contentPackage.addPage(entry.getKey(), entry.getValue());
    		}
    		for(Map.Entry<String, File> entry : images.entrySet()) {
    			contentPackage.addFile(entry.getKey(), entry.getValue(), "image/gif");
    		}
    	}

    }


	private static Map<String, Map<String, Object>> toContent(Map<String, PageData> pages) {
		Map<String, Map<String, Object>> content = new HashMap<String, Map<String, Object>>();
		for(Map.Entry<String, PageData> entry : pages.entrySet()) {
			content.put(entry.getKey(), entry.getValue().toContent());
		}
		return content;
	}
}

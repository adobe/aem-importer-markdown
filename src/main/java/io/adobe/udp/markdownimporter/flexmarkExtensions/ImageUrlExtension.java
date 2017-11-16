package io.adobe.udp.markdownimporter.flexmarkExtensions;

import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.html.renderer.NodeRenderer;
import com.vladsch.flexmark.html.renderer.NodeRendererFactory;
import com.vladsch.flexmark.util.options.DataHolder;
import com.vladsch.flexmark.util.options.DataKey;
import com.vladsch.flexmark.util.options.MutableDataHolder;

public class ImageUrlExtension implements HtmlRenderer.HtmlRendererExtension {
	
	public static final DataKey<UrlPrefixer> URL_CHANGER = new DataKey<UrlPrefixer>("urlprefixer", new GithubHostedImagePrefixer("", null, null, null, null, null, null));    
	
	    private ImageUrlExtension() {
	    }
	    
	    public static Extension create() {
	        return new ImageUrlExtension();
	    }

	    @Override
	    public void rendererOptions(MutableDataHolder options) {
	        
	    }
	    

	    @Override
	    public void extend(com.vladsch.flexmark.html.HtmlRenderer.Builder rendererBuilder, String rendererType) {
	        switch (rendererType) {
	            case "HTML":
	                rendererBuilder.nodeRendererFactory(new NodeRendererFactory() {
	                    @Override
	                    public NodeRenderer create(DataHolder options) {
	                        return new ImageRenderer(options);
	                    }
	                });
	                break;

	            case "JIRA":
	            case "YOUTRACK":
	                break;
	    }
	        
	    }

}

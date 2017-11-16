/**
 * Copyright 2017 Adobe Systems Incorporated. All rights reserved.
 * This file is licensed to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy
 * of the License at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.adobe.udp.markdownimporter.flexmarkExtensions;

import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.html.renderer.NodeRenderer;
import com.vladsch.flexmark.html.renderer.NodeRendererFactory;
import com.vladsch.flexmark.util.options.DataHolder;
import com.vladsch.flexmark.util.options.DataKey;
import com.vladsch.flexmark.util.options.MutableDataHolder;

public class UdpUrlExtension implements HtmlRenderer.HtmlRendererExtension {
	
	public static final DataKey<UrlPrefixer> URL_CHANGER = new DataKey<UrlPrefixer>("urlprefixer", new GithubHostedImagePrefixer("", null, null, null, null, null, null));    
	
	    private UdpUrlExtension() {
	    }
	    
	    public static Extension create() {
	        return new UdpUrlExtension();
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
	                        return new LinkRenderer(options);
	                    }
	                });
	                break;

	            case "JIRA":
	            case "YOUTRACK":
	                break;
	    }
	        
	    }

}
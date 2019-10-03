/**
 * Copyright 2017 Adobe Systems Incorporated. All rights reserved.
 * This file is licensed to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy
 * of the License at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.adobe.udp.markdownimporter.flexmarkExtensions;

import io.adobe.udp.markdownimporter.utils.Constants;

import java.util.List;

import com.vladsch.flexmark.ast.Image;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ast.NodeVisitor;
import com.vladsch.flexmark.ast.VisitHandler;

public class ImageVisitor {
    
	List<String> urls ;

   
    NodeVisitor visitor = new NodeVisitor(
            new VisitHandler<>(Image.class, ImageVisitor.this::visit)
    );
    
    public ImageVisitor(List<String> urls) {
    	this.urls = urls;
    }

    public void visit(Image image) {
    	if(!image.getUrl().startsWith(Constants.HTTP_PREFIX) && !image.getUrl().startsWith(Constants.HTTPS_PREFIX)) {
    		this.urls.add(image.getUrl().toString());
    	}
        visitor.visitChildren(image);
    }
    
    public List<String> getUrls() {
    	return this.urls;
    }
    
    public void collectImages(Node document) {
    	this.visitor.visitChildren(document);
    }

}
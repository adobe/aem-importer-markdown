/**
 * Copyright 2017 Adobe Systems Incorporated. All rights reserved.
 * This file is licensed to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy
 * of the License at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.adobe.udp.markdownimporter.flexmarkExtensions;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.vladsch.flexmark.ast.Link;
import com.vladsch.flexmark.html.HtmlWriter;
import com.vladsch.flexmark.html.renderer.LinkType;
import com.vladsch.flexmark.html.renderer.NodeRenderer;
import com.vladsch.flexmark.html.renderer.NodeRendererContext;
import com.vladsch.flexmark.html.renderer.NodeRenderingHandler;
import com.vladsch.flexmark.html.renderer.ResolvedLink;
import com.vladsch.flexmark.util.options.DataHolder;

public class LinkRenderer implements NodeRenderer {
    
	 private UrlPrefixer prefixer;
	    
	    public LinkRenderer(DataHolder options) {
	        this.prefixer = ImageUrlExtension.URL_CHANGER.getFrom(options);
	    }


    public Set<NodeRenderingHandler<?>> getNodeRenderingHandlers() {
        return new HashSet<>(Collections.singletonList(
                new NodeRenderingHandler<>(Link.class, LinkRenderer.this::render)
        ));
    }

    private void render(Link node, NodeRendererContext context, HtmlWriter html) {
        if (context.isDoNotRenderLinks()) {
            context.renderChildren(node);
        } else {
            ResolvedLink resolvedLink = context.resolveLink(LinkType.LINK, node.getUrl().unescape(), null);

            html.attr("href", prefixer.prefix(resolvedLink.getUrl()));
            if (node.getTitle().isNotNull()) {
                html.attr("title", node.getTitle().unescape());
            }
            html.srcPos(node.getChars()).withAttr(resolvedLink).tag("a");
            context.renderChildren(node);
            html.tag("/a");
        }
    }

}

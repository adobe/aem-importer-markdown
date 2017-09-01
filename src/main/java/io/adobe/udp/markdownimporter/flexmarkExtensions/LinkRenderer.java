package io.adobe.udp.markdownimporter.flexmarkExtensions;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.vladsch.flexmark.ast.Link;
import com.vladsch.flexmark.html.CustomNodeRenderer;
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
        return new HashSet<>(Arrays.asList(
	                new NodeRenderingHandler<>(Link.class, new CustomNodeRenderer<Link>() {
	                    @Override
	                    public void render(Link node, NodeRendererContext context, HtmlWriter html) {
	                        LinkRenderer.this.render(node, context, html);
	                    }
	                })
                ));
    };

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

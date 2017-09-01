package io.adobe.udp.markdownimporter.flexmarkExtensions;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.vladsch.flexmark.ast.Image;
import com.vladsch.flexmark.ast.util.TextCollectingVisitor;
import com.vladsch.flexmark.html.CustomNodeRenderer;
import com.vladsch.flexmark.html.HtmlWriter;
import com.vladsch.flexmark.html.renderer.LinkType;
import com.vladsch.flexmark.html.renderer.NodeRenderer;
import com.vladsch.flexmark.html.renderer.NodeRendererContext;
import com.vladsch.flexmark.html.renderer.NodeRenderingHandler;
import com.vladsch.flexmark.html.renderer.ResolvedLink;
import com.vladsch.flexmark.util.html.Escaping;
import com.vladsch.flexmark.util.options.DataHolder;

public class ImageRenderer implements NodeRenderer {
    
    private UrlPrefixer prefixer;
    
    public ImageRenderer(DataHolder options) {
        this.prefixer = ImageUrlExtension.URL_CHANGER.getFrom(options);
    }


    public Set<NodeRenderingHandler<?>> getNodeRenderingHandlers() {
        return new HashSet<>(Arrays.asList(
	                new NodeRenderingHandler<>(Image.class, new CustomNodeRenderer<Image>() {
	                    @Override
	                    public void render(Image node, NodeRendererContext context, HtmlWriter html) {
	                        ImageRenderer.this.render(node, context, html);
	                    }
	                })
                ));
    };

    private void render(final Image node, final NodeRendererContext context, HtmlWriter html) {
        if (!context.isDoNotRenderLinks()) {
            String altText = new TextCollectingVisitor().collectAndGetText(node);

            ResolvedLink resolvedLink = context.resolveLink(LinkType.IMAGE, node.getUrl().unescape(), null);
            String url = prefixer.prefix(resolvedLink.getUrl());

            if (!node.getUrlContent().isEmpty()) {
                // reverse URL encoding of =, &
                String content = Escaping.percentEncodeUrl(node.getUrlContent()).replace("+", "%2B").replace("%3D", "=").replace("%26", "&amp;");
                url += content;
            }

            html.attr("src", url);
            html.attr("alt", altText);
            if (node.getTitle().isNotNull()) {
                html.attr("title", node.getTitle().unescape());
            }
            html.srcPos(node.getChars()).withAttr(resolvedLink).tagVoid("img");
        }
    }

}

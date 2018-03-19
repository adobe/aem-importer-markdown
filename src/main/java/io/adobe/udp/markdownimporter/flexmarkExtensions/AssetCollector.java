package io.adobe.udp.markdownimporter.flexmarkExtensions;

import io.adobe.udp.markdownimporter.utils.Constants;

import java.util.List;

import com.vladsch.flexmark.ast.Link;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ast.NodeVisitor;
import com.vladsch.flexmark.ast.VisitHandler;
import com.vladsch.flexmark.ast.Visitor;

public class AssetCollector {
	
	private static final String EXTENSIONS = ".*(pdf|xls|xlsx|doc|docx)$";
	List<String> urls ;

	   
    NodeVisitor visitor = new NodeVisitor(
            new VisitHandler<Link>(Link.class, new Visitor<Link>() {
                @Override
                public void visit(Link link) {
                    AssetCollector.this.visit(link);
                }
            })
    );
    
    public AssetCollector(List<String> urls) {
    	this.urls = urls;
    }

    public void visit(Link link) {
    	if(!link.getUrl().startsWith(Constants.HTTP_PREFIX) && !link.getUrl().startsWith(Constants.HTTPS_PREFIX)
    			&& link.getUrl().toString().matches(EXTENSIONS)) {
    		this.urls.add(link.getUrl().toString());
    	}
        visitor.visitChildren(link);
    }
    
    public List<String> getUrls() {
    	return this.urls;
    }
    
    public void collectAssets(Node document) {
    	this.visitor.visitChildren(document);
    }
}

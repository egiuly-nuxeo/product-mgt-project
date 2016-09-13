package com.mycompany.productmgt;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.ModuleRoot;

@WebObject(type="myWebApp")
@Produces("text/html;charset=UTF-8")
@Path("product-price")
public class MyWebAppRoot extends ModuleRoot {
	
	@GET
	public String doGet() {
	  return "Welcome to the Product Price REST API. Please enter a product path as URL. For instance: "
			  + "http://localhost:8080/nuxeo/site/product-price/default-domain/workspaces/Workshop/Dress";
	}
	
	@GET
	@Path("{path: ([^/s]+/)(.*)}")
	public String doGet(@PathParam("path") String path) {
		try {
			PathRef pathRef = new PathRef(path);
	    	DocumentModel doc = ctx.getCoreSession().getDocument(pathRef);
	    	ProductAdapter product = doc.getAdapter(ProductAdapter.class);
	    	return Double.toString(product.getPrice());
		} catch (Exception e) {
			return "No document found for path " + path + ".";
		}
	}
	
    @PUT
    @Path("{path: ([^/s]+/)(.*)}")
    public String doPut(@PathParam("path") String path) {
    	try {
	    	PathRef pathRef = new PathRef(path);
	    	DocumentModel doc = ctx.getCoreSession().getDocument(pathRef);
	    	ProductAdapter product = doc.getAdapter(ProductAdapter.class);
	    	double oldPrice = product.getPrice();
	    	ProductUpdater.run(doc);
	    	double newPrice = product.getPrice();
	    	return "The price of the product of path " + path
    			+ " has just changed from " + oldPrice + " to " + newPrice + ".";
    	} catch (Exception e){
    		return "Could not update the price for document of path " + path + ".";
    	}
    }
}
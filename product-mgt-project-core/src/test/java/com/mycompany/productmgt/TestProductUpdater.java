package com.mycompany.productmgt;

import java.util.Calendar;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.automation.test.AutomationFeature;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.test.DefaultRepositoryInit;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;


@RunWith(FeaturesRunner.class)
@Features({AutomationFeature.class, ProductFeature.class})
@RepositoryConfig(init = DefaultRepositoryInit.class, cleanup = Granularity.METHOD)
public class TestProductUpdater {

    @Inject
    protected CoreSession coreSession;

    @Inject
    protected AutomationService automationService;

    @Test
    public void shouldCallTheOperation() throws OperationException {
    	OperationContext ctx = new OperationContext(coreSession);
    	
    	DocumentModel product1 = coreSession.createDocumentModel("/", "Dress", "product");
    	product1 = coreSession.createDocument(product1);
    	product1.setPropertyValue("product:price", 19.99);
    	PathRef pathRef1 = new PathRef(product1.getPathAsString());
    	
    	DocumentModel product2 = coreSession.createDocumentModel("/", "Jacket", "product");
    	product2 = coreSession.createDocument(product2);
    	product2.setPropertyValue("product:price", 220.00);
    	PathRef pathRef2 = new PathRef(product2.getPathAsString());
    	Calendar creationDate = Calendar.getInstance();
    	creationDate.add(Calendar.MONTH, -12); 
    	product2.setPropertyValue("dc:created", creationDate);
    	
    	coreSession.save();
    	
    	ctx.setInput(product1);
        automationService.run(ctx, ProductUpdater.ID);
        product1 = coreSession.getDocument(pathRef1);
        Assert.assertEquals(21.99, product1.getPropertyValue("product:price"));
        
    	ctx.setInput(product2);
    	automationService.run(ctx, ProductUpdater.ID);
        product2 = coreSession.getDocument(pathRef2);
        Assert.assertEquals(176.00, product2.getPropertyValue("product:price"));
    }
    
    @Test
    public void shouldCallTheOperationAndThrowAnException() throws OperationException {
    	DocumentModel product = coreSession.createDocumentModel("/", "NotAProductFile", "File");
    	product = coreSession.createDocument(product);
    	coreSession.save();

    	OperationContext ctx = new OperationContext(coreSession);
    	ctx.setInput(product);
        
        try {
        	automationService.run(ctx, ProductUpdater.ID);
        	Assert.fail("A NuxeoException is expected, but no one found.");
        } catch (Exception e) {
        	// The NuxeoException is wrapped into an OperationException wrapped into a TraceException
            Assert.assertEquals(NuxeoException.class, e.getCause().getCause().getClass());
        }
    }
}

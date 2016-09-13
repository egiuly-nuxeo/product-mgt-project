package com.mycompany.productmgt;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.test.PlatformFeature;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
	
@RunWith(FeaturesRunner.class)
@Features({ ProductFeature.class, PlatformFeature.class })
public class TestProductService {
	@Inject
	CoreSession coreSession;
	
    @Inject
    protected ProductService productService;

    @Test
    public void testService() {
        Assert.assertNotNull(productService);
    }
    
    @Test
    public void testComputePrice() {
    	DocumentModel doc = coreSession.createDocumentModel("/", "Dress", "product");
    	ProductAdapter product = doc.getAdapter(ProductAdapter.class);
    	
    	doc = coreSession.createDocument(doc);
    	Assert.assertEquals(0.99, productService.computePrice(product), 0.009);
    	
    	product.setPrice(19.99);
    	product.save();
    	Assert.assertEquals(21.99, productService.computePrice(product), 0.009);
    	
    	Calendar creationDate = Calendar.getInstance();
    	creationDate.add(Calendar.MONTH, -10); 
    	product.setCreationDate(creationDate);
    	product.save();
    	Assert.assertEquals(15.99, productService.computePrice(product), 0.009);
    }
}

package com.mycompany.productmgt;

import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.runtime.api.Framework;

/**
 *
 */
@Operation(
	id=ProductUpdater.ID, category=Constants.CAT_DOCUMENT, label="Update Product",
	description="Update the price of a given product document."
			+ "If the product has no price, its price will be set to 0.99."
			+ "If the product is older than 6 months, its price will decrease of 20%."
			+ "Else, its price will increase of 10%."
)
public class ProductUpdater {

    public static final String ID = "Document.ProductUpdater";

    @Context
    protected CoreSession coreSession;

    @OperationMethod
    public static void run(DocumentModel doc) throws NuxeoException {
    	ProductService productService = Framework.getService(ProductService.class);
		if (!(ProductAdapter.PRODUCT_TYPE.equals(doc.getType()))) {
            throw new NuxeoException("Operation works only with " + ProductAdapter.PRODUCT_TYPE + " document type.");
        }
		ProductAdapter product = doc.getAdapter(ProductAdapter.class);
    	double newPrice = productService.computePrice(product);
        product.setPrice(newPrice);
        product.save();
    }
}

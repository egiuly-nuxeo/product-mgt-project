package com.mycompany.productmgt;


import java.util.List;

import org.nuxeo.ecm.collections.core.adapter.Collection;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.EventListener;

import org.nuxeo.ecm.core.event.impl.DocumentEventContext;

public class NotSoldProduct implements EventListener {
  
	public static final String NOT_SOLD_FOLDER_PATH = "/not-sold/";
			
    @Override
    public void handleEvent(Event event) {
        EventContext ctx = event.getContext();
        if (!(ctx instanceof DocumentEventContext)) {
          return;
        }

        DocumentEventContext docCtx = (DocumentEventContext) ctx;
        DocumentModel doc = docCtx.getSourceDocument();
        String type = doc.getType();
        
        if (ProductAdapter.PRODUCT_TYPE.equals(type)) {
	        process(doc);
        }
    }
        
    public void process(DocumentModel doc) {
    	CoreSession coreSession = doc.getCoreSession();
        Collection product = doc.getAdapter(Collection.class);
        List<String> visualIds = product.getCollectedDocumentIds();
        
        for (String visualId : visualIds) {
        	IdRef idRef = new IdRef(visualId);
        	DocumentModel visual = coreSession.getDocument(idRef);
        	coreSession.move(
        		visual.getRef(),
        		new PathRef(NOT_SOLD_FOLDER_PATH),
        		visual.getName()
            );
        }
    }
}

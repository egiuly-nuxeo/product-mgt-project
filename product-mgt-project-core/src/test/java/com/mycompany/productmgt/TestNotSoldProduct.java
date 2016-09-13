package com.mycompany.productmgt;


import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.collections.api.CollectionManager;
import org.nuxeo.ecm.collections.core.adapter.Collection;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.EventService;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.ecm.core.event.impl.EventListenerDescriptor;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;

@RunWith(FeaturesRunner.class)
@Features({ ProductFeature.class })
@Deploy({"org.nuxeo.ecm.platform.collections.core"})
public class TestNotSoldProduct {

    protected final List<String> events = Arrays.asList("not-sold");

    @Inject
    protected EventService s;
    
    @Inject
	CoreSession coreSession;
    
    @Inject
	CollectionManager collectionManager;

    @Test
    public void listenerRegistration() {
        EventListenerDescriptor listener = s.getEventListener("not-sold-product");
        Assert.assertNotNull(listener);
        Assert.assertTrue(events.stream().allMatch(listener::acceptEvent));
    }
    
    @Test
    public void testHandleEvent() {
    	EventProducer eventProducer;
    	try {
    	    eventProducer = Framework.getService(EventProducer.class);
    	} catch (Exception e) {
    	    return;
    	}
    	
    	DocumentModel folder = coreSession.createDocumentModel("/", "not-sold", "Folder");
    	DocumentModel doc = coreSession.createDocumentModel("/", "Dress", "product");
    	DocumentModel visual = coreSession.createDocumentModel("/", "Dress (front view)", "visual");
    	folder = coreSession.createDocument(folder);
    	doc = coreSession.createDocument(doc);
    	visual = coreSession.createDocument(visual);
    	collectionManager.addToCollection(doc, visual, coreSession);
    	coreSession.save();
    	
    	DocumentEventContext ctx = new DocumentEventContext(coreSession, coreSession.getPrincipal(), doc);
    	Event event = ctx.newEvent("not-sold");
    	try {
    	    eventProducer.fireEvent(event);
    	} catch (Exception e) {
    	    return;
    	}
    	
    	Collection product = doc.getAdapter(Collection.class);
    	List<String> visualIds = product.getCollectedDocumentIds();
    	Assert.assertEquals(1, visualIds.size());
    	
    	IdRef idRef = new IdRef(visualIds.get(0));
    	DocumentModel collectedVisual = coreSession.getDocument(idRef);
    	Assert.assertEquals("/not-sold/Dress (front view)", collectedVisual.getPathAsString());
    }
}

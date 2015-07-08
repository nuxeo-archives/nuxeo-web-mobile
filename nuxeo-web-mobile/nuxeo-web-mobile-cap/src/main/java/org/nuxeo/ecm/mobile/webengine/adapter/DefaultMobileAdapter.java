package org.nuxeo.ecm.mobile.webengine.adapter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelComparator;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.Filter;
import org.nuxeo.ecm.core.api.impl.CompoundFilter;
import org.nuxeo.ecm.core.api.impl.FacetFilter;
import org.nuxeo.ecm.core.api.impl.LifeCycleFilter;
import org.nuxeo.ecm.mobile.webengine.document.MobileDocument;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.model.impl.DefaultAdapter;

/**
 * Default mobile adapter that exposes some usefull methods to other adapters.
 * 
 * @author <a href="mailto:akervern@nuxeo.com">Arnaud Kervern</a>
 */
public abstract class DefaultMobileAdapter extends DefaultAdapter {
    public static final Filter ONLY_VISIBLE_CHILDREN = new CompoundFilter(new FacetFilter(null,
            Collections.singletonList("HiddenInNavigation")), new LifeCycleFilter(null,
            Collections.singletonList("deleted")));

    /**
     * Get the current DocumentModel
     * 
     * @return
     */
    protected DocumentModel getDocumentModel() {
        MobileDocument doc = getMobileDocument();
        if (doc == null) {
            doc = getMobileDocument();
        }
        return doc.getDocument();
    }

    /**
     * Get the current MobileDocument
     * 
     * @return
     */
    protected MobileDocument getMobileDocument() {
        Object targetObject = ctx.getTargetObject();
        if (!(targetObject instanceof MobileDocument)) {
            throw new WebException("Target Object must be MobileDocument");
        }

        MobileDocument mobileDoc = (MobileDocument) targetObject;
        return mobileDoc;
    }

    public DocumentModelList getChildren() {
        CoreSession session = ctx.getCoreSession();
        Map<String, String> order = new HashMap<String, String>();
        order.put("title", "asc");

        DocumentModelComparator dmc = new DocumentModelComparator("dublincore", order);

        return session.getChildren(getDocumentModel().getRef(), null, ONLY_VISIBLE_CHILDREN, dmc);
    }
}

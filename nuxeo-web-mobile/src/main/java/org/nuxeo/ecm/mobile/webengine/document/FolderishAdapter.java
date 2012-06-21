/*
 * (C) Copyright 2012 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     bjalon
 */
package org.nuxeo.ecm.mobile.webengine.document;

import java.util.Collections;
import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.Filter;
import org.nuxeo.ecm.core.api.impl.CompoundFilter;
import org.nuxeo.ecm.core.api.impl.FacetFilter;
import org.nuxeo.ecm.core.api.impl.LifeCycleFilter;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.WebAdapter;
import org.nuxeo.ecm.webengine.model.impl.DefaultAdapter;
import org.nuxeo.runtime.api.Framework;

/**
 * @author <a href="mailto:bjalon@nuxeo.com">Benjamin JALON</a>
 * @since 5.5
 * 
 */
@WebAdapter(name = "folderish", type = "Folderish", targetType = "MobileDocument")
public class FolderishAdapter extends DefaultAdapter {

    public static final Filter ONLY_VISIBLE_CHILDREN = new CompoundFilter(
            new FacetFilter(null,
                    Collections.singletonList("HiddenInNavigation")),
            new LifeCycleFilter(null, Collections.singletonList("deleted")));

    @GET
    public Object doGet() {
        return getView("index");
    }
    
    @POST
    public Object doUpload() throws Exception {
        FormData form = ctx.getForm();
        Blob blob = form.getFirstBlob();
        if (blob == null) {
            throw new IllegalArgumentException(
                    "Could not find any uploaded file");
        }
        AutomationService as = Framework.getLocalService(AutomationService.class);
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("overwite", true);
        
        OperationContext subctx = new OperationContext(
                ctx.getCoreSession(), null);
        subctx.setInput(blob);
        subctx.put("currentDocument", getCurrentDocument().getPathAsString());
        
        as.run(subctx, "FileManager.Import", params);
        return Response.ok().build();
    }
    
    public DocumentModel getCurrentDocument() {
        Object targetObject = ctx.getTargetObject();
        if (!(targetObject instanceof MobileDocument)) {
            throw new WebException("Target Object must be MobileDocument");
        }
        MobileDocument doc = (MobileDocument) targetObject;
        return doc.getDocument();
    }
    
    public DocumentModelList getChildren() throws ClientException {
        CoreSession session = ctx.getCoreSession();
        return session.getChildren(getCurrentDocument().getRef(), null,
                ONLY_VISIBLE_CHILDREN, null);
    }

}

/*
 * (C) Copyright 2012 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     bjalon
 */
package org.nuxeo.ecm.mobile.webengine.adapter;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.model.WebAdapter;

/**
 * @author <a href="mailto:bjalon@nuxeo.com">Benjamin JALON</a>
 * @since 5.5
 */
@WebAdapter(name = "newDocument", type = "NewDocument", targetType = "MobileDocument")
public class NewDocumentAdapter extends DefaultMobileAdapter {

    private static final Log log = LogFactory.getLog(NewDocumentAdapter.class);

    @GET
    public Object doGet(@QueryParam("type") String docType) {
        CoreSession session = ctx.getCoreSession();

        DocumentModel doc;
        try {
            doc = session.createDocumentModel(docType);
        } catch (NuxeoException e) {
            log.error(e, e);
            throw new WebException(e.getMessage());
        }
        return getView("index").arg("doc", doc);
    }

    @POST
    public Object doPost() {
        return getView("index");
    }

}

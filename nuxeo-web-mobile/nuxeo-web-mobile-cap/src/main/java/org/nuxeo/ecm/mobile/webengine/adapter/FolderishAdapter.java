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

import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.core.Response;

import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.WebAdapter;
import org.nuxeo.runtime.api.Framework;

/**
 * @author <a href="mailto:bjalon@nuxeo.com">Benjamin JALON</a>
 * @since 5.5
 */
@WebAdapter(name = "folderish", type = "Folderish", targetType = "MobileDocument")
public class FolderishAdapter extends DefaultMobileAdapter {

    @GET
    public Object doGet() {
        return getView("index");
    }

    @POST
    public Object doUpload() throws OperationException {
        FormData form = ctx.getForm();
        Blob blob = form.getFirstBlob();
        if (blob == null) {
            throw new IllegalArgumentException("Could not find any uploaded file");
        }
        AutomationService as = Framework.getLocalService(AutomationService.class);
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("overwite", true);

        OperationContext subctx = new OperationContext(ctx.getCoreSession(), null);
        subctx.setInput(blob);
        subctx.put("currentDocument", getDocumentModel().getPathAsString());

        as.run(subctx, "FileManager.Import", params);
        return Response.ok().build();
    }
}

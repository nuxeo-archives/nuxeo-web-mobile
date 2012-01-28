/*
 * (C) Copyright 2006-2012 Nuxeo SAS (http://nuxeo.com/) and contributors.
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
 *     Benjamin JALON <bjalon@nuxeo.com>
 */

package org.nuxeo.ecm.mobile.webengine;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.mobile.webengine.document.MobileDocument;
import org.nuxeo.ecm.platform.url.api.DocumentView;
import org.nuxeo.ecm.platform.url.api.DocumentViewCodecManager;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.ModuleRoot;
import org.nuxeo.runtime.api.Framework;

import static org.nuxeo.ecm.mobile.ApplicationConstants.TARGET_URL_PARAMETER;

/**
 * @author Benjamin JALON <bjalon@nuxeo.com>
 *
 *         Entry point of the webengine application
 */
@Path("mobile")
@Produces("text/html;charset=UTF-8")
@WebObject(type = "MobileApplication")
public class MobileApplication extends ModuleRoot {

    private static final Log log = LogFactory.getLog(MobileApplication.class);

    private String nuxeoContextPath;

    private DocumentViewCodecManager codecManager;

    /**
     * Try to fetch document in targetURL parameter in URL if not this is the
     * Home binding
     *
     */
    @GET
    public Object doGet(@QueryParam(TARGET_URL_PARAMETER) String initialURL)
            throws Exception {
        if (initialURL != null) {
            DocumentView docView = getCodecManager().getDocumentViewFromUrl(
                    initialURL, true, getNuxeoContextPath() + "/");
            if (docView != null) {
                log.debug("Request from home: Target URL given into url parameter detected as a "
                        + "document request from url codec service: "
                        + initialURL);
                MobileDocument docResolved = new MobileDocument(ctx,
                        docView.getDocumentLocation().getDocRef());
                return docResolved.doGet();
            }
        }
        return getView("index");
    }

    @Path("auth")
    public Object doTraverseAuthentication() {
        return ctx.newObject("WebMobileAuthentication");
    }

    @Path("doc/{docId}")
    public Object doTraverseDocument(@PathParam("docId") String docId) {
        DocumentRef ref = new IdRef(docId);
        return new MobileDocument(ctx, ref);
    }

    private String getNuxeoContextPath() {
        if (nuxeoContextPath == null) {
            nuxeoContextPath = Framework.getProperty("org.nuxeo.ecm.contextPath");
        }
        return nuxeoContextPath;
    }

    private DocumentViewCodecManager getCodecManager() throws Exception {
        if (codecManager == null) {
            codecManager = Framework.getService(DocumentViewCodecManager.class);
        }
        return codecManager;
    }

}

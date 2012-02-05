/*
 * (C) Copyright 2006-2008 Nuxeo SAS (http://nuxeo.com/) and contributors.
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
 *     bstefanescu
 *
 * $Id$
 */

package org.nuxeo.ecm.mobile.webengine.document;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.model.PropertyException;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.platform.preview.helper.PreviewHelper;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.model.ResourceType;
import org.nuxeo.ecm.webengine.model.WebContext;
import org.nuxeo.runtime.api.Framework;

import static org.nuxeo.ecm.mobile.ApplicationConstants.TARGET_URL_PARAMETER;

/**
 * This Class resolve a DocumentModel and expose differents restitutions
 * according Adapter defined (PreviewAdapter, CommentAdapter) in uri and "mode"
 * parameter given as parameter into the url. Default mode if given doesn't
 * exist or not set is view mode.
 * 
 * @author bjalon
 * 
 */
public class MobileDocument extends DocumentObject {

    private static final Log log = LogFactory.getLog(MobileDocument.class);

    private AutomationService automationService;

    public MobileDocument(WebContext ctx, DocumentRef docRef) {
        try {
            ResourceType resType = ctx.getModule().getType("Document");
            DocumentModel docModel = ctx.getCoreSession().getDocument(docRef);
            initialize(ctx, resType, docModel);
            ctx.push(this);
        } catch (Exception e) {
            throw WebException.wrap(e);
        }
    }

    /**
     * Needed to not break the navigation after a false PUT see NXP-8778 as I'm
     * redirected to this /@put URL
     * 
     */
    @GET
    @Path("@put")
    public Object redirectToRealURL() {
        String uri = ctx.getRequest().getRequestURI();
        // Remove the @put
        uri = uri.substring(0, uri.length() - "@put".length() - 1);
        String queryString = ctx.getRequest().getQueryString();
        return redirect(uri + (queryString != null ? "?" + queryString : ""));
    }

    @GET
    @Path("mailIt")
    public Object doMailIt() {
        try {
            OperationContext subctx = new OperationContext(ctx.getCoreSession(),
                    null);
            subctx.setInput(getDocument());
            getAutomationService().run(subctx, "sendEmailToMe");
        } catch (Exception e) {
            log.error(e, e);
            return Response.status(400).build();
        }
        return Response.ok().build(); 
    }

    @Override
    public Object doGet() {
        Map<String, Object> args = new HashMap<String, Object>();

        // must override the original doGet to override the resolution
        // so get parameter into the request instead injection
        HttpServletRequest request = ctx.getRequest();
        String mode = request.getParameter("mode");
        if (mode == null) {
            mode = "view";
        }

        // if url from JSF => ask to push mobile URL into browser history
        if (request.getParameter(TARGET_URL_PARAMETER) != null) {
            String mobileURL = String.format("%s/doc/%s?mode=%s",
                    ctx.getRoot().getPath(), doc.getId(), mode);
            args.put("mobileURL", mobileURL);
        }

        // Add the JSON DForm export
        JSonExportAdapter json = (JSonExportAdapter) ctx.newObject("JSONExport");
        args.put("doc", json.doGet(request.getRequestURI(), "post"));

        return getView(mode).args(args);
    }

    public boolean hasPreview() throws PropertyException, ClientException {
        if (doc.hasSchema("file")
                && doc.getPropertyValue("file:content") != null) {
            return PreviewHelper.typeSupportsPreview(doc);
        }
        return false;
    }

    public NuxeoPrincipal getPrincipal() {
        if (ctx.getPrincipal() instanceof NuxeoPrincipal) {
            return (NuxeoPrincipal) ctx.getPrincipal();
        }

        throw new WebException(
                "Principal found is not a NuxeoPrincipal can't generate it!");
    }

    // *************** TODO REMOVE WHEN PREVIEW WITH NAVIGATION RESOLVED
    // ************
    private String nuxeoContextPath;

    public String getPreviewURL() {
        Object targetObject = ctx.getTargetObject();
        if (!(targetObject instanceof MobileDocument)) {
            throw new WebException("Target Object must be MobileDocument");
        }
        return getNuxeoContextPath()
                + "/"
                + PreviewHelper.getPreviewURL(((MobileDocument) targetObject).getDocument());
    }

    private String getNuxeoContextPath() {
        if (nuxeoContextPath == null) {
            nuxeoContextPath = Framework.getProperty("org.nuxeo.ecm.contextPath");
        }
        return nuxeoContextPath;
    }

    private AutomationService getAutomationService() throws Exception {
        if (automationService == null) {
            automationService = Framework.getService(AutomationService.class);
        }
        return automationService;
    }

}

/*
 * (C) Copyright 2006-2013 Nuxeo SAS (http://nuxeo.com/) and contributors.
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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.mobile.webengine.RedirectHelper;
import org.nuxeo.ecm.mobile.webengine.adapter.JSonExportAdapter;
import org.nuxeo.ecm.platform.preview.helper.PreviewHelper;
import org.nuxeo.ecm.platform.url.api.DocumentViewCodecManager;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.ecm.platform.web.common.vh.VirtualHostHelper;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.model.ResourceType;
import org.nuxeo.ecm.webengine.model.WebContext;
import org.nuxeo.runtime.api.Framework;

import static org.nuxeo.ecm.mobile.filter.ApplicationRedirectionFilter.INITIAL_TARGET_URL_PARAM_NAME;

/**
 * This Class resolve a DocumentModel and expose differents restitutions according Adapter defined (PreviewAdapter,
 * CommentAdapter) in uri and "mode" parameter given as parameter into the url. Default mode if given doesn't exist or
 * not set is view mode.
 * 
 * @author <a href="mailto:bjalon@nuxeo.com">Benjamin JALON</a>
 * @since 5.5
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
     * Needed to not break the navigation after a false PUT see NXP-8778 as I'm redirected to this /@put URL
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
    public Object emailToCurrentPrincipal() {
        try {
            OperationContext subctx = new OperationContext(ctx.getCoreSession(), null);
            subctx.setInput(getDocument());
            getAutomationService().run(subctx, "sendEmailToMe");
        } catch (Exception e) {
            log.error(e, e);
            return Response.status(400).build();
        }
        return Response.ok().build();
    }

    @Path("like")
    public Object doLike() {
        try {
            return ctx.newObject("Like", getDocument());
        } catch (Exception e) {
            log.debug(e, e);
            return ctx.newObject("Empty");
        }
    }

    @Path("hasLiked")
    public Object doHasLiked() {
        try {
            return ctx.newObject("hasLiked", getDocument());
        } catch (Exception e) {
            log.debug(e, e);
            return ctx.newObject("Empty");
        }
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
        if (request.getParameter(INITIAL_TARGET_URL_PARAM_NAME) != null) {
            String mobileURL = String.format("%s/doc/%s?mode=%s", ctx.getRoot().getPath(), doc.getId(), mode);
            args.put("mobileURL", mobileURL);
        }
        args.put("hasBlob", getHasBlob());

        // Add the JSON DForm export
        JSonExportAdapter json = (JSonExportAdapter) ctx.newObject("JSONExport");
        args.put("doc", json.doGet(request.getRequestURI(), "post"));

        return getView(mode).args(args);
    }

    protected boolean getHasBlob() {
        DocumentModel doc = getDocument();
        BlobHolder bh = doc.getAdapter(BlobHolder.class);

        try {
            return bh != null && bh.getBlob() != null;
        } catch (ClientException e) {
            log.debug(e, e);
            return false;
        }
    }

    public boolean hasPreview() {
        if (doc.hasSchema("file") && doc.getPropertyValue("file:content") != null) {
            return PreviewHelper.typeSupportsPreview(doc);
        }
        return false;
    }

    public NuxeoPrincipal getPrincipal() {
        if (ctx.getPrincipal() instanceof NuxeoPrincipal) {
            return (NuxeoPrincipal) ctx.getPrincipal();
        }

        throw new WebException("Principal found is not a NuxeoPrincipal can't generate it!");
    }

    public String getJSFURLPath(DocumentModel docModel) throws Exception {
        return RedirectHelper.getJSFDocumentPath(docModel, VirtualHostHelper.getBaseURL(ctx.getRequest()));
    }

    public String getDownloadURL() throws Exception {
        DocumentModel docModel = getDocument();

        return getDownloadURL(docModel);
    }

    public String getDownloadURL(DocumentModel docModel) throws Exception {
        BlobHolder bh = doc.getAdapter(BlobHolder.class);
        String filename = bh.getBlob().getFilename();
        String mimetype = bh.getBlob().getMimeType();

        String downloadURL = getNuxeoContextPath() + "/";
        downloadURL += "nxbigfile" + "/";
        downloadURL += doc.getRepositoryName() + "/";
        downloadURL += doc.getRef().toString() + "/";
        downloadURL += "blobholder:0" + "/";
        downloadURL += filename;
        downloadURL += "?mimetype=" + mimetype;

        return downloadURL;
    }

    public String getJSFURLPath() throws Exception {
        return getJSFURLPath(getDocument());
    }

    // **** TODO REMOVE WHEN PREVIEW WITH NAVIGATION RESOLVED ****
    private String nuxeoContextPath;

    private DocumentViewCodecManager codecManager;

    public String getPreviewURL() {
        Object targetObject = ctx.getTargetObject();
        if (!(targetObject instanceof MobileDocument)) {
            throw new WebException("Target Object must be MobileDocument");
        }
        return getNuxeoContextPath() + "/" + PreviewHelper.getPreviewURL(((MobileDocument) targetObject).getDocument());
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

    public String getDisplayPrincipalName(String name) {
        try {
            NuxeoPrincipal principal = Framework.getLocalService(UserManager.class).getPrincipal(name);
            if (principal != null) {
                return getDisplayPrincipalName(principal);
            }
        } catch (ClientException e) {
            log.debug(e, e);
        }
        return name;
    }

    /**
     * Return the display name of an expected principal. It passes as an Object to prevent Freemarker to thrown an
     * exception when creator is null.
     * 
     * @param object must be of type org.nuxeo.ecm.core.api.NuxeoPrincipal
     * @return the display name will be empty if not a NuxeoPrincipal
     */
    public String getDisplayPrincipalName(Object object) {
        if (object instanceof NuxeoPrincipal) {
            NuxeoPrincipal principal = (NuxeoPrincipal) object;
            String display = (principal.getFirstName() + " " + principal.getLastName()).trim();
            return StringUtils.isBlank(display) ? principal.getName() : display;
        }
        return "";
    }

    public String getDisplayPrincipalName() {
        return getDisplayPrincipalName(getPrincipal());
    }

}

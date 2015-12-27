/*
 * (C) Copyright 2006-2012 Nuxeo SA (http://nuxeo.com/) and others.
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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.mobile.webengine.document.MobileDocument;
import org.nuxeo.ecm.platform.userworkspace.api.UserWorkspaceService;
import org.nuxeo.ecm.platform.web.common.vh.VirtualHostHelper;
import org.nuxeo.ecm.webengine.model.TypeNotFoundException;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.ModuleRoot;
import org.nuxeo.runtime.api.Framework;

import static org.nuxeo.ecm.mobile.filter.ApplicationRedirectionFilter.INITIAL_TARGET_URL_PARAM_NAME;
import static org.nuxeo.ecm.mobile.webengine.adapter.DefaultMobileAdapter.ONLY_VISIBLE_CHILDREN;

/**
 * Entry point of the webengine application
 *
 * @author <a href="mailto:bjalon@nuxeo.com">Benjamin JALON</a>
 * @since 5.5
 */
@Path("mobile")
@Produces("text/html;charset=UTF-8")
@WebObject(type = "MobileApplication")
public class MobileApplication extends ModuleRoot {

    private static final Log log = LogFactory.getLog(MobileApplication.class);

    private UserWorkspaceService userWorkspaceService;

    protected static final Pattern CORDOVA_USER_AGENT_REGEXP = Pattern.compile("Cordova/(.+?) \\((.*)\\)");

    protected enum ToolbarPage {
        HOME, BROWSE, PROFILE, SEARCH
    }

    @Override
    protected void initialize(Object... args) {
        // Check if the Client is using Cordova
        // XXX Optimize it to prevent from regex all request
        Map<String, Serializable> context = null;

        String userAgent = getContext().getRequest().getHeader("User-Agent");
        if (StringUtils.isEmpty(userAgent)) {
            log.debug("User-Agent empty: assuming not on a mobile device.");
            return;
        }

        Matcher matcher = CORDOVA_USER_AGENT_REGEXP.matcher(userAgent);
        if (matcher.find()) {
            context = new HashMap<String, Serializable>();

            context.put("version", matcher.group(1));
            context.put("device", matcher.group(2));

            log.info("Cordova User-Agent detected");
        }
        getContext().setProperty("Cordova", context);
    }

    /**
     * Try to fetch document in targetURL parameter in URL if not this is the Home binding
     */
    @GET
    public Object doGet(@QueryParam(INITIAL_TARGET_URL_PARAM_NAME) String targetURL) {

        DocumentRef targetRef = RedirectHelper.findDocumentRef(targetURL);
        if (targetRef != null) {
            setCurrentPage(ToolbarPage.BROWSE);

            MobileDocument targetDoc = new MobileDocument(ctx, targetRef);
            return targetDoc.doGet();
        }

        // If SC mobile fragment is enable, redirect to the new homepage
        if (getSocialObject() != null) {
            return redirect(ctx.getServerURL() + ctx.getModulePath() + "/social");
        }

        Map<String, Object> args = new HashMap<String, Object>();
        args.put("userWorkspace", getUserWorkspacesDocs());

        setCurrentPage(ToolbarPage.HOME);
        return getView("index").args(args);
    }

    @Path("auth")
    public Object doTraverseAuthentication() {
        return ctx.newObject("WebMobileAuthentication");
    }

    @Path("profile")
    public Object doTraverseProfile() {
        setCurrentPage(ToolbarPage.PROFILE);
        return ctx.newObject("Profile");
    }

    /**
     * Generate the root view of the repository. First root descendant and user workspace are rendered
     */
    @GET
    @Path("root")
    public Object getRootRepositoryView() {
        Map<String, Object> args = new HashMap<String, Object>();

        CoreSession session = ctx.getCoreSession();

        DocumentModel doc = session.getRootDocument();
        DocumentModelList children;
        do {
            children = session.getChildren(doc.getRef(), null, ONLY_VISIBLE_CHILDREN, null);
            if (children.size() == 1) {
                doc = children.get(0);
            }
        } while (children.size() == 1);
        args.put("domain", children);

        setCurrentPage(ToolbarPage.BROWSE);
        return getView("root").args(args);
    }

    @Path("docPath/@{adapter}")
    public Object doTraverseRootDocumentByPath(@PathParam("adapter") String adapter) {
        DocumentRef ref = new PathRef("/");
        setCurrentPage(ToolbarPage.BROWSE);
        if ("search".equals(adapter)) {
            return new MobileDocument(ctx, ref).search();
        }
        return new MobileDocument(ctx, ref).disptachAdapter(adapter);
    }

    @Path("docPath{docPathValue:(/(?:(?!/@).)*)}")
    public Object doTraverseDocumentByPath(@PathParam("docPathValue") String docPath) {
        DocumentRef ref = new PathRef(docPath);
        setCurrentPage(ToolbarPage.BROWSE);
        return new MobileDocument(ctx, ref);
    }

    @Path("doc/{docId}")
    public Object doTraverseDocument(@PathParam("docId") String docId) {
        DocumentRef ref = new IdRef(docId);
        setCurrentPage(ToolbarPage.BROWSE);
        return new MobileDocument(ctx, ref);
    }

    @Path("search")
    public Object doTraverseSearch() {
        setCurrentPage(ToolbarPage.SEARCH);
        return ctx.newObject("Search");
    }

    @Path("task")
    @Deprecated
    // Since 5.6 with content routing.
    public Object doTraverseTask() {
        return null;
    }

    @Path("activity")
    public Object doTraverseActivity() {
        return ctx.newObject("Activity");
    }

    @Path("social")
    public Object doSocial() {
        setCurrentPage(ToolbarPage.HOME);
        return ctx.newObject("Social");
    }

    protected void setCurrentPage(ToolbarPage page) {
        getContext().setProperty("currentPage", page.name());
    }

    protected DocumentModelList getUserWorkspacesDocs() {
        CoreSession session = ctx.getCoreSession();
        DocumentModel userWorkspace = getUserWorkspaceService().getCurrentUserPersonalWorkspace(session, null);
        return session.getChildren(userWorkspace.getRef(), null, ONLY_VISIBLE_CHILDREN, null);
    }

    protected Object getSocialObject() {
        try {
            return ctx.newObject("Social");
        } catch (TypeNotFoundException e) {
            log.debug(e, e);
            return null;
        }
    }

    protected UserWorkspaceService getUserWorkspaceService() {
        if (userWorkspaceService == null) {
            userWorkspaceService = Framework.getLocalService(UserWorkspaceService.class);
        }
        return userWorkspaceService;
    }

    public String getNuxeoContextPath() {
        return VirtualHostHelper.getBaseURL(request);
    }

    public String getDocumentMobileUrl(DocumentModel doc) {
        String baseUrl = String.format("%s/doc/%s", getPath(), doc.getId());
        if (doc.isFolder()) {
            baseUrl = String.format("%s/@folderish", baseUrl);
        }
        return baseUrl;
    }
}

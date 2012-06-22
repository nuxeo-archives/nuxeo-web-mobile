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

import static org.nuxeo.ecm.mobile.filter.ApplicationRedirectionFilter.INITIAL_TARGET_URL_PARAM_NAME;
import static org.nuxeo.ecm.mobile.webengine.document.FolderishAdapter.ONLY_VISIBLE_CHILDREN;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.activity.ActivitiesList;
import org.nuxeo.ecm.activity.Activity;
import org.nuxeo.ecm.activity.ActivityHelper;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.impl.DocumentModelListImpl;
import org.nuxeo.ecm.mobile.webengine.document.MobileDocument;
import org.nuxeo.ecm.platform.url.api.DocumentView;
import org.nuxeo.ecm.platform.url.api.DocumentViewCodecManager;
import org.nuxeo.ecm.platform.userworkspace.api.UserWorkspaceService;
import org.nuxeo.ecm.rating.api.Constants;
import org.nuxeo.ecm.rating.api.RatingService;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.ModuleRoot;
import org.nuxeo.runtime.api.Framework;

/**
 * Entry point of the webengine application
 * 
 * @author <a href="mailto:bjalon@nuxeo.com">Benjamin JALON</a>
 * @since 5.5
 * 
 */
@Path("mobile")
@Produces("text/html;charset=UTF-8")
@WebObject(type = "MobileApplication")
public class MobileApplication extends ModuleRoot {

    private static final Log log = LogFactory.getLog(MobileApplication.class);

    private String nuxeoContextPath;

    private DocumentViewCodecManager codecManager;

    private UserWorkspaceService userWorkspaceService;

    protected static final Pattern CORDOVA_USER_AGENT_REGEXP = Pattern.compile("Cordova/(.+?) \\((.*)\\)");

    @Override
    protected void initialize(Object... args) {
        // Check if the Client is using Cordova
        // XXX Optimize it to prevent from regex all request
        Map<String, Serializable> context = null;

        String userAgent = getContext().getRequest().getHeader("User-Agent");
        Matcher matcher = CORDOVA_USER_AGENT_REGEXP.matcher(userAgent);
        if (matcher.find()) {
            context = new HashMap<String, Serializable>();

            context.put("version", matcher.group(1));
            context.put("device", matcher.group(2));
            context.put("isIOS", matcher.group(2).matches("iOS"));
            context.put("isAndroid", matcher.group(2).matches("android"));

            log.info("Cordova User-Agent detected");
        }
        getContext().setProperty("Cordova", context);
    }

    /**
     * Try to fetch document in targetURL parameter in URL if not this is the
     * Home binding
     * 
     */
    @GET
    public Object doGet(@QueryParam(INITIAL_TARGET_URL_PARAM_NAME)
    String initialURL) throws Exception {
        if (initialURL != null) {
            DocumentView docView = getCodecManager().getDocumentViewFromUrl(
                    initialURL, true, "");
            if (docView != null) {
                log.debug("Request from home: Target URL given into url parameter detected as a "
                        + "document request from url codec service: "
                        + initialURL);
                MobileDocument docResolved = new MobileDocument(ctx,
                        docView.getDocumentLocation().getDocRef());
                return docResolved.doGet();
            }
        }
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("latestLiked", getLatestRatedDocs());
        args.put("userWorkspace", getUserWorkspacesDocs());

        return getView("index").args(args);
    }

    @Path("auth")
    public Object doTraverseAuthentication() {
        return ctx.newObject("WebMobileAuthentication");
    }

    @Path("profile")
    public Object doTraverseProfile() {
        return ctx.newObject("Profile");
    }

    /**
     * Generate the root view of the repository. First root descendant and user
     * workspace are rendered
     */
    @GET
    @Path("root")
    public Object getRootRepositoryView() throws Exception {
        Map<String, Object> args = new HashMap<String, Object>();

        CoreSession session = ctx.getCoreSession();

        DocumentModel doc = session.getRootDocument();
        DocumentModelList children;
        do {
            children = session.getChildren(doc.getRef(), null,
                    ONLY_VISIBLE_CHILDREN, null);
            if (children.size() == 1) {
                doc = children.get(0);
            }
        } while (children.size() == 1);
        args.put("domain", children);

        return getView("root").args(args);
    }

    @Path("docPath/@{adapter}")
    public Object doTraverseRootDocumentByPath(@PathParam("adapter")
    String adapter) {
        DocumentRef ref = new PathRef("/");
        if ("search".equals(adapter)) {
            return new MobileDocument(ctx, ref).search();
        }
        return new MobileDocument(ctx, ref).disptachAdapter(adapter);
    }

    @Path("docPath{docPathValue:(/(?:(?!/@).)*)}")
    public Object doTraverseDocumentByPath(@PathParam("docPathValue")
    String docPath) {
        DocumentRef ref = new PathRef(docPath);
        return new MobileDocument(ctx, ref);
    }

    @Path("doc/{docId}")
    public Object doTraverseDocument(@PathParam("docId")
    String docId) {
        DocumentRef ref = new IdRef(docId);
        return new MobileDocument(ctx, ref);
    }

    @Path("search")
    public Object doTraverseSearch() {
        return ctx.newObject("Search");
    }

    @Path("task")
    public Object doTraverseTask() {
        return ctx.newObject("Workflow");
    }

    @Path("activity")
    public Object doTraverseActivity() {
        return ctx.newObject("Activity");
    }

    public String getNuxeoContextPath() {
        if (nuxeoContextPath == null) {
            nuxeoContextPath = Framework.getProperty("org.nuxeo.ecm.contextPath");
        }
        return nuxeoContextPath;
    }

    protected DocumentModelList getUserWorkspacesDocs() throws ClientException {
        CoreSession session = ctx.getCoreSession();
        DocumentModel userWorkspace = getUserWorkspaceService().getCurrentUserPersonalWorkspace(
                session, null);
        return session.getChildren(userWorkspace.getRef(), null,
                ONLY_VISIBLE_CHILDREN, null);
    }

    protected DocumentModelList getLatestRatedDocs() {
        ActivitiesList latestAct = getRatingService().getLastestRatedDocByUser(
                ctx.getPrincipal().getName(), Constants.LIKE_ASPECT, 10);
        latestAct.filterActivities(ctx.getCoreSession());

        DocumentModelList ret = new DocumentModelListImpl();
        for (Activity activity : latestAct) {
            try {
                DocumentRef ref = new IdRef(
                        ActivityHelper.getDocumentId(activity.getTarget()));
                ret.add(ctx.getCoreSession().getDocument(ref));
            } catch (ClientException e) {
                log.info(e.getMessage());
                log.debug(e, e);
            }
        }

        return ret;
    }

    protected RatingService getRatingService() {
        return Framework.getLocalService(RatingService.class);
    }

    protected DocumentViewCodecManager getCodecManager() throws Exception {
        if (codecManager == null) {
            codecManager = Framework.getService(DocumentViewCodecManager.class);
        }
        return codecManager;
    }

    protected UserWorkspaceService getUserWorkspaceService() {
        if (userWorkspaceService == null) {
            userWorkspaceService = Framework.getLocalService(UserWorkspaceService.class);
        }
        return userWorkspaceService;
    }

}

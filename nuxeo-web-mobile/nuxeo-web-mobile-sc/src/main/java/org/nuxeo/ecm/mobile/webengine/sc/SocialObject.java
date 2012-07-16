package org.nuxeo.ecm.mobile.webengine.sc;

import static org.nuxeo.ecm.mobile.webengine.adapter.DefaultMobileAdapter.ONLY_VISIBLE_CHILDREN;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;

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
import org.nuxeo.ecm.core.api.impl.DocumentModelListImpl;
import org.nuxeo.ecm.platform.userworkspace.api.UserWorkspaceService;
import org.nuxeo.ecm.rating.api.Constants;
import org.nuxeo.ecm.rating.api.RatingService;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.DefaultObject;
import org.nuxeo.runtime.api.Framework;

@WebObject(type = "Social")
public class SocialObject extends DefaultObject {
    private static final Log log = LogFactory.getLog(SocialObject.class);

    protected UserWorkspaceService userWorkspaceService;

    @Override
    protected void initialize(Object... args) {
        super.initialize(args);
    }

    @GET
    public Object doGet() throws ClientException {

        Map<String, Object> args = new HashMap<String, Object>();
        args.put("latestLiked", getLatestRatedDocs());
        args.put("userWorkspace", getUserWorkspacesDocs());

        return getView("index").args(args);
    }

    protected DocumentModelList getUserWorkspacesDocs() throws ClientException {
        CoreSession session = ctx.getCoreSession();
        DocumentModel userWorkspace = getUserWorkspaceService().getCurrentUserPersonalWorkspace(
                session, null);
        return session.getChildren(userWorkspace.getRef(), null,
                ONLY_VISIBLE_CHILDREN, null);
    }

    protected UserWorkspaceService getUserWorkspaceService() {
        if (userWorkspaceService == null) {
            userWorkspaceService = Framework.getLocalService(UserWorkspaceService.class);
        }
        return userWorkspaceService;
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
}

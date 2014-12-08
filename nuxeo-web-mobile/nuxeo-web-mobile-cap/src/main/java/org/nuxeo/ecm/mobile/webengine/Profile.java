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
package org.nuxeo.ecm.mobile.webengine;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.ecm.user.center.profile.UserProfileService;
import org.nuxeo.ecm.webengine.model.Template;
import org.nuxeo.ecm.webengine.model.TemplateNotFoundException;
import org.nuxeo.ecm.webengine.model.View;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.DefaultObject;
import org.nuxeo.runtime.api.Framework;

/**
 * Manage Profile view
 * 
 * @author <a href="mailto:bjalon@nuxeo.com">Benjamin JALON</a>
 * @since 5.5
 */
@WebObject(type = "Profile")
@Produces("text/html;charset=UTF-8")
public class Profile extends DefaultObject {

    private static final Log log = LogFactory.getLog(Profile.class);

    private boolean hasBeenFetched;

    /************** Bindings *******************/

    @GET
    @Path("search")
    public Object doGetUsers(@QueryParam("q") String query) throws ClientException, Exception {
        DocumentModelList users = getUserManager().searchUsers(query);

        return getView("users").arg("users", users);
    }

    @GET
    @Path("{username}")
    public Template doGetUser(@PathParam("username") String username) throws ClientException, Exception {
        DocumentModel userProfile = getUserProfile(username);
        DocumentModel userMainInfo = getUserManager().getUserModel(username);

        return getView("view").arg("userProfile", userProfile).arg("userMainInfo", userMainInfo);
    }

    /************** Actions *******************/

    public boolean isRichProfileDeployed() {
        try {
            return getUserProfileService() != null;
        } catch (Exception e) {
            return false;
        }
    }

    public String getAvatarURI(String username) {
        // TODO improve to not fetch several times the document and avatar
        String contextPath = Framework.getProperty("org.nuxeo.ecm.contextPath");

        DocumentModel userProfile = getUserProfile(username);
        Blob avatar;
        try {
            avatar = (Blob) userProfile.getPropertyValue("userprofile:avatar");
        } catch (ClientException e) {
            log.debug("No avatar found");
            avatar = null;
        }

        if (userProfile != null && avatar != null) {
            // TODO : Use a relative path (be careful of proxy stuff)
            String uriPattern = "%s/nxfile/%s/%s/userprofile:avatar/";
            String repositoryName = ctx.getCoreSession().getRepositoryName();
            return String.format(uriPattern, contextPath, repositoryName, getUserProfile(username).getId());
        } else {
            // TODO : Use a relative path (be careful of proxy stuff)
            return contextPath + "/site/skin/nuxeo/icons/default_avatar.png";
        }
    }

    /************** OTHERS **********************/

    private DocumentModel getUserProfile(String username) {
        try {
            return getUserProfileService().getUserProfileDocument(username, ctx.getCoreSession());
        } catch (Exception e) {
            log.debug("Can't get Rich Profile");
            return null;
        }
    }

    private String getViewSuffix() {
        if (isRichProfileDeployed()) {
            return "rich";
        } else {
            return "standard";
        }
    }

    /**
     * Try to return the dedicated view for standard or rich profile (with suffix) if not exists return the view without
     * suffix.
     */
    @Override
    public Template getView(final String viewId) {

        String dedicatedViewId = String.format("%s-%s", viewId, getViewSuffix());

        try {
            return new View(this, dedicatedViewId).resolve();
        } catch (TemplateNotFoundException e) {
            log.debug("Dedicated view not exists, try use standard binding");
        }

        return new View(this, viewId).resolve();
    }

    /**** fetch service *****/

    private UserManager userManager;

    private UserProfileService userProfileService;

    private UserProfileService getUserProfileService() throws Exception {
        if (!hasBeenFetched) {
            hasBeenFetched = false;
            try {
                Class.forName("org.nuxeo.ecm.user.center.profile.UserProfileService");
            } catch (ClassNotFoundException e) {
                log.info("UserProfileService not deployed, use the UserManager");
                return null;
            }

            userProfileService = Framework.getService(UserProfileService.class);
        }
        return userProfileService;
    }

    private UserManager getUserManager() throws Exception {
        if (userManager == null) {
            userManager = Framework.getService(UserManager.class);
        }
        return userManager;
    }

}

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
package org.nuxeo.ecm.mobile.webengine;

import static org.nuxeo.ecm.user.center.profile.UserProfileConstants.USER_PROFILE_AVATAR_FIELD;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.PropertyException;
import org.nuxeo.ecm.core.io.download.DownloadService;
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
    public Object doGetUsers(@QueryParam("q") String query) {
        DocumentModelList users = getUserManager().searchUsers(query);

        return getView("users").arg("users", users);
    }

    @GET
    @Path("{username}")
    public Template doGetUser(@PathParam("username") String username) {
        DocumentModel userProfile = getUserProfile(username);
        DocumentModel userMainInfo = getUserManager().getUserModel(username);

        return getView("view").arg("userProfile", userProfile).arg("userMainInfo", userMainInfo);
    }

    /************** Actions *******************/

    public boolean isRichProfileDeployed() {
        return getUserProfileService() != null;
    }

    public String getAvatarURI(String username) {
        // TODO improve to not fetch several times the document and avatar
        String contextPath = Framework.getProperty("org.nuxeo.ecm.contextPath");

        DocumentModel userProfile = getUserProfile(username);
        Blob avatar;
        try {
            avatar = (Blob) userProfile.getPropertyValue(USER_PROFILE_AVATAR_FIELD);
        } catch (PropertyException e) {
            log.debug("No avatar found");
            avatar = null;
        }

        if (userProfile != null && avatar != null) {
            DownloadService downloadService = Framework.getService(DownloadService.class);
            return contextPath + "/" + downloadService.getDownloadUrl(userProfile, USER_PROFILE_AVATAR_FIELD, "");
        } else {
            // TODO : Use a relative path (be careful of proxy stuff)
            return contextPath + "/site/skin/nuxeo/icons/default_avatar.png";
        }
    }

    /************** OTHERS **********************/

    private DocumentModel getUserProfile(String username) {
        return getUserProfileService().getUserProfileDocument(username, ctx.getCoreSession());
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

    private UserProfileService userProfileService;

    private UserProfileService getUserProfileService() {
        if (!hasBeenFetched) {
            hasBeenFetched = false;
            try {
                Class.forName("org.nuxeo.ecm.user.center.profile.UserProfileService");
            } catch (ReflectiveOperationException e) {
                log.info("UserProfileService not deployed, use the UserManager");
                return null;
            }

            userProfileService = Framework.getService(UserProfileService.class);
        }
        return userProfileService;
    }

    private UserManager getUserManager() {
        return Framework.getService(UserManager.class);
    }

}

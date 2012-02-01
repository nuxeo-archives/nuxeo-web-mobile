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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.ecm.user.center.profile.UserProfileService;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.DefaultObject;
import org.nuxeo.runtime.api.Framework;

/**
 * Manage Profile view
 * 
 * @author bjalon
 * 
 */
@WebObject(type = "Profile")
@Produces("text/html;charset=UTF-8")
public class Profile extends DefaultObject {

    private static final Log log = LogFactory.getLog(Profile.class);

    private UserManager userManager;

    private boolean hasNotBeenFetched;

    private UserProfileService userProfileService;

    @GET
    @Path("{username}")
    public Object doGet(@PathParam("username") String username)
            throws ClientException, Exception {
        DocumentModel userProfile = getUserProfile(username);
        return getView("index").arg("userProfile", userProfile);
    }

    @GET
    public Object doGet() throws ClientException, Exception {
        DocumentModel userProfile = getUserProfile(ctx.getPrincipal().getName());
        return getView("index").arg("userProfile", userProfile);
    }

    private DocumentModel getUserProfile(String username)
            throws ClientException, Exception {
        if (getUserProfileService() != null) {
            return getUserProfileService().getUserProfileDocument(username,
                    ctx.getCoreSession());
        }

        return getUserManager().getUserModel(username);
    }

    private UserManager getUserManager() throws Exception {
        if (userManager == null) {
            userManager = Framework.getService(UserManager.class);
        }
        return userManager;
    }

    private UserProfileService getUserProfileService() throws Exception {
        if (hasNotBeenFetched) {
            hasNotBeenFetched = false;
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
}

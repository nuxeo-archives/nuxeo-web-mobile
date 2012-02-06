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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.nuxeo.ecm.activity.ActivitiesList;
import org.nuxeo.ecm.activity.ActivityStreamService;
import org.nuxeo.ecm.social.activity.stream.UserActivityStreamFilter;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.DefaultAdapter;
import org.nuxeo.runtime.api.Framework;

import static org.nuxeo.ecm.social.activity.stream.UserActivityStreamFilter.ACTOR_PARAMETER;
import static org.nuxeo.ecm.social.activity.stream.UserActivityStreamFilter.QUERY_TYPE_PARAMETER;
import static org.nuxeo.ecm.social.activity.stream.UserActivityStreamFilter.QueryType.ACTIVITY_STREAM_FOR_ACTOR;

/**
 * @author bjalon
 * 
 */
@WebObject(type = "Activity")
@Produces("text/html;charset=UTF-8")
public class Activity extends DefaultAdapter {

    private ActivityStreamService service;

    @GET
    public Object doGetAllActivities() throws Exception {
        return getView("index").arg("activities", getAllActivities());
    }

    @GET
    @Path("user/{username}")
    public Object doGetUserActivities(@PathParam("username") String username)
            throws Exception {
        return getView("User").arg("username", username).arg("activities",
                getUserActivities(username));
    }

    public ActivitiesList getUserActivities(String username) throws Exception {
        Map<String, Serializable> parameters = new HashMap<String, Serializable>();
        parameters.put(QUERY_TYPE_PARAMETER, ACTIVITY_STREAM_FOR_ACTOR);
        parameters.put(ACTOR_PARAMETER, username);
        return getActivityStreamService().query(UserActivityStreamFilter.ID,
                parameters);
    }

    public ActivitiesList getAllActivities() throws Exception {
        return getActivityStreamService().query(
                ActivityStreamService.ALL_ACTIVITIES, null);
    }

    private ActivityStreamService getActivityStreamService() throws Exception {
        if (service == null) {
            service = Framework.getService(ActivityStreamService.class);
        }
        return service;
    }

}

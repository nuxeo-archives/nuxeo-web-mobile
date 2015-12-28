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
package org.nuxeo.ecm.mobile;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * This service will store all the information needed to redirect the user to the right url according his navigation
 * choice or the request context. This is needed for instance when we want to redirect the user to a dedicated
 * application according the user-agent browser or logic implemented into your RequestHandler.
 * 
 * @author <a href="mailto:bjalon@nuxeo.com">Benjamin JALON</a>
 * @since 5.5
 */
public interface ApplicationDefinitionService {

    /**
     * Return the root path of the web application where the request must be redirected according request context. If
     * there is no application that match this request context, null is returned. Absolute URL is retuned with the
     * protocole, the servername, ...
     */
    public String getApplicationBaseURL(HttpServletRequest request);

    public String getApplicationBaseURI(HttpServletRequest request);

    /**
     * Return the login url according request context. This is the absolute URL with the protocole, the servername, ...
     */
    public String getLoginURL(HttpServletRequest request);

    /**
     * Return the logout url according request context.This is the absolute URL with the protocole, the servername, ...
     */
    public String getLogoutURL(HttpServletRequest request);

    /**
     * Return the list of relative unprotected URI for all application
     */
    public List<String> getUnAuthenticatedURLPrefix();

    /**
     * Return the list of relative unprotected URI for this request
     */
    public List<String> getUnAuthenticatedURLPrefix(HttpServletRequest request);

    /**
     * Return true if the given request is a resource for the application this request match
     */
    public boolean isResourceURL(HttpServletRequest request);

}

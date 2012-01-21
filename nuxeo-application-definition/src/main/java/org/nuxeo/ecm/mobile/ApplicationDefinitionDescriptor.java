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
package org.nuxeo.ecm.mobile;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;
import org.nuxeo.ecm.mobile.handler.RequestHandler;

/**
 * Descriptor that represent a definition of an application and a handler that
 * will detect request context that will make the user redirect to this
 * application.
 *
 * @author bjalon
 *
 */
@XObject("application")
public class ApplicationDefinitionDescriptor {

    @XNode("@name")
    public String name;

    @XNode("@order")
    public Integer order;

    @XNode("@disabled")
    public boolean disabled;

    @XNode("handler")
    public Class<?> klass;

    @XNode("baseURL")
    public String baseURL;

    @XNode("loginPage")
    public String loginPage;

    @XNode("logoutPage")
    public String logoutPage;

    @XNode("resourcesBaseURL")
    private String resourcesBaseUrl;

    /**
     * Application name described
     */
    public String getName() {
        return name;
    }

    /**
     * Return true if the given Application is enabled.
     */
    public boolean isDisable() {
        return disabled;
    }

    /**
     * Application definition service provide a way to expose easily a new
     * dedicated UI for a specific type of environment (for instance for user
     * agent browser selection). Implementation given here implements the logic
     * to tell if request must be redirected to this application.
     */
    public RequestHandler getRequestHandlerInstance() {
        Object obj;
        try {
            obj = klass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(
                    "Problem during the Given class instanciation please check your contribution");
        }
        if (obj instanceof RequestHandler) {
            obj = (RequestHandler) obj;
            return (RequestHandler) obj;
        }

        throw new RuntimeException("Given class is not a "
                + RequestHandler.class + " implementation");
    }

    /**
     * Order is used to sort {@code RequestHandler} executed to find the target
     * application given a request.
     */
    public Integer getOrder() {
        return order;
    }

    /**
     * Base URL of the described application (without the Nuxeo Context Path)
     */
    public String getBaseUrl() {
        return baseURL;
    }

    /**
     * relative path of the login page
     */
    public String getLoginPage() {
        return loginPage;
    }

    /**
     * relative path of the logout page
     */
    public String getLogoutPage() {
        return logoutPage;
    }

    /**
     * Resource Base URL of the resources needed by the application described
     * (without the Nuxeo Context Path)
     */
    public String getResourcesBaseUrl() {
        return resourcesBaseUrl;
    }

}

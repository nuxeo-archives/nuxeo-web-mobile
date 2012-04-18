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

import java.util.ArrayList;
import java.util.List;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XNodeList;
import org.nuxeo.common.xmap.annotation.XObject;

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

    @XNode("requestHandlerName")
    public String requestHandlerName;

    @XNode("applicationRelativePath")
    public String applicationRelativePath;

    @XNode("loginPage")
    public String loginPage;

    @XNode("logoutPage")
    public String logoutPage;

    @XNodeList(value = "resources/resourcesBaseURL", type = ArrayList.class, componentType = String.class)
    public List<String> resourcesBaseUrl = new ArrayList<String>();

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
     * Return the name of the handler that implements the logic of redirection
     * to the application described into this descriptor. Definition of the
     * handler is defined into the handler extension point.
     */
    public String getRequestHandlerName() {
        return requestHandlerName;
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
    public String getApplicationRelativePath() {
        return applicationRelativePath;
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

    private boolean isResourcesBaseUrlChecked = false;

    /**
     * Resource Base URL of the resources needed by the application described
     * (without the Nuxeo Context Path) add a slash at the end of the base url.
     */
    public List<String> getResourcesBaseUrl() {
        if (!isResourcesBaseUrlChecked) {
            List<String> result = new ArrayList<String>();
            isResourcesBaseUrlChecked = true;
            for (String url : resourcesBaseUrl) {
                if (!url.endsWith("/")) {
                    result.add(url + "/");
                }
            }
            resourcesBaseUrl = result;
        }
        return resourcesBaseUrl;
    }

}

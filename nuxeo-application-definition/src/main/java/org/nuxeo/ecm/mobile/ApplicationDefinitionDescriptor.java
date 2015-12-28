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

import java.util.ArrayList;
import java.util.List;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XNodeList;
import org.nuxeo.common.xmap.annotation.XObject;

/**
 * Descriptor that represent a definition of an application and a handler that will detect request context that will
 * make the user redirect to this application.
 * 
 * @author <a href="mailto:bjalon@nuxeo.com">Benjamin JALON</a>
 * @since 5.5
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
     * Return the name of the handler that implements the logic of redirection to the application described into this
     * descriptor. Definition of the handler is defined into the handler extension point.
     */
    public String getRequestHandlerName() {
        return requestHandlerName;
    }

    /**
     * Order is used to sort {@code RequestHandler} executed to find the target application given a request.
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
     * Resource Base URL of the resources needed by the application described (without the Nuxeo Context Path) add a
     * slash at the end of the base url.
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

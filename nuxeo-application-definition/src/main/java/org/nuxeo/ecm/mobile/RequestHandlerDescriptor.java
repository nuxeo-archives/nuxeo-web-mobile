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
 *     Benjamin JALON
 */
package org.nuxeo.ecm.mobile;

import java.util.HashMap;
import java.util.Map;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XNodeMap;
import org.nuxeo.common.xmap.annotation.XObject;
import org.nuxeo.ecm.mobile.handler.RequestHandler;

/**
 * @author <a href="mailto:bjalon@nuxeo.com">Benjamin JALON</a>
 * @since 5.5
 */
@XObject("requestHandler")
public class RequestHandlerDescriptor {

    @XNode("implementation")
    public Class<?> klass;

    @XNode("@name")
    public String requestHandlerName;

    @XNode("@disabled")
    public boolean disabled;

    @XNodeMap(value = "properties/property", key = "@name", type = HashMap.class, componentType = String.class)
    protected Map<String, String> properties = new HashMap<String, String>();

    /**
     * Application definition service provide a way to expose easily a new dedicated UI for a specific type of
     * environment (for instance for user agent browser selection). Implementation given here the logic to tell if
     * request must be redirected to this application or not.
     */
    public RequestHandler getRequestHandlerInstance() {
        Object obj;
        try {
            obj = klass.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Problem during the Given class instanciation please check your contribution", e);
        }
        if (obj instanceof RequestHandler) {
            RequestHandler rh = (RequestHandler) obj;
            rh.init(properties);
            return rh;
        }

        throw new RuntimeException("Given class is not a " + RequestHandler.class + " implementation");
    }

    public String getRequestHandlerName() {
        return requestHandlerName;
    }
}

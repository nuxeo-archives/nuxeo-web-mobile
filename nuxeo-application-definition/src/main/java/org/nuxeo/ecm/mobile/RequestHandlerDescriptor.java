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
 *
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
     * Application definition service provide a way to expose easily a new
     * dedicated UI for a specific type of environment (for instance for user
     * agent browser selection). Implementation given here the logic
     * to tell if request must be redirected to this application or not.
     */
    public RequestHandler getRequestHandlerInstance() {
        Object obj;
        try {
            obj = klass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(
                    "Problem during the Given class instanciation please check your contribution", e);
        }
        if (obj instanceof RequestHandler) {
            RequestHandler rh = (RequestHandler) obj;
            rh.init(properties);
            return rh;
        }

        throw new RuntimeException("Given class is not a "
                + RequestHandler.class + " implementation");
    }
    
    public String getRequestHandlerName() {
        return requestHandlerName;
    }
}

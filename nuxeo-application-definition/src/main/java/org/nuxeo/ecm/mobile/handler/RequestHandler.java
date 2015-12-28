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
package org.nuxeo.ecm.mobile.handler;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * A mobile application redirection service provide a way to expose easily a new dedicated UI for a specific type of
 * environment (essentially user agent browser). Pointer of the implementation of this interface is given into the
 * Application descriptor. Here is implemented the logic to tell if the request is candidate for the application
 * described.
 * 
 * @author <a href="mailto:bjalon@nuxeo.com">Benjamin JALON</a>
 * @since 5.5
 */
public interface RequestHandler {

    /**
     * Used for initialize request handler with properties given in descriptor definition.
     */
    public RequestHandler init(Map<String, String> properties);

    /**
     * return true if the request is a candidate for the Application described into the {@code ApplicationDescriptor}.
     */
    public boolean isRequestRedirectedToApplication(HttpServletRequest request);

    /**
     * return true if the request is a candidate for the specific login form described by application
     */
    public boolean isRequestRedirectedToApplicationLoginForm(HttpServletRequest request);

}

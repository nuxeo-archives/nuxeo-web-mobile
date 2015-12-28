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
package org.nuxeo.application.definition;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.nuxeo.ecm.mobile.handler.RequestHandler;

/**
 * @author bjalon
 */
public class MyApplicationRequestHandler implements RequestHandler {

    @Override
    public boolean isRequestRedirectedToApplication(HttpServletRequest request) {
        return true;
    }

    @Override
    public RequestHandler init(Map<String, String> arg0) {
        return this;
    }

    @Override
    public boolean isRequestRedirectedToApplicationLoginForm(HttpServletRequest arg0) {
        return true;
    }

}

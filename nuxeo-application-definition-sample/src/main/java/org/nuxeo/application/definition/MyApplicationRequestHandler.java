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
package org.nuxeo.application.definition;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.nuxeo.ecm.mobile.handler.RequestHandler;



/**
 * @author bjalon
 *
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
    public boolean isRequestRedirectedToApplicationLoginForm(
            HttpServletRequest arg0) {
        return true;
    }

}

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
package org.nuxeo.ecm.mobile.handler;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @author bjalon
 *
 */
public class MobileRequestHandler implements RequestHandler {

    private static List<String> MOBILE_USER_AGENT_REGEXP = Arrays.asList(
            "(.*)Mobile(.*)Safari(.*)", "(.*)AppleWebKit(.*)Mobile(.*)");

    @Override
    public boolean isRequestRedirectedToApplication(HttpServletRequest request) {

        String userAgent = request.getHeader("User-Agent");

        for (String pattern : MOBILE_USER_AGENT_REGEXP) {
            if (userAgent.matches(pattern)) {
                return true;
            }
        }

        return false;
    }

}

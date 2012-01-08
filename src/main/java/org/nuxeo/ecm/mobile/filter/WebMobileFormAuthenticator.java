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
package org.nuxeo.ecm.mobile.filter;

import static org.nuxeo.ecm.platform.ui.web.auth.NXAuthConstants.ERROR_USERNAME_MISSING;
import static org.nuxeo.ecm.platform.ui.web.auth.NXAuthConstants.LOGIN_ERROR;
import static org.nuxeo.ecm.platform.ui.web.auth.NXAuthConstants.LOGIN_FAILED;
import static org.nuxeo.ecm.platform.ui.web.auth.NXAuthConstants.LOGIN_MISSING;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.common.utils.URIUtils;
import org.nuxeo.ecm.mobile.WebMobileConstants;
import org.nuxeo.ecm.platform.ui.web.auth.NXAuthConstants;
import org.nuxeo.ecm.platform.ui.web.auth.plugins.FormAuthenticator;

/**
 * Filter that redirects user to mobile authentication form if Mobile Browser
 * and user not authenticated.
 * 
 * @author bjalon
 * 
 */
public class WebMobileFormAuthenticator extends FormAuthenticator {

    protected static final Log log = LogFactory.getLog(WebMobileFormAuthenticator.class);

    protected static final String MOBILE_HOME_PAGE = "nxstartup.faces";

    @Override
    public Boolean needLoginPrompt(HttpServletRequest httpRequest) {
        if (WebMobileConstants.isMobileUserAgent(httpRequest)) {
            return Boolean.TRUE;
        }
        
        return Boolean.FALSE;
    }

    @Override
    public Boolean handleLoginPrompt(HttpServletRequest httpRequest,
            HttpServletResponse httpResponse, String baseURL) {
        
        if (WebMobileConstants.isMobileUserAgent(httpRequest)) {
            return super.handleLoginPrompt(httpRequest, httpResponse, baseURL);
        }
        
        return Boolean.FALSE;
    }

}

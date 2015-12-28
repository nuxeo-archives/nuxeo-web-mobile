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

package org.nuxeo.ecm.mobile.seam;

import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.mobile.handler.MobileRequestHandler;
import org.nuxeo.ecm.mobile.handler.RequestHandler;

/**
 * Actions Bean needed to build JSF actions
 *
 * @author <a href="mailto:bjalon@nuxeo.com">Benjamin JALON</a>
 * @since 5.5
 */
@Name("mobileApplicationActions")
@Scope(ScopeType.EVENT)
public class MobileApplicationActionsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public boolean isMobileBrowser() {
        RequestHandler handler = new MobileRequestHandler();
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return handler.isRequestRedirectedToApplication(request);
    }

    public String cleanCookie() {
        Cookie cookie = new Cookie("skipMobileRedirection", "false");
        cookie.setPath("/");

        ((HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse()).addCookie(cookie);
        return null;
    }

}

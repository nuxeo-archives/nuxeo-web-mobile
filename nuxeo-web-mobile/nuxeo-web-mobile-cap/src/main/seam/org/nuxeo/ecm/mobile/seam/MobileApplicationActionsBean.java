/*
 * (C) Copyright ${year} Nuxeo SA (http://nuxeo.com/) and contributors.
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

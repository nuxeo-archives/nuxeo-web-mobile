/*
 * (C) Copyright 2006-2008 Nuxeo SAS (http://nuxeo.com/) and contributors.
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
 *     bstefanescu
 *
 * $Id$
 */

package org.nuxeo.ecm.mobile.webengine.user;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.model.WebContext;
import org.nuxeo.runtime.api.Framework;

/**
 * @author <a href="mailto:bjalon@nuxeo.com">Benjamin JALON</a>
 * @since 5.5
 */
public class UserHelper {

    private static UserManager userManager;

    // Utility class.
    private UserHelper() {
    }

    /**
     * TODO merge org.nuxeo.ecm.core.rest.DocumentHelper and UserHelper
     *
     */
    public static DocumentModel updateUser(WebContext ctx, DocumentModel user) {
        try {
            /* XXX disable yet.
            FormData form = ctx.getForm();
            String password = ctx.getRequest().getParameter("user:password");
            if (password != null) {
                throw new WebException(
                        "You can inject password modification, please use the dedicated action");
            }
            form.fillDocument(user);
            user.putContextData(VersioningService.VERSIONING_OPTION,
                    form.getVersioningOption());
            Module module = ctx.getModule();
            Validator v = module.getValidator(user.getType());
            if (v != null) {
                user = v.validate(user);
            }

            getUserManager().updateUser(user);
            */
            return user;
        } catch (Exception e) {
            throw WebException.wrap("Failed to update document", e);
        }
    }

    private static UserManager getUserManager() throws Exception {
        if (userManager == null) {
            userManager = Framework.getService(UserManager.class);
        }
        return userManager;
    }

}

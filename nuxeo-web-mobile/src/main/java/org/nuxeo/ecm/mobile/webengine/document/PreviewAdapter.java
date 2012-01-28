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
package org.nuxeo.ecm.mobile.webengine.document;

import javax.ws.rs.GET;

import org.nuxeo.ecm.platform.preview.helper.PreviewHelper;
import org.nuxeo.ecm.webengine.WebEngine;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.model.WebAdapter;
import org.nuxeo.ecm.webengine.model.impl.DefaultAdapter;

/**
 * @author bjalon
 *
 */
@WebAdapter(name="preview", type="Preview", targetType="MobileDocument")
public class PreviewAdapter extends DefaultAdapter {

    @GET
    public Object doGet() {
      return getView("index");
    }
    
    public String getPreviewURL() {
        Object targetObject = ctx.getTargetObject();
        if (!(targetObject instanceof MobileDocument)) {
            throw new WebException("Target Object must be DocumentModel");
        }
        return WebEngine.getActiveContext().getServerURL() + "/nuxeo/"
                + PreviewHelper.getPreviewURL(((MobileDocument) targetObject).getDocument());
    }


}

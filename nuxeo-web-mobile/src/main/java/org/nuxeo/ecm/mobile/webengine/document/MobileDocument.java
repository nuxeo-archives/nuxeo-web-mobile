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

package org.nuxeo.ecm.mobile.webengine.document;

import javax.servlet.http.HttpServletRequest;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.model.PropertyException;
import org.nuxeo.ecm.core.rest.DocumentObject;
import org.nuxeo.ecm.platform.preview.helper.PreviewHelper;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.model.ResourceType;
import org.nuxeo.ecm.webengine.model.WebContext;

/**
 * This Class resolve a DocumentModel and expose differents restitutions
 * according Adapter defined (PreviewAdapter, CommentAdapter) in uri and "mode"
 * parameter given as parameter into the url. Default mode if given doesn't
 * exist or not set is view mode.
 * 
 * @author bjalon
 * 
 */
public class MobileDocument extends DocumentObject {

    public enum Mode {
        view, edit, create;

        /**
         * return the mode as enum and if value is unknown return the default
         * value.
         * 
         * @param value
         * @return
         */
        public static Mode valueOfWithDefault(String value) {
            for (Mode mode : Mode.values()) {
                if (mode.name().equalsIgnoreCase(value)) {
                    return mode;
                }
            }
            return view;
        }
    }

    public MobileDocument(WebContext ctx, DocumentRef docRef) {
        try {
            ResourceType resType = ctx.getModule().getType("Document");
            DocumentModel docModel = ctx.getCoreSession().getDocument(docRef);
            initialize(ctx, resType, docModel);
            ctx.push(this);
        } catch (Exception e) {
            throw WebException.wrap(e);
        }
    }

    @Override
    public Object doGet() {
        // must override the original doGet to override the resolution
        // so get parameter into the request instead injection
        HttpServletRequest request = ctx.getRequest();
        String mode = request.getParameter("mode");

        mode = Mode.valueOfWithDefault(mode).name();

        DFormJSONAdapter json = (DFormJSONAdapter) ctx.newObject("DFormJSON");
        return getView(mode).arg("doc",
                json.doGet(request.getRequestURI(), "post"));
    }

    public boolean hasPreview() throws PropertyException, ClientException {
        if (doc.hasSchema("file")
                && doc.getPropertyValue("file:content") != null) {
            return PreviewHelper.typeSupportsPreview(doc);
        }
        return false;
    }

}

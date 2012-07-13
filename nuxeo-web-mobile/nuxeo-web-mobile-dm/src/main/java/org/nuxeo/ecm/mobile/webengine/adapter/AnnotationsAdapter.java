/*
 * (C) Copyright 2006-2012 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Thomas Roger <troger@nuxeo.com>
 */

package org.nuxeo.ecm.mobile.webengine.adapter;

import java.net.URI;
import java.util.List;

import javax.ws.rs.GET;

import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.platform.annotations.api.Annotation;
import org.nuxeo.ecm.platform.annotations.api.AnnotationsService;
import org.nuxeo.ecm.platform.url.DocumentViewImpl;
import org.nuxeo.ecm.platform.url.api.DocumentView;
import org.nuxeo.ecm.platform.url.api.DocumentViewCodecManager;
import org.nuxeo.ecm.platform.web.common.vh.VirtualHostHelper;
import org.nuxeo.ecm.webengine.WebEngine;
import org.nuxeo.ecm.webengine.model.WebAdapter;
import org.nuxeo.ecm.webengine.model.WebContext;
import org.nuxeo.runtime.api.Framework;

/**
 * Adapter to retrieve annotations for the current {@code MobileDocument}.
 *
 * @author <a href="mailto:troger@nuxeo.com">Thomas Roger</a>
 * @since 5.5
 */
@WebAdapter(name = "annotations", type = "Annotations", targetType = "MobileDocument")
public class AnnotationsAdapter extends DefaultMobileAdapter {

    public static String DOCUMENT_PATH_CODED = "docpath";

    @GET
    public Object doGet() {
        return getView("index");
    }

    public List<Annotation> getAnnotations() throws Exception {
        AnnotationsService annotationsService = Framework.getLocalService(AnnotationsService.class);
        DocumentViewCodecManager documentViewCodecManager = Framework.getService(DocumentViewCodecManager.class);

        WebContext context = WebEngine.getActiveContext();
        CoreSession session = context.getCoreSession();

        DocumentView docView = new DocumentViewImpl(getDocumentModel());
        String documentUrl = documentViewCodecManager.getUrlFromDocumentView(
                DOCUMENT_PATH_CODED, docView, true,
                VirtualHostHelper.getBaseURL(context.getRequest()));

        return annotationsService.queryAnnotations(
                new URI(documentUrl), null,
                (NuxeoPrincipal) session.getPrincipal());
    }
}

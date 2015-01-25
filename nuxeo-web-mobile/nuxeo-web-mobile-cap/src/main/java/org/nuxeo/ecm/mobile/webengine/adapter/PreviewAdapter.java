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
package org.nuxeo.ecm.mobile.webengine.adapter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.Blobs;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.api.blobholder.SimpleBlobHolder;
import org.nuxeo.ecm.core.api.model.PropertyException;
import org.nuxeo.ecm.core.convert.api.ConversionService;
import org.nuxeo.ecm.mobile.webengine.document.MobileDocument;
import org.nuxeo.ecm.platform.preview.helper.PreviewHelper;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.model.WebAdapter;
import org.nuxeo.runtime.api.Framework;

/**
 * @author <a href="mailto:bjalon@nuxeo.com">Benjamin JALON</a>
 * @since 5.5
 */
@WebAdapter(name = "preview", type = "Preview", targetType = "MobileDocument")
public class PreviewAdapter extends DefaultMobileAdapter {
    private static final Log log = LogFactory.getLog(PreviewAdapter.class);

    private String nuxeoContextPath;

    @GET
    public Object doGet() {
        return getView("index");
    }

    public String getPreviewURL() {
        Object targetObject = ctx.getTargetObject();
        if (!(targetObject instanceof MobileDocument)) {
            throw new WebException("Target Object must be MobileDocument");
        }
        return getNuxeoContextPath() + "/" + PreviewHelper.getPreviewURL(((MobileDocument) targetObject).getDocument());
    }

    public String getPreviewContent() throws PropertyException, ClientException {
        Object targetObject = ctx.getTargetObject();
        if (!(targetObject instanceof MobileDocument)) {
            throw new WebException("Target Object must be MobileDocument");
        }
        DocumentModel document = ((MobileDocument) targetObject).getDocument();
        if (!document.hasSchema("note")) {
            throw new WebException("Can't produce preview for this document type");
        }
        String content = (String) document.getPropertyValue("note:note");
        String mimetype = (String) document.getPropertyValue("note:mime_type");
        return convertToHtml(content, mimetype);
    }

    private String convertToHtml(String text, String mimeType) {
        BlobHolder bh = new SimpleBlobHolder(Blobs.createBlob(text, mimeType, "UTF-8"));
        Map<String, Serializable> parameters = new HashMap<String, Serializable>();
        parameters.put("bodyContentOnly", Boolean.TRUE);
        try {
            bh = Framework.getLocalService(ConversionService.class).convertToMimeType("text/html", bh, parameters);
            text = bh.getBlob().getString();
        } catch (Exception e) {
            log.error("Failed to convert to HTML.", e);
        }
        return text;
    }

    private String getNuxeoContextPath() {
        if (nuxeoContextPath == null) {
            nuxeoContextPath = Framework.getProperty("org.nuxeo.ecm.contextPath");
        }
        return nuxeoContextPath;
    }

}

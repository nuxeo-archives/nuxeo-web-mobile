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

import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.GET;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.comment.api.CommentableDocument;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.model.WebAdapter;
import org.nuxeo.ecm.webengine.model.impl.DefaultAdapter;

/**
 * @author bjalon
 *
 */
@WebAdapter(name="comment", type="Comment", targetType="MobileDocument")
public class CommentAdapter extends DefaultAdapter {

    @GET
    public Object doGet() {
      return getView("index");
    }
    
    public List<DocumentModel> getComments() throws ClientException {
        Object targetObject = ctx.getTargetObject();
        if (!(targetObject instanceof MobileDocument)) {
            throw new WebException("Target Object must be MobileDocument");
        }
        
        MobileDocument doc = (MobileDocument) targetObject;

        CommentableDocument commentableDocument = doc.getDocument().getAdapter(CommentableDocument.class);
        
        List<DocumentModel> comments = commentableDocument.getComments();
        return comments;

    }

}

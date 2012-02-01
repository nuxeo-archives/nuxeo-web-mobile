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

import java.util.Calendar;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.ClientRuntimeException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.model.PropertyException;
import org.nuxeo.ecm.platform.comment.api.CommentableDocument;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.model.WebAdapter;
import org.nuxeo.ecm.webengine.model.impl.DefaultAdapter;

/**
 * Provide view and action on document comments
 * 
 * @author bjalon
 * 
 */
@WebAdapter(name = "comment", type = "Comment", targetType = "MobileDocument")
public class CommentAdapter extends DefaultAdapter {

    private MobileDocument doc;

    private CommentableDocument commentableDocument;

    @GET
    public Object doGet() {
        return getView("index");
    }

    @POST
    public Object doPost(@QueryParam("newComment") String newComment)
            throws PropertyException, ClientException {
        DocumentModel comment = ctx.getCoreSession().createDocumentModel(
                "Comment");
        initializeComment(comment);
        comment.setPropertyValue("comment:text", newComment);
        getCommentableDocument().addComment(comment);
        ctx.getCoreSession().saveDocument(getDocumentModel());
        return getView("index");
    }

    public List<DocumentModel> getComments() throws ClientException {
        List<DocumentModel> comments = getCommentableDocument().getComments();
        return comments;

    }

    private CommentableDocument getCommentableDocument() {
        if (commentableDocument == null) {
            commentableDocument = getDocumentModel().getAdapter(
                    CommentableDocument.class);
        }
        return commentableDocument;
    }

    private DocumentModel getDocumentModel() {
        if (doc == null) {
            doc = getMobileDocument();
        }
        return doc.getDocument();
    }

    private MobileDocument getMobileDocument() {
        Object targetObject = ctx.getTargetObject();
        if (!(targetObject instanceof MobileDocument)) {
            throw new WebException("Target Object must be MobileDocument");
        }

        MobileDocument mobileDoc = (MobileDocument) targetObject;
        return mobileDoc;
    }

    public boolean hasAddingCommentRight() throws ClientException {
        return ctx.getCoreSession().hasPermission(getDocumentModel().getRef(), "Write");
    }
    
    protected DocumentModel initializeComment(DocumentModel comment) {
        if (comment != null) {
            try {
                if (comment.getProperty("dublincore", "contributors") == null) {
                    String[] contributors = new String[1];
                    contributors[0] = ctx.getPrincipal().getName();
                    comment.setProperty("dublincore", "contributors",
                            contributors);
                }
            } catch (ClientException e) {
                throw new ClientRuntimeException(e);
            }
            try {
                if (comment.getProperty("dublincore", "created") == null) {
                    comment.setProperty("dublincore", "created",
                            Calendar.getInstance());
                }
            } catch (ClientException e) {
                throw new ClientRuntimeException(e);
            }
        }
        return comment;
    }


}

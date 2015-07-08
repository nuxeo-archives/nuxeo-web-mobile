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

import java.util.Calendar;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.model.PropertyException;
import org.nuxeo.ecm.platform.comment.api.CommentableDocument;
import org.nuxeo.ecm.webengine.model.WebAdapter;

/**
 * Provide view and action on document comments
 *
 * @author <a href="mailto:bjalon@nuxeo.com">Benjamin JALON</a>
 * @since 5.5
 */
@WebAdapter(name = "comment", type = "Comment", targetType = "MobileDocument")
public class CommentAdapter extends DefaultMobileAdapter {

    private CommentableDocument commentableDocument;

    @GET
    public Object doGet() {
        return getView("index");
    }

    @POST
    public Object doPost(@FormParam("newComment") String newTextComment) throws PropertyException {
        DocumentModel comment = initializeEmptyComment();
        comment.setPropertyValue("comment:text", newTextComment);
        getCommentableDocument().addComment(comment);
        ctx.getCoreSession().saveDocument(getDocumentModel());
        return getView("index");
    }

    @POST
    @Path("{commentIdParent}")
    public Object doPostReplyForm(@FormParam("newComment") String newTextComment,
            @PathParam("commentIdParent") String commentIdParent) throws PropertyException {
        DocumentModel commentParent = null;
        if (commentIdParent != null && !"null".equals(commentIdParent)) {
            commentParent = ctx.getCoreSession().getDocument(new IdRef(commentIdParent));
        }
        DocumentModel comment = initializeEmptyComment();
        comment.setPropertyValue("comment:text", newTextComment);
        if (commentParent != null) {
            getCommentableDocument().addComment(commentParent, comment);
        } else {
            getCommentableDocument().addComment(comment);
        }
        ctx.getCoreSession().saveDocument(getDocumentModel());
        return redirect(this.getPath());
    }

    @GET
    @Path("{commentIdParent}")
    public Object doGetReplyForm(@PathParam("commentIdParent") String commentIdParent) throws PropertyException {
        return getView("new").arg("parentId", commentIdParent);
    }

    @GET
    @Path("{commentId}/@delete")
    public Object doDeleteComment(@PathParam("commentId") String commentId) throws PropertyException {
        ctx.getCoreSession().removeDocument(new IdRef(commentId));
        return Response.ok().build();
    }

    public List<DocumentModel> getComments() {
        List<DocumentModel> comments = getCommentableDocument().getComments();
        return comments;

    }

    public List<DocumentModel> getComments(DocumentModel commentParent) {
        List<DocumentModel> comments = getCommentableDocument().getComments(commentParent);
        return comments;

    }

    public boolean hasWriteRightOnComment(DocumentModel comment) {
        return ctx.getCoreSession().hasPermission(comment.getRef(), "Write");
    }

    private CommentableDocument getCommentableDocument() {
        if (commentableDocument == null) {
            commentableDocument = getDocumentModel().getAdapter(CommentableDocument.class);
        }
        return commentableDocument;
    }

    public boolean hasAddingCommentRight() {
        return ctx.getCoreSession().hasPermission(getDocumentModel().getRef(), "Write");
    }

    protected DocumentModel initializeEmptyComment() {
        DocumentModel comment = ctx.getCoreSession().createDocumentModel("Comment");

        String[] contributors = new String[1];
        contributors[0] = ctx.getPrincipal().getName();
        comment.setProperty("dublincore", "contributors", contributors);
        comment.setProperty("dublincore", "created", Calendar.getInstance());
        comment.setProperty("comment", "author", ctx.getPrincipal().getName());
        comment.setProperty("comment", "creationDate", Calendar.getInstance());
        return comment;
    }

}

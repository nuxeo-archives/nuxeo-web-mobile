package org.nuxeo.ecm.mobile.webengine.sc;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.rating.api.LikeService;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.runtime.api.Framework;

@WebObject(type = "Like")
public class LikeObject extends AbstractLikeObject {

    private static final Log log = LogFactory.getLog(LikeObject.class);

    @GET
    public Object doGet() {
        LikeService ls = Framework.getLocalService(LikeService.class);
        String username = ctx.getCoreSession().getPrincipal().getName();
        if (getHasLiked()) {
            ls.cancel(username, doc);
        } else {
            ls.like(username, doc);
        }

        return Response.ok().build();
    }
}

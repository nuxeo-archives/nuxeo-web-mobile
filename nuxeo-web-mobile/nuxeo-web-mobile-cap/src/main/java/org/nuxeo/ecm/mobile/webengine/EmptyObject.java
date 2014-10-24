package org.nuxeo.ecm.mobile.webengine;

import javax.ws.rs.GET;
import javax.ws.rs.core.Response;

import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.DefaultObject;

@WebObject(type = "Empty")
public class EmptyObject extends DefaultObject {
    @GET
    public Object doGet() {
        return Response.status(404).build();
    }
}

package org.nuxeo.ecm.mobile.webengine.sc;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;

import org.json.JSONObject;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "hasLiked")
public class HasLikedObject extends AbstractLikeObject {

    @GET
    public Object doGet() {
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("hasLiked", getHasLiked());
        return new JSONObject(ret).toString();
    }

}

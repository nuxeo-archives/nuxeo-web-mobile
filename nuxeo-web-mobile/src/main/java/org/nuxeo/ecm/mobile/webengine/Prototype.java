package org.nuxeo.ecm.mobile.webengine;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.DefaultObject;

/**
 * Manage authentication form and logout action
 *
 * @author bjalon
 *
 */
@WebObject(type = "Prototype")
@Produces("text/html;charset=UTF-8")
public class Prototype extends DefaultObject {

    @GET
    @Path("{item1}/{item2}/{item3}")
    public Object doGetTemplate(@PathParam("item1") String item1, @PathParam("item2") String item2, @PathParam("item3") String item3) {
        return getView(item1 + "_" + item2 + "_" + item3).args(getParamsBen());
    }

    @GET
    @Path("{item1}/{item2}")
    public Object doGetTemplate(@PathParam("item1") String item1, @PathParam("item2") String item2) {
        return getView(item1 + "_" + item2).args(getParamsBen());
    }

    @GET
    @Path("{item1}")
    public Object doGetTemplate(@PathParam("item1") String item1) {
        return getView(item1).args(getParamsBen());
    }

    @GET
    public Object doGet() {
        return getView("index").args(getParamsBen());
    }

    protected Map<String, Object> getParamsBen() {
        Map<String, Object> result = new HashMap<String, Object>();
        HttpServletRequest request = getContext().getRequest();
        Enumeration<?> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            String value = request.getParameter(name);
            result.put(name, value);
        }
        return result;
    }

}

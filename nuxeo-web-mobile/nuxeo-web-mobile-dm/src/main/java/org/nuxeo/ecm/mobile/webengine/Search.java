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
package org.nuxeo.ecm.mobile.webengine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IterableQueryResult;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.model.Template;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.DefaultObject;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.search.ui.SearchUIService;

/**
 * Manage authentication form and logout action
 *
 * @author <a href="mailto:bjalon@nuxeo.com">Benjamin JALON</a>
 * @since 5.5
 *
 */
@WebObject(type = "Search")
@Produces("text/html;charset=UTF-8")
public class Search extends DefaultObject {

    private static final Log log = LogFactory.getLog(Search.class);

    private static final String QUERY_PATTERN = "SELECT ecm:uuid, dc:title, dc:description, common:icon "
            + "FROM Document WHERE ecm:fulltext = '%s' "
            + "AND (ecm:isCheckedInVersion = 0) "
            + "AND (ecm:mixinType != 'HiddenInNavigation') "
            + "AND (ecm:currentLifeCycleState != 'deleted') %s";

    private static final String ORDER_PATTERN = "ORDER BY %s";

    @GET
    public Template doGet(@QueryParam("q") String fulltext,
            @QueryParam("order") String orderParam,
            @QueryParam("max") Integer max) {
        String order = (orderParam != null && !"".equals(orderParam.trim())) ? String.format(
                ORDER_PATTERN, orderParam) : "";
        String query = String.format(QUERY_PATTERN, fulltext, order);

        return doGetNXQL(query, max).arg("fulltext", fulltext);
    }

    @GET
    @Path("nxql")
    public Template doGetNXQL(@QueryParam("q") String queryString,
            @QueryParam("max") Integer max) {
        CoreSession session = ctx.getCoreSession();
        IterableQueryResult docs;

        if (max == null) {
            max = 20;
        }

        try {
            docs = session.queryAndFetch(queryString, "NXQL");
        } catch (ClientException e) {
            log.error(e, e);
            throw new WebException(e.getMessage());
        }
        return getView("index").arg("docs", docs.iterator()).arg("size",
                docs.size()).arg("max", max).arg("q", queryString);
    }

    @GET
    @Path("faceted")
    public Object doGetFacetedSearches() throws ClientException {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("mySearches", mySearch());
        args.put("sharedSearches", sharedSearches());
        return getView("faceted-searches").args(args);
    }

    private List<DocumentModel> mySearch() throws ClientException {
        CoreSession session = ctx.getCoreSession();
        return getSearchService().getCurrentUserSavedSearches(
                session);
    }

    private Object sharedSearches() throws ClientException {
        CoreSession session = ctx.getCoreSession();
        return getSearchService().getSharedSavedSearches(
                session);
    }

    private SearchUIService getSearchService()
            throws ClientException {
        return Framework.getLocalService(SearchUIService.class);
    }
}

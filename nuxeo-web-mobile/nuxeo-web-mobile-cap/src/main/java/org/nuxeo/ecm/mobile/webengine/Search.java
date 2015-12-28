/*
 * (C) Copyright 2012 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IterableQueryResult;
import org.nuxeo.ecm.core.query.QueryParseException;
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
 */
@WebObject(type = "Search")
@Produces("text/html;charset=UTF-8")
public class Search extends DefaultObject {

    private static final Log log = LogFactory.getLog(Search.class);

    private static final String QUERY_PATTERN = "SELECT ecm:uuid, dc:title, dc:description, common:icon "
            + "FROM Document WHERE ecm:fulltext = '%s' " + "AND (ecm:isCheckedInVersion = 0) "
            + "AND (ecm:mixinType != 'HiddenInNavigation') " + "AND (ecm:currentLifeCycleState != 'deleted') %s";

    private static final String ORDER_PATTERN = "ORDER BY %s";

    @GET
    public Template doGet(@QueryParam("q") String fulltext, @QueryParam("order") String orderParam,
            @QueryParam("max") Integer max) {
        String order = (orderParam != null && !"".equals(orderParam.trim())) ? String.format(ORDER_PATTERN, orderParam)
                : "";
        String query = String.format(QUERY_PATTERN, fulltext, order);

        return doGetNXQL(query, max).arg("fulltext", fulltext);
    }

    @GET
    @Path("nxql")
    public Template doGetNXQL(@QueryParam("q") String queryString, @QueryParam("max") Integer max) {
        CoreSession session = ctx.getCoreSession();
        IterableQueryResult docs;

        if (max == null) {
            max = 20;
        }

        try {
            docs = session.queryAndFetch(queryString, "NXQL");
        } catch (QueryParseException e) {
            log.error(e, e);
            throw new WebException(e.getMessage());
        }
        return getView("index").arg("docs", docs.iterator()).arg("size", docs.size()).arg("max", max).arg("q",
                queryString);
    }

    @GET
    @Path("list")
    public Object doGetFacetedSearches() {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("mySearches", mySearch());
        args.put("sharedSearches", sharedSearches());
        return getView("saved-searches").args(args);
    }

    private List<DocumentModel> mySearch() {
        CoreSession session = ctx.getCoreSession();
        return getSearchService().getCurrentUserSavedSearches(session);
    }

    private Object sharedSearches() {
        CoreSession session = ctx.getCoreSession();
        return getSearchService().getSharedSavedSearches(session);
    }

    private SearchUIService getSearchService() {
        return Framework.getLocalService(SearchUIService.class);
    }
}

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
import javax.ws.rs.QueryParam;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.query.api.PageProvider;
import org.nuxeo.ecm.platform.query.api.PageProviderService;
import org.nuxeo.ecm.webengine.model.WebAdapter;
import org.nuxeo.runtime.api.Framework;

/**
 * @author <a href="mailto:bjalon@nuxeo.com">Benjamin JALON</a>
 * @since 5.5
 */
@WebAdapter(name = "faceted", type = "Faceted", targetType = "MobileDocument")
public class FacetedAdapter extends DefaultMobileAdapter {

    private PageProviderService service;

    @GET
    public Object doGet(@QueryParam("pageIndex")
    long pageIndex) throws Exception {

        PageProvider<?> pageProvider = getPageProvider(pageIndex);

        pageIndex = pageProvider.getCurrentPageIndex();

        Map<String, Object> args = new HashMap<String, Object>();
        args.put("page", pageProvider.getCurrentPage());
        args.put("pageNumber", pageProvider.getNumberOfPages());
        args.put("pageIndex", pageIndex);

        return getView("index").args(args);
    }

    private PageProvider<?> getPageProvider(long pageIndex)
            throws ClientException, Exception {
        Map<String, Serializable> prop = new HashMap<String, Serializable>();
        prop.put("coreSession", (Serializable) ctx.getCoreSession());

        PageProvider<?> pageProvider = getPageProviderService().getPageProvider(
                "search_core_default", null, 9L, null, prop);

        DocumentModel searchCriteria = getDocumentModel();
        pageProvider.setSearchDocumentModel(searchCriteria);

        if (pageIndex < 0) {
            pageIndex = 0;
        }
        // TODO : Waiting an improvement of CoreQuerySession if pageIndex > 0
        pageProvider.setCurrentPage(0);

        if (pageIndex > pageProvider.getNumberOfPages()) {
            pageIndex = pageProvider.getNumberOfPages();
        }
        pageProvider.setCurrentPage(pageIndex);
        return pageProvider;
    }

    public PageProviderService getPageProviderService() throws Exception {
        if (service == null) {
            service = Framework.getService(PageProviderService.class);
        }
        return service;
    }

}

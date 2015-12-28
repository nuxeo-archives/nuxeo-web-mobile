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
package org.nuxeo.ecm.mobile.webengine.adapter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.query.api.PageProvider;
import org.nuxeo.ecm.platform.query.api.PageProviderService;
import org.nuxeo.ecm.webengine.model.WebAdapter;
import org.nuxeo.runtime.api.Framework;

/**
 * @author <a href="mailto:bjalon@nuxeo.com">Benjamin JALON</a>
 * @since 5.5
 */
@WebAdapter(name = "mobileSearch", type = "DefaultSearch", targetType = "MobileDocument")
public class SearchAdapter extends DefaultMobileAdapter {

    private PageProviderService service;

    @GET
    public Object doGet(@QueryParam("pageIndex") long pageIndex) {

        PageProvider<?> pageProvider = getPageProvider(pageIndex);

        pageIndex = pageProvider.getCurrentPageIndex();

        Map<String, Object> args = new HashMap<String, Object>();
        args.put("page", pageProvider.getCurrentPage());
        args.put("pageNumber", pageProvider.getNumberOfPages());
        args.put("pageIndex", pageIndex);

        return getView("index").args(args);
    }

    private PageProvider<?> getPageProvider(long pageIndex) {
        Map<String, Serializable> prop = new HashMap<String, Serializable>();
        prop.put("coreSession", (Serializable) ctx.getCoreSession());

        PageProvider<?> pageProvider = getPageProviderService().getPageProvider("search_core_default", null, 9L, null,
                prop);

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

    public PageProviderService getPageProviderService() {
        if (service == null) {
            service = Framework.getService(PageProviderService.class);
        }
        return service;
    }

}

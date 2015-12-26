/*
 * (C) Copyright 2013 Nuxeo SA (http://nuxeo.com/) and others.
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
 *     Arnaud Kervern
 */
package org.nuxeo.ecm.mobile.webengine;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.test.DefaultRepositoryInit;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.ecm.platform.test.PlatformFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

/**
 * Redirect helper test case
 *
 * @author <a href="mailto:ak@nuxeo.com">Arnaud Kervern</a>
 * @since 5.7
 */

@RunWith(FeaturesRunner.class)
@Features(PlatformFeature.class)
@RepositoryConfig(init = DefaultRepositoryInit.class, cleanup = Granularity.METHOD)
@Deploy({ "org.nuxeo.ecm.platform.url.api", "org.nuxeo.ecm.platform.url.core" })
public class RedirectHelperTest {

    public static final String CODEC_NXDOC = "nxdoc/default/7d43f95a-9d25-4dcb-ad9c-bfce22eaa6ae/view_documents";

    public static final String CODEC_NXPATH = "nxpath/default/default-domain/workspaces/myDoc%20Cool@view_documents?tabIds=%3A";

    @Test
    public void testRedirectHelper() {
        DocumentRef documentRef = RedirectHelper.findDocumentRef(CODEC_NXDOC);
        Assert.assertEquals("7d43f95a-9d25-4dcb-ad9c-bfce22eaa6ae", documentRef.toString());

        documentRef = RedirectHelper.findDocumentRef(CODEC_NXPATH);
        Assert.assertEquals("/default-domain/workspaces/myDoc Cool", documentRef.toString());
    }
}

package org.nuxeo.ecm.mobile.webengine;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.platform.url.DocumentViewImpl;
import org.nuxeo.ecm.platform.url.api.DocumentView;
import org.nuxeo.ecm.platform.url.api.DocumentViewCodecManager;
import org.nuxeo.runtime.api.Framework;

/**
 * Utility class to handle redirection from a given targetURL
 * 
 * @author <a href="mailto:ak@nuxeo.com">Arnaud Kervern</a>
 * @since 5.7
 */
public class RedirectHelper {

    private static final Log log = LogFactory.getLog(RedirectHelper.class);

    public static String getJSFDocumentPath(DocumentModel doc, String baseUrl) {
        DocumentView view = new DocumentViewImpl(doc);
        return getCodecManager().getUrlFromDocumentView(view, true, baseUrl);
    }

    public static DocumentRef findDocumentRef(String targetURL) {
        if (StringUtils.isBlank(targetURL)) {
            log.debug("TargetURL is empty");
            return null;
        }

        DocumentView docView = getCodecManager().getDocumentViewFromUrl(
                targetURL, true, "");
        if (docView == null) {
            log.info("Unable to resolve docView for targetURL: " + targetURL);
            return null;
        }

        // Handle root pathRef, to redirect to workspace selection not on detail
        // view.
        PathRef docPathRef = docView.getDocumentLocation().getPathRef();
        if (docPathRef != null && docPathRef.equals(new PathRef("/"))) {
            log.debug("Trying to access root document using targetUrl");
            return null;
        }

        return docView.getDocumentLocation().getDocRef();
    }

    protected static DocumentViewCodecManager getCodecManager() {
        return Framework.getLocalService(DocumentViewCodecManager.class);
    }
}

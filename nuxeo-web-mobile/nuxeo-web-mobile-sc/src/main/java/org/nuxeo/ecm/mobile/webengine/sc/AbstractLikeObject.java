package org.nuxeo.ecm.mobile.webengine.sc;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.rating.api.LikeService;
import org.nuxeo.ecm.webengine.model.impl.DefaultObject;
import org.nuxeo.runtime.api.Framework;

public abstract class AbstractLikeObject extends DefaultObject {
    protected DocumentModel doc;

    @Override
    protected void initialize(Object... args) {
        super.initialize(args);
        doc = (DocumentModel) args[0];
    }

    protected boolean getHasLiked() {
        LikeService rs = Framework.getLocalService(LikeService.class);
        return rs.hasUserLiked(ctx.getCoreSession().getPrincipal().getName(),
                doc);
    }
}

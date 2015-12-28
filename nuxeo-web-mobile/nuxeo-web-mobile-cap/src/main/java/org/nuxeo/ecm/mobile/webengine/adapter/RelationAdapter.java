/*
 * (C) Copyright 2006-2012 Nuxeo SA (http://nuxeo.com/) and others.
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
 *     Thomas Roger <troger@nuxeo.com>
 */
package org.nuxeo.ecm.mobile.webengine.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;

import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.directory.Session;
import org.nuxeo.ecm.directory.api.DirectoryService;
import org.nuxeo.ecm.mobile.webengine.document.MobileDocument;
import org.nuxeo.ecm.platform.relations.api.Node;
import org.nuxeo.ecm.platform.relations.api.QNameResource;
import org.nuxeo.ecm.platform.relations.api.RelationManager;
import org.nuxeo.ecm.platform.relations.api.Resource;
import org.nuxeo.ecm.platform.relations.api.ResourceAdapter;
import org.nuxeo.ecm.platform.relations.api.Statement;
import org.nuxeo.ecm.platform.relations.api.Subject;
import org.nuxeo.ecm.platform.relations.api.impl.StatementImpl;
import org.nuxeo.ecm.platform.relations.api.util.RelationConstants;
import org.nuxeo.ecm.platform.relations.web.NodeInfo;
import org.nuxeo.ecm.platform.relations.web.NodeInfoImpl;
import org.nuxeo.ecm.platform.relations.web.StatementInfo;
import org.nuxeo.ecm.platform.relations.web.StatementInfoImpl;
import org.nuxeo.ecm.webengine.WebEngine;
import org.nuxeo.ecm.webengine.WebException;
import org.nuxeo.ecm.webengine.model.WebAdapter;
import org.nuxeo.runtime.api.Framework;

/**
 * Adapter to retrieve annotations for the current {@code MobileDocument}.
 * <p>
 * Most of the code comes from the {@link org.nuxeo.ecm.platform.relations.web.listener.ejb.RelationActionsBean} class.
 *
 * @author <a href="mailto:troger@nuxeo.com">Thomas Roger</a>
 * @author <a href="mailto:bjalon@nuxeo.com">Benjamin JALON</a>
 * @since 5.5
 */
@WebAdapter(name = "relation", type = "Relation", targetType = "MobileDocument")
public class RelationAdapter extends DefaultMobileAdapter {

    public static final String PREDICATES_DIRECTORY = "predicates";

    @GET
    public Object doGet() {
        return getView("index");
    }

    public Map<String, List<StatementInfo>> getRelations() {
        DocumentModel doc = getDocumentModel();
        RelationManager relationManager = Framework.getLocalService(RelationManager.class);
        Resource docResource = getDocumentResource(doc);

        List<StatementInfo> allStatements = new ArrayList<StatementInfo>();
        if (docResource != null) {
            Statement pattern = new StatementImpl(docResource, null, null);
            List<Statement> outgoingStatements = relationManager.getStatements(RelationConstants.GRAPH_NAME, pattern);
            allStatements.addAll(getStatementsInfo(outgoingStatements));

            pattern = new StatementImpl(null, null, docResource);
            List<Statement> incomingStatements = relationManager.getStatements(RelationConstants.GRAPH_NAME, pattern);
            allStatements.addAll(getStatementsInfo(incomingStatements));
        }

        Map<String, List<StatementInfo>> relations = new HashMap<String, List<StatementInfo>>();
        for (StatementInfo statement : allStatements) {
            String label = getPredicateLabel(PREDICATES_DIRECTORY, statement);
            if (!relations.containsKey(label)) {
                relations.put(label, new ArrayList<StatementInfo>());
            }
            List<StatementInfo> statements = relations.get(label);
            statements.add(statement);
            relations.put(label, statements);
        }
        return relations;
    }

    public boolean hasRelation() {
        Map<String, List<StatementInfo>> relation = getRelations();
        for (String key : relation.keySet()) {
            if (relation.get(key).size() > 0) {
                return true;
            }
        }
        return false;
    }

    private QNameResource getDocumentResource(DocumentModel document) {
        QNameResource documentResource = null;
        if (document != null) {
            documentResource = (QNameResource) Framework.getLocalService(RelationManager.class).getResource(
                    RelationConstants.DOCUMENT_NAMESPACE, document, null);
        }
        return documentResource;
    }

    private List<StatementInfo> getStatementsInfo(List<Statement> statements) {
        if (statements == null) {
            return null;
        }
        List<StatementInfo> infoList = new ArrayList<StatementInfo>();
        for (Statement statement : statements) {
            Subject subject = statement.getSubject();
            NodeInfo subjectInfo = new NodeInfoImpl(subject, getDocumentModel(subject), true);
            Resource predicate = statement.getPredicate();
            Node object = statement.getObject();
            NodeInfo objectInfo = new NodeInfoImpl(object, getDocumentModel(object), true);
            StatementInfo info = new StatementInfoImpl(statement, subjectInfo, new NodeInfoImpl(predicate), objectInfo);
            infoList.add(info);
        }
        return infoList;
    }

    private DocumentModel getDocumentModel(Node node) {
        if (node.isQNameResource()) {
            QNameResource resource = (QNameResource) node;
            CoreSession session = WebEngine.getActiveContext().getCoreSession();
            Map<String, Object> context = Collections.<String, Object> singletonMap(
                    ResourceAdapter.CORE_SESSION_CONTEXT_KEY, session);
            Object o = Framework.getLocalService(RelationManager.class).getResourceRepresentation(
                    resource.getNamespace(), resource, context);
            if (o instanceof DocumentModel) {
                return (DocumentModel) o;
            }
        }
        return null;
    }

    /**
     * Returns the predicate's label for a given {@code Statement}.
     */
    private String getPredicateLabel(String directoryName, StatementInfo statementInfo) {
        DirectoryService directoryService = Framework.getLocalService(DirectoryService.class);
        Session session = directoryService.open(directoryName);

        DocumentModel entry = session.getEntry(statementInfo.getPredicate().getUri());
        return (String) entry.getPropertyValue("vocabulary:label");
    }

}

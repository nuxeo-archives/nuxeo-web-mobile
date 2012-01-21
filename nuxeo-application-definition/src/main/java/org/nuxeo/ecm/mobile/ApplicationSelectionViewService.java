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
package org.nuxeo.ecm.mobile;

/**
 * This service store information about Application Selection View. Integrators
 * can activate a page that let the user choose the Application he wants to user
 * during his session work. Here we define if this possibility is enabled and if
 * the localization of this view. The service will guarantee this view will be
 * not protected by Nuxeo Authentication service.
 *
 * @author bjalon
 *
 */
public interface ApplicationSelectionViewService {

    /**
     * Return true if, at the begining of the session, the user will be redirected
     * to the Application Selection view.
     */
    public boolean isApplicationSelectionViewEnabled();

    /**
     * Return the ApplicationSelector URL where the user will be redirect at the
     * begining of his session.
     */
    public String getApplicationSelectionURL();
}

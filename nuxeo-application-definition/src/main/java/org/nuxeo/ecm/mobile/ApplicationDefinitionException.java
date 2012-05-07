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
 *     Benjamin JALON
 */
package org.nuxeo.ecm.mobile;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.WrappedException;


/**
 * @author <a href="mailto:bjalon@nuxeo.com">Benjamin JALON</a>
 *
 */
public class ApplicationDefinitionException extends Exception {

    private static final long serialVersionUID = 1L;


    public ApplicationDefinitionException() {
    }

    public ApplicationDefinitionException(String message) {
        super(message);
    }

    public ApplicationDefinitionException(String message, ApplicationDefinitionException cause) {
        super(message, cause);
    }

    public ApplicationDefinitionException(String message, Throwable cause) {
        super(message, WrappedException.wrap(cause));
    }

    public ApplicationDefinitionException(Throwable cause) {
        super(WrappedException.wrap(cause));
    }

    public ApplicationDefinitionException(ApplicationDefinitionException cause) {
        super(cause);
    }

    public static ApplicationDefinitionException wrap(Throwable exception) {
        ApplicationDefinitionException appDefException;

        if (null == exception) {
            appDefException = new ApplicationDefinitionException(
                    "Root exception was null. Please check your code.");
        } else {
            if (exception instanceof ClientException) {
                appDefException = (ApplicationDefinitionException) exception;
            } else {
                appDefException = new ApplicationDefinitionException(
                        exception.getLocalizedMessage(), exception);
            }
        }
        return appDefException;
    }
    
}

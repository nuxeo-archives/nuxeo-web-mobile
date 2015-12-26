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
 *     Arnaud Kervern
 */
package org.nuxeo.ecm.mobile.webengine;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class MatcherTest {

    protected static final String CORDOVA_USER_AGENT = "Mozilla/5.0 (iPhone Simulator; CPU iPhone OS 5_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Mobile/9B176 Cordova/1.7 (iOS, iPhone)";

    @Test
    public void testMatcher() {
        Matcher matcher = MobileApplication.CORDOVA_USER_AGENT_REGEXP.matcher(CORDOVA_USER_AGENT);
        assertTrue(matcher.find(0));
        do {
            assertEquals("1.7", matcher.group(1));
            assertEquals("iOS, iPhone", matcher.group(2));
        } while (matcher.find());
    }
}

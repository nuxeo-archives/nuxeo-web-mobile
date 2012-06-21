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

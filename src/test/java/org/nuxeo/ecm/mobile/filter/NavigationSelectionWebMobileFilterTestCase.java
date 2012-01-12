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
package org.nuxeo.ecm.mobile.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import static org.nuxeo.ecm.mobile.WebMobileConstants.FORCE_STANDARD_NAVIGATION_COOKIE_NAME;
import static org.nuxeo.ecm.mobile.WebMobileConstants.getNavigationSelectionURL;
import static org.nuxeo.ecm.mobile.WebMobileConstants.isUnitTestExecution;

/**
 * @author bjalon
 *
 */
public class NavigationSelectionWebMobileFilterTestCase {

    private WebMobileNavigationSelectionFilter filter;

    @Before
    public void init() throws ServletException {

        isUnitTestExecution = true;

        FilterConfig fc = mock(FilterConfig.class);
        when(fc.getInitParameter("navigation.selection.url")).thenReturn(
                "/web-mobile/navigation-selection.jsp");
        filter = new WebMobileNavigationSelectionFilter();
        filter.init(fc);

    }

    @Test
    public void shouldNoRedirectIfUserAgentNotMobile() throws IOException,
            ServletException {

        String initialURI = null;
        boolean isMobile = false;
        Cookie[] cookies = new Cookie[0];

        HttpServletRequest request = initializeRequest(initialURI, isMobile,
                cookies);

        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);
        verifyZeroInteractions(response);

    }

    @Test
    public void shouldRedirectToNavigationChoiceViewIfMobileBrowserAndNoCookieAndNotMobileURL()
            throws IOException, ServletException {

        String initialURI = "/nuxeo/anything/else/url";
        boolean isMobile = true;
        Cookie[] cookies = new Cookie[0];

        String redirectURLExpected = "/nuxeo/web-mobile/navigation-choice.jsp?"
                + "initialURLRequested=%2Fnuxeo%2Fanything%2Felse%2Furl";

        // Create a mock request with target URL and no cookie
        HttpServletRequest request = initializeRequest(initialURI, isMobile,
                cookies);

        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);
        verify(response, times(1)).sendRedirect(redirectURLExpected);
    }

    @Test
    public void shouldNoRedirectIfUserAgentMobileAndUserChooseStandardNavigation()
            throws IOException, ServletException {

        String initialURI = "/nuxeo/anything/else/url";
        boolean isMobile = true;
        Cookie[] cookies = initializeCookiesWithStandardNavigationChoice("true");

        // Create a mock request with target URL and no cookie
        HttpServletRequest request = initializeRequest(initialURI, isMobile,
                cookies);

        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);
        verifyZeroInteractions(response);
    }

    @Test
    public void shouldNoRedirectIfUserAgentMobileAndUserChooseMobileNavigation()
            throws IOException, ServletException {

        String initialURI = "/nuxeo/anything/else/url";
        boolean isMobile = true;
        Cookie[] cookies = initializeCookiesWithStandardNavigationChoice("false");

        // Create a mock request with target URL and no cookie
        HttpServletRequest request = initializeRequest(initialURI, isMobile,
                cookies);

        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);
        verifyZeroInteractions(response);
    }

    @Test
    public void shouldNoRedirectIfMobileUserAgentAndURLIsNavigationChoiceAndNoNavigationChoiceSet()
            throws IOException, ServletException {

        String initialURI = getNavigationSelectionURL();
        boolean isMobile = true;
        Cookie[] cookies = new Cookie[0];

        // Create a mock request with target URL and no cookie
        HttpServletRequest request = initializeRequest(initialURI, isMobile,
                cookies);

        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);
        // TODO: Enabled runtime and enable PluggableAuthenticationService with contribution
        // verifyZeroInteractions(response);
    }

    @Test
    public void shouldNoRedirectIfUserAgentURLIsNavigationChoiceAndMobileNavigationSet()
            throws IOException, ServletException {

        String initialURI = getNavigationSelectionURL();
        boolean isMobile = true;
        Cookie[] cookies = initializeCookiesWithStandardNavigationChoice("false");

        // Create a mock request with target URL and no cookie
        HttpServletRequest request = initializeRequest(initialURI, isMobile,
                cookies);

        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);
        verifyZeroInteractions(response);
    }

    /**
     * Initial a servlet request with the given parameters
     *
     * @param uri Initial URI send by the browser
     * @param isMobileBrowser Is the Browser a mobile or not
     * @param cookies Arrays of cookie sent by the browser
     * @return
     */
    private HttpServletRequest initializeRequest(String uri,
            boolean isMobileBrowser, Cookie[] cookies) {
        HttpServletRequest request = mock(HttpServletRequest.class);

        if (isMobileBrowser) {
            when(request.getHeader("User-Agent")).thenReturn(MOBILE_USER_AGENT);

        } else {
            when(request.getHeader("User-Agent")).thenReturn(DESKTOP_USER_AGENT);
        }
        when(request.getCookies()).thenReturn(cookies);
        when(request.getRequestURI()).thenReturn(uri);
        when(request.getRequestURL()).thenReturn(
                new StringBuffer("http://servername:port/serverContext" + uri));

        return request;
    }

    /**
     * Initialize a Cookie array with the cookie navigation choice.
     *
     * @param isStandardNavigationChosen If true the choice set will be Standard
     *            navigation esle Mobile Navigation.
     * @return
     */
    private Cookie[] initializeCookiesWithStandardNavigationChoice(
            String isStandardNavigationChosen) {
        Cookie[] cookies = new Cookie[1];
        Cookie cookie = mock(Cookie.class);
        when(cookie.getName()).thenReturn(FORCE_STANDARD_NAVIGATION_COOKIE_NAME);
        when(cookie.getValue()).thenReturn(isStandardNavigationChosen);
        cookies[0] = cookie;
        return cookies;

    }

    private static final String DESKTOP_USER_AGENT = "Mozilla/5.0 "
            + "(Macintosh; U; Intel Mac OS X 10_6_6; en-us) "
            + "AppleWebKit/533.20.25 (KHTML, like Gecko) Version/5.0.4 Safari/533.20.27";

    private static final String MOBILE_USER_AGENT = "Mozilla/5.0 "
            + "(iPod; U; CPU iPhone OS 2_2_1 like Mac OS X; en-us) "
            + "AppleWebKit/525.18.1 (KHTML, like Gecko) Version/3.1.1 Mobile/5H11a Safari/525.20";

}

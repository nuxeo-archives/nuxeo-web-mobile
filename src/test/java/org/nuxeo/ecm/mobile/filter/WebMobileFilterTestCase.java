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
import static org.nuxeo.ecm.mobile.WebMobileConstants.MOBILE_HOME_URL_SUFFIX;
import static org.nuxeo.ecm.mobile.WebMobileConstants.NAVIGATION_SELECTION_URL_SUFFIX;;

/**
 * @author bjalon
 * 
 */
public class WebMobileFilterTestCase {

    private WebMobileFilter filter;

    /**
     * Is the browser that has generated the request is a mobile browser
     */
    private boolean isMobile;

    /**
     * Initial request called by the browser
     */
    private StringBuffer initialURL;

    /**
     * redirection result expected
     */
    private String redirectURLExpected;

    @Before
    public void init() throws ServletException {

        // initialize the filter to return the value is mobile for the user
        // Agent and take the default context path
        filter = new WebMobileFilter() {

            @Override
            protected boolean isMobileBrowserRequester(HttpServletRequest req) {
                return isMobile;
            }

            @Override
            protected String getContextPath() {
                return "/nuxeo";
            }

        };
        filter.init(null);

    }

    @Test
    public void shouldNoRedirectIfUserAgentNotMobile() throws IOException,
            ServletException {

        // ***** INITIALIZATION ***********
        // filter will detect user agent not mobile
        isMobile = false;
        // No redirection for any initial URL
        HttpServletRequest request = mock(HttpServletRequest.class);

        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        // ***** END INITIALIZATION ***********

        filter.doFilter(request, response, chain);
        verifyZeroInteractions(response);

    }

    @Test
    public void shouldRedirectToNavigationChoiceViewIfUserAgentMobileAndNoCookieAndNotMobileURL()
            throws IOException, ServletException {

        // ***** INITIALIZATION ***********
        String initialURI = "/nuxeo/anything/else/than/mobile/url";
        initialURL = new StringBuffer("http://ip:port" + initialURI);
        redirectURLExpected = "/nuxeo/mobile/ask-mobile-or-standard-navigation.jsp"
                + "?initialURLRequested=%2Fnuxeo%2Fanything%2Felse%2Fthan%2Fmobile%2Furl";

        // filter will detect a mobile user agent
        isMobile = true;

        // Create a mock request with initial URL and no cookie
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(initialURI);
        when(request.getRequestURL()).thenReturn(initialURL);
        when(request.getCookies()).thenReturn(new Cookie[0]);

        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        // ***** INITIALIZATION ***********

        filter.doFilter(request, response, chain);
        verify(response, times(1)).sendRedirect(redirectURLExpected);
    }

    @Test
    public void shouldNoRedirectIfUserAgentMobileAndUserChooseStandardNavigation()
            throws IOException, ServletException {

        // ***** INITIALIZATION ***********
        initialURL = new StringBuffer("http://ip:port/content");
        redirectURLExpected = null;
        String isStandardNavigationChosen = "true";

        // filter will detect a mobile user agent
        isMobile = true;

        // Create a mock request with initial URL and no cookie
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(initialURL);
        Cookie[] cookies = initializeCookies(isStandardNavigationChosen);
        when(request.getCookies()).thenReturn(cookies);

        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        // ***** INITIALIZATION ***********

        filter.doFilter(request, response, chain);
        verifyZeroInteractions(response);
    }

    @Test
    public void shouldNoRedirectIfUserAgentMobileAndUserChooseMobileNavigationAndURLIsMobile()
            throws IOException, ServletException {

        // ***** INITIALIZATION ***********
        isMobile = true;
        String initialURI = filter.getContextPath() + MOBILE_HOME_URL_SUFFIX
                + "/anything/you/want";
        initialURL = new StringBuffer("http://ip:port" + initialURI);
        String isStandardNavigationChosen = "false";

        // Create a mock request with initial URL and cookie that tells the user
        // has chosen mobile navigation
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(initialURL);
        when(request.getRequestURI()).thenReturn(initialURI);
        Cookie[] cookies = initializeCookies(isStandardNavigationChosen);
        when(request.getCookies()).thenReturn(cookies);

        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        // ***** INITIALIZATION ***********

        filter.doFilter(request, response, chain);
        verifyZeroInteractions(response);
    }

    @Test
    public void shouldNoRedirectIfUserAgentMobileAndUserChooseMobileNavigationAndInitialURLisNavigationChoice()
            throws IOException, ServletException {

        // ***** INITIALIZATION ***********
        isMobile = true;
        String initialURI = filter.getContextPath() + NAVIGATION_SELECTION_URL_SUFFIX;
        initialURL = new StringBuffer("http://ip:port" + initialURI);
        String isStandardNavigationChosen = "false";

        // Create a mock request with initial URL and cookie that tells the user
        // has chosen mobile navigation
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(initialURL);
        when(request.getRequestURI()).thenReturn(initialURI);
        Cookie[] cookies = initializeCookies(isStandardNavigationChosen);
        when(request.getCookies()).thenReturn(cookies);

        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        // ***** INITIALIZATION ***********

        filter.doFilter(request, response, chain);
        verifyZeroInteractions(response);
    }

    @Test
    public void shouldRedirectToMobileHomeIfUserAgentMobileAndUserChooseMobileNavigationAndURLIsNotMobile()
            throws IOException, ServletException {

        // ***** INITIALIZATION ***********
        String initialURI = "/nuxeo/anything/else/than/mobile/url";
        initialURL = new StringBuffer("http://ip:port" + initialURI);
        redirectURLExpected = "/nuxeo/site/mobile/home"
                + "?initialURLRequested=%2Fnuxeo%2Fanything%2Felse%2Fthan%2Fmobile%2Furl";

        // filter will detect a mobile user agent
        isMobile = true;
        String isStandardNavigationChosen = "false";

        // Create a mock request with initial URL and no cookie
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(initialURI);
        when(request.getRequestURL()).thenReturn(initialURL);
        Cookie[] cookies = initializeCookies(isStandardNavigationChosen);
        when(request.getCookies()).thenReturn(cookies);

        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        // ***** INITIALIZATION ***********

        filter.doFilter(request, response, chain);
        verify(response, times(1)).sendRedirect(redirectURLExpected);
    }

    private Cookie[] initializeCookies(String isStandardNavigationChosen) {
        Cookie[] cookies = new Cookie[1];
        Cookie cookie = mock(Cookie.class);
        when(cookie.getName()).thenReturn(FORCE_STANDARD_NAVIGATION_COOKIE_NAME);
        when(cookie.getValue()).thenReturn(isStandardNavigationChosen);
        cookies[0] = cookie;
        return cookies;

    }

}

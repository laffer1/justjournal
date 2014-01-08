package com.justjournal;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public final class CrawlServlet implements Filter {

    @Override
    public void init(FilterConfig config) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        ServletOutputStream out = response.getOutputStream();
        String escapedFragment = request.getParameter("_escaped_fragment_");

        if (escapedFragment != null) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
                         /*
                          * Rewrite the URL back to the original #! version.
                          */
            String urlWithHashFragment = httpRequest.getScheme() + "://"
                    + httpRequest.getServerName() + ":"
                    + httpRequest.getServerPort()
                    + httpRequest.getContextPath()
                    + httpRequest.getServletPath();

            String pathInfo = httpRequest.getPathInfo();
            if (pathInfo != null) {
                urlWithHashFragment += pathInfo;
            }

            String queryString = httpRequest.getQueryString();
            Pattern pattern = Pattern
                    .compile("(.*&)?_escaped_fragment_=[^&]*(&(.*))?");
            Matcher matcher = pattern.matcher(queryString);

            if (matcher.matches()) {
                urlWithHashFragment += "?" + matcher.group(1)
                        + matcher.group(3);
            }

                         /*
                          * TODO Unescape %XX characters.
                          */
            urlWithHashFragment += "#!" + escapedFragment;

                         /*
                          * Use the headless browser (HtmlUnit) to obtain an HTML snapshot.
                          */
            final WebClient webClient = new WebClient();
            HtmlPage page = webClient.getPage(urlWithHashFragment);

                         /*
                          * Give the headless browser enough time to execute JavaScript. The
                          * exact time to wait may depend on your application.
                          */
            webClient.waitForBackgroundJavaScript(2000);

                         /*
                          * Return the snapshot.
                          */
            out.println(page.asXml());
        } else {
            try {
                                 /*
                                  * Not an _escaped_fragment_ URL, so move up the chain of
                                  * servlet filters.
                                  */
                chain.doFilter(request, response);
            } catch (ServletException e) {
                System.err.println("Servlet exception caught: " + e);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void destroy() {

    }
}
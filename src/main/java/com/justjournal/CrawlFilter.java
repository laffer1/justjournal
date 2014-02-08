package com.justjournal;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CrawlFilter implements Filter {
    private Logger log = LoggerFactory.getLogger(CrawlFilter.class);

    private FilterConfig filterConfig = null;

    private static String rewriteQueryString(String queryString) throws UnsupportedEncodingException {
        StringBuilder queryStringSb = new StringBuilder(queryString);
        int i = queryStringSb.indexOf("&_escaped_fragment_");
        if (i != -1) {
            StringBuilder tmpSb = new StringBuilder(queryStringSb.substring(0, i));
            tmpSb.append("#!");
            tmpSb.append(URLDecoder.decode(queryStringSb.substring(i + 20, queryStringSb.length()), "UTF-8"));
            queryStringSb = tmpSb;
        }

        i = queryStringSb.indexOf("_escaped_fragment_");
        if (i != -1) {
            StringBuilder tmpSb = new StringBuilder(queryStringSb.substring(0, i));
            tmpSb.append("#!");
            tmpSb.append(URLDecoder.decode(queryStringSb.substring(i + 19, queryStringSb.length()), "UTF-8"));
            queryStringSb = tmpSb;
        }
        if (queryStringSb.indexOf("#!") != 0) {
            queryStringSb.insert(0, '?');
        }
        queryString = queryStringSb.toString();


        return queryString;
    }

    /**
     * Initializes the filter configuration
     */
    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Filters all requests and invokes headless browser if necessary
     */
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException {
        if (filterConfig == null) {
            return;
        }

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String queryString = req.getQueryString();

        if ((queryString != null) && (queryString.contains("_escaped_fragment_"))) {
            StringBuilder pageNameSb = new StringBuilder(req.getScheme() + "://");
            pageNameSb.append(req.getServerName());
            if (req.getServerPort() != 0) {
                pageNameSb.append(":");
                pageNameSb.append(req.getServerPort());
            }
            pageNameSb.append(req.getRequestURI());
            queryString = rewriteQueryString(queryString);
            pageNameSb.append(queryString);

            final WebClient webClient = new WebClient(BrowserVersion.CHROME);
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            webClient.getOptions().setJavaScriptEnabled(true);
            String pageName = pageNameSb.toString();
            HtmlPage page = webClient.getPage(pageName);
            webClient.waitForBackgroundJavaScriptStartingBefore(2000);
            webClient.waitForBackgroundJavaScript(8000);

            res.setContentType("text/html;charset=UTF-8");
            PrintWriter out = res.getWriter();
            out.println("<hr>");
            out.println("<center><h3>You are viewing a non-interactive page that is intended for the crawler.  You probably want to see this page: <a href=\""
                    + pageName + "\">" + pageName + "</a></h3></center>");
            out.println("<hr>");

            out.println(page.asXml());
            webClient.closeAllWindows();
            out.close();

        } else {
            try {
                chain.doFilter(request, response);
            } catch (ServletException e) {
                log.error(e.getMessage());
            }
        }
    }

    public void doFilterOld(ServletRequest request, ServletResponse response,
                            FilterChain chain) throws IOException, ServletException {
        if (filterConfig == null) {
            return;
        }

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
                log.error(e.getMessage());
            }
        }
    }

    /**
     * Destroys the filter configuration
     */
    @Override
    public void destroy() {
        this.filterConfig = null;
    }
}
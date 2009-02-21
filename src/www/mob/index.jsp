<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.1//EN"
    "http://www.openmobilealliance.org/tech/DTD/xhtml-mobile11.dtd">
<%@ page contentType="application/xhtml+xml; charset=utf-8" language="java" %>
<%@ page import="com.justjournal.User" %>
<%@ page import="com.justjournal.WebError" %>
<%@ page import="com.justjournal.core.Statistics" %>
<%@ page import="com.justjournal.db.*" %>
<%@ page import="com.justjournal.search.BaseSearch" %>
<%@ page import="com.justjournal.utility.StringUtil" %>
<%@ page import="com.justjournal.utility.Xml" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="java.io.*" %>
<%@ page import="java.net.URL" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.text.ParsePosition" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Collection,
                 java.util.Iterator" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
    <head>
        <title>JustJournal.com: Mobile</title>
        <link rel="stylesheet" type="text/css" href="mobile.css" />
    </head>

    <body>
    <div id="header">
        <h1>JustJournal.com</h1>
    </div>
         <% if (session.getAttribute("auth.user") != null) { %>
        <p><%= session.getAttribute("auth.user") %>, you can: </p>
         <ul>
             <li><a href="update.jsp">Update Journal</a></li>
             <li><a href="logout.jsp">Log out</a></li>
          </ul>
    <% } else { %>
    <p><a href="login.jsp">Login</a></p>
    <% } %>
<%!

private static final String PAGEAD =
    "http://pagead2.googlesyndication.com/pagead/ads?";

private void googleAppendUrl(StringBuilder url, String param, String value)
    throws UnsupportedEncodingException {
  if (value != null) {
    String encodedValue = URLEncoder.encode(value, "UTF-8");
    url.append("&").append(param).append("=").append(encodedValue);
  }
}

private void googleAppendColor(StringBuilder url, String param,
    String value, long random) {
  String[] colorArray = value.split(",");
  url.append("&").append(param).append("=").append(
      colorArray[(int)(random % colorArray.length)]);
}

private void googleAppendScreenRes(StringBuilder url, String uaPixels,
    String xUpDevcapScreenpixels) {
  String screenRes = uaPixels;
  String delimiter = "x";
  if (uaPixels == null) {
    screenRes = xUpDevcapScreenpixels;
    delimiter = ",";
  }
  if (screenRes != null) {
    String[] resArray = screenRes.split(delimiter);
    if (resArray.length == 2) {
      url.append("&u_w=").append(resArray[0]);
      url.append("&u_h=").append(resArray[1]);
    }
  }
}

%>
<%

long googleDt = System.currentTimeMillis();
String googleHost = (request.isSecure() ? "https://" : "http://")
    + request.getHeader("Host");

StringBuilder googleAdUrlStr = new StringBuilder(PAGEAD);
googleAdUrlStr.append("ad_type=text_image");
googleAdUrlStr.append("&channel=8400186772");
googleAdUrlStr.append("&client=ca-mb-pub-1321195614665440");
googleAdUrlStr.append("&dt=").append(googleDt);
googleAdUrlStr.append("&format=mobile_single");
googleAppendUrl(googleAdUrlStr, "host", googleHost);
googleAppendUrl(googleAdUrlStr, "ip", request.getRemoteAddr());
googleAdUrlStr.append("&markup=xhtml");
googleAdUrlStr.append("&oe=utf8");
googleAdUrlStr.append("&output=xhtml");
googleAppendUrl(googleAdUrlStr, "ref", request.getHeader("Referer"));
String googleUrl = request.getRequestURL().toString();
if (request.getQueryString() != null) {
  googleUrl += "?" + request.getQueryString().toString();
}
googleAppendUrl(googleAdUrlStr, "url", googleUrl);
googleAppendUrl(googleAdUrlStr, "useragent", request.getHeader("User-Agent"));
googleAppendScreenRes(googleAdUrlStr, request.getHeader("UA-pixels"),
    request.getHeader("x-up-devcap-screenpixels"));

try {
  URL googleAdUrl = new URL(googleAdUrlStr.toString());
  BufferedReader reader = new BufferedReader(
      new InputStreamReader(googleAdUrl.openStream(), "UTF-8"));
  for (String line; (line = reader.readLine()) != null;) {
    out.println(line);
  }
} catch (IOException e) {}

%>
    </body>
</html>
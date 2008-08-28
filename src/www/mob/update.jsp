<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.1//EN"
    "http://www.openmobilealliance.org/tech/DTD/xhtml-mobile11.dtd">
<%@ page contentType="application/xhtml+xml; charset=utf-8" language="java" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
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
<%
    response.setHeader("Vary", "Accept"); // content negotiation
    response.setDateHeader("Expires", System.currentTimeMillis());
    response.setDateHeader("Last-Modified", System.currentTimeMillis());
    response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
    response.setHeader("Pragma", "no-cache");

    // date stuff
    java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    java.sql.Date now = new java.sql.Date(System.currentTimeMillis());

// Get the session user input
    String sbody = (String) session.getAttribute("spell.body");
    String smusic = (String) session.getAttribute("spell.music");
    String ssubject = (String) session.getAttribute("spell.subject");
    String stags = (String) session.getAttribute("spell.tags");
    String strackback = (String) session.getAttribute("spell.trackback");

    if (sbody == null)
        sbody = "";

    if (smusic == null)
        smusic = "";

    if (ssubject == null)
        ssubject = "";

    if (stags == null)
        stags = "";

    if (strackback == null)
        strackback = "";

    Integer userID = (Integer) session.getAttribute("auth.uid");
    int ival = 0;
    if (userID != null) {
        ival = userID.intValue();
    }

%>

    <head>
        <title>JustJournal.com: Update Journal</title>
    </head>

    <body>
        <h1>JustJournal.com</h1>
        <h2>Update Journal</h2>
         <% if (session.getAttribute("auth.user") != null) { %>
        <p><%= session.getAttribute("auth.user") %>, you can: </p>
         <ul>
             <li><a href="logout.jsp">Log out</a></li>
          </ul>

        <form method="post" action="../updateJournal" id="frmUpdateJournal">
          <fieldset>

               <input type="hidden" name="mood" id="mood" value="12"/>
               <input type="hidden" name="location" id="location" value="0"/>
               <input type="hidden" name="client" id="client" value="mobile"/>

    <div class="row">
        <span class="label"><label for="date">Date</label></span>
	  <span class="formw"><input name="date" id="date" value="<%=fmt.format( now ) %>" size="25" maxlength="19"/>
	  </span>
    </div>

    <div class="row">
        <span class="label"><label for="subject">Subject</label></span>
	  <span class="formw"><input name="subject" type="text" id="subject" size="25" maxlength="150"
                                 value="<%=ssubject%>"/>
	  (optional)</span>
    </div>

    <div class="row">
        <span class="label"><label for="body">Body</label></span>
        <span class="formw"><textarea id="body" name="body" style="width: 100%" cols="50"
            rows="20"><%=sbody%></textarea></span>
    </div>

        <div class="row">
    <span class="label"><label for="security">Security</label></span>
	  <span class="formw">
	  	<select id="security" name="security" size="1">
              <%
                  Integer ssec = (Integer) session.getAttribute("spell.security");
                  int issec = 0;
                  if (ssec != null) {
                      issec = ssec.intValue();
                  }

                  /* Check to see if the Journal is marked private */
                  boolean prvt = false;
                  try {
                      prvt = (ival > 0 &&
                              new User((String) session.getAttribute("auth.user")).isPrivateJournal());
                  } catch (Exception e) {
                      // ignore exception.
                  }


                  for (java.util.Iterator iterator = SecurityDao.view().iterator(); iterator.hasNext();) {
                      SecurityTo o = (SecurityTo) iterator.next();

                      out.print("\t<option value=\"" + o.getId());

                      if (prvt && o.getName().compareTo("private") == 0)
                          out.print("\" selected=\"selected\">");
                      else if ((o.getName().compareTo("public") == 0 && !prvt)
                              || o.getId() == issec)
                          out.print("\" selected=\"selected\">");
                      else
                          out.print("\">");

                      out.println(o.getName() + "</option>");
                  }
              %>
          </select>
	  </span>
</div>

              <div class="row"><input type="submit" name="submit" value="submit"/></div>
   
</fieldset>
       </form>

    <% } else { %>
    <p><a href="login.jsp">Login</a></p>
    <% } %>

    <%@ page import="java.util.Collection,
                 java.util.Iterator" %>
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
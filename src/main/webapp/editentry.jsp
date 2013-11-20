<?xml version="1.0" encoding="UTF-8"?>
<%@ page contentType="text/html; charset=UTF-8" language="java" import="com.justjournal.User" %>
<%@ page import="com.justjournal.WebError" %>
<%@ page import="com.justjournal.core.Statistics" %>
<%@ page import="com.justjournal.db.*" %>
<%@ page import="com.justjournal.search.BaseSearch" %>
<%@ page import="com.justjournal.utility.StringUtil" %>
<%@ page import="com.justjournal.utility.Xml" %>
<%@ page import="javax.sql.rowset.CachedRowSet" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.text.ParsePosition" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.ArrayList" %>
<%
    response.setHeader("Vary", "Accept"); // content negotiation
    response.setDateHeader("Expires", System.currentTimeMillis());
    response.setDateHeader("Last-Modified", System.currentTimeMillis());
    response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
    response.setHeader("Pragma", "no-cache");

    // date stuff
    java.text.SimpleDateFormat fmtdate = new java.text.SimpleDateFormat("yyyy-MM-dd");
    java.text.SimpleDateFormat fmttime = new java.text.SimpleDateFormat("HH:mm:ss");

    Integer userID = (Integer) session.getAttribute("auth.uid");
    int ival = 0;
    if (userID != null) {
        ival = userID.intValue();
    }

    int entryid = Integer.valueOf(request.getParameter("entryid")).intValue();
    EntryTo et = EntryDAO.viewSingle(entryid, ival);

    // Get the session user input
    String sbody = (String) session.getAttribute("spell.body");
    String smusic = (String) session.getAttribute("spell.music");
    String ssubject = (String) session.getAttribute("spell.subject");
    String stags = (String) session.getAttribute("spell.tags");
    String strackback = (String) session.getAttribute("spell.trackback");
    String spellcheck = (String) session.getAttribute("spell.check");

    if (spellcheck == null)
        spellcheck = "";

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

    Integer ssec = (Integer) session.getAttribute("spell.security");
    int issec = 0;
    if (ssec != null) {
        issec = ssec.intValue();
    }

    Integer sloc = (Integer) session.getAttribute("spell.location");
    int isloc = -1;
    if (sloc != null) {
        isloc = sloc.intValue();
    }

    Integer smood = (Integer) session.getAttribute("spell.mood");
    int ismood = -1;
    if (smood != null) {
        ismood = smood.intValue();
    }

    if (spellcheck.compareTo("true") != 0) {
        sbody = et.getBody();
        smusic = et.getMusic();
        ssubject = et.getSubject();
        issec = et.getSecurityLevel();
        isloc = et.getLocationId();
        ismood = et.getMoodId();

        Collection tags = et.getTags();
        if (tags != null) {
            int i = 0;
            Iterator it = tags.iterator();
            while (it.hasNext()) {
                String tagname = (String) it.next();
                if (i > 0)
                    stags = stags + "," + tagname;
                else
                    stags = tagname;
                i++;

            }
        }
    }

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <meta content="text/html; charset=UTF-8" http-equiv="Content-Type"/>
    <title>JustJournal.com: Update Journal</title>
    <link rel="stylesheet" type="text/css" href="layout.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="font-normal.css" media="all"/>
    <link rel="home" title="Home" href="index.jsp"/>
    <link rel="contents" title="Site Map" href="sitemap.jsp"/>
    <link rel="help" title="Technical Support" href="support/index.jsp"/>
    <script type="text/javascript" src="FCKeditor/fckeditor.js">//iesucks</script>
    <script type="text/javascript">
        window.onload = function()
        {
            var oFCKeditor = new FCKeditor('body');
            oFCKeditor.BasePath = '/FCKeditor/';
            oFCKeditor.Height = 300;
            oFCKeditor.Width = 572;
            oFCKeditor.Config["CustomConfigurationsPath"] = '/js/fckconfig.js';
            oFCKeditor.Config['SkinPath'] = '/FCKeditor/editor/skins/office2003/';
            oFCKeditor.Config['AutoDetectLanguage'] = true;
            oFCKeditor.ToolbarSet = 'JJ';
            oFCKeditor.ReplaceTextarea();
        }

        function FCKeditor_OnComplete(editorInstance)
        {
            document.getElementById("aformat").checked = false;
            document.getElementById("aformatrow").style.visibility = "hidden";
            document.getElementById("newlinetext").style.visibility = "hidden";
            document.getElementById("aformatrow").style.display = "none";
            document.getElementById("newlinetext").style.display = "none";
        }
    </script>
    <style type="text/css">
        @import "js/dijit/themes/soria/soria.css";
        @import "js/dojo/resources/dojo.css";
    </style>
    <script type="text/javascript" src="js/dojo/dojo.js" djConfig="parseOnLoad: true">//iesucks</script>
    <script type="text/javascript" src="js/dijit/dijit.js">//iesucks</script>
    <script type="text/javascript">
        dojo.require("dijit.form.FilteringSelect");
        dojo.require("dijit.form.TimeTextBox");
        dojo.require("dijit.form.DateTextBox");
        dojo.require("dijit.form.TextBox");
        dojo.require("dijit.form.ValidationTextBox");
        dojo.require("dojox.validate.regexp");
        dojo.require("dojo.parser");
    </script>
    <style type="text/css" media="all">
        div.row {
            clear: both;
            padding-top: 5px;
        }

        div.row span.label {
            float: left;
            width: 140px;
            text-align: right;
        }

        div.row span.formw {
            float: right;
            width: 400px;
            text-align: left;
        }

        div.spacer {
            clear: both;
        }

        fieldset {
            border: 2px solid #6FCADE;
            background: white;
            color: #333;
        }

        legend {
            color: navy;
            font-weight: bold;
        }
    </style>
</head>

<body>

<jsp:include page="header.inc" flush="false"/>

<div id="content">
<h2>Update Journal</h2>

<p>Post a journal entry.</p>

<div style="width: 575px; padding: 5px; margin: 0;">
<form method="post" action="EditEntry" name="frmEditEntry" id="frmEditEntry">

<%


    if (ival < 1) {
%>
<p>You must login first.</p>
<% } else { %>
<p>You are logged in as <a href="/users/<%= session.getAttribute("auth.user") %>"><img src="images/userclass_16.png"
                                                                                       alt="user"/><%= session.getAttribute("auth.user") %>
</a>.
    If you want to post to another journal, <a href="logout.jsp">log out</a> first.</p>
<% } %>

<fieldset>
    <legend><strong>Journal Entry</strong><br/></legend>

    <div class="row">
        <span class="label"><label for="date">Date</label></span>
	  <span class="formw"><input name="date" id="date" value="<%=fmtdate.format( et.getDate() )  %>" size="15"
                                 maxlength="10"
                                 dojoType="dijit.form.DateTextBox"/>
	  </span>
    </div>

    <div class="row">
        <span class="label"><label for="time">Time</label></span>
        <span class="formw"><input name="time" id="time" value="T<%=fmttime.format( et.getDate() ) %>" size="15"
                                   maxlength="8"
                                   dojoType="dijit.form.TimeTextBox"
                                   constraints="{timePattern:'HH:mm:ss'}"
                                   required="true"
                                   invalidMessage="Invalid time. Use HH:mm:ss where HH is 00 - 23 hours."/></span>
    </div>

    <div class="row">
        <span class="label"><label for="subject">Subject</label></span>
	  <span class="formw"><input name="subject" type="text" id="subject" size="30" maxlength="150"
                                 value="<%=ssubject%>"
                                 dojoType="dijit.form.TextBox"
                                 trim="true"/>
	  (optional)</span>
    </div>
    <% if (spellcheck.compareTo("true") == 0) { %>
    <div class="row">
        <strong>Your spell-checked post:</strong><br/>
        <%=sbody%>
    </div>

    <div class="row">
        <strong>Suggestions:</strong><br/>
        <%=(String) session.getAttribute("spell.suggest")%>
    </div>
    <%
        }
    %>

    <div class="row"><textarea id="body" name="body" style="width: 100%" cols="60"
                               rows="20"><%=sbody%>
    </textarea>
    </div>

    <div class="row" id="newlinetext">
        <span class="formw">by default, newlines will be auto-formatted to &lt;br&gt;</span>
    </div>

    <div class="row">
			<span class="formw"><input type="checkbox" name="spellcheck" id="spellcheck"
                                       value="checked"/>
			<label for="spellcheck">Spell check entry before posting</label>
			</span>
    </div>

    <!-- Hack to fix spacing problem.. especially with text boxes -->
    <div class="spacer">&nbsp;</div>

</fieldset>

<fieldset>
<legend><strong>Optional Settings</strong><br/></legend>

<div class="row">
    <span class="label"><label for="security">Security</label></span>
	  <span class="formw">
	  	<select id="security" name="security" size="1" dojoType="dijit.form.FilteringSelect" autoComplete="false"
                  invalidMessage="Invalid security setting">
              <%
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

<div class="row">
    <span class="label"><label for="location">Location</label></span>
	  <span class="formw">
	  	<select id="location" name="location" size="1" dojoType="dijit.form.FilteringSelect" autoComplete="false"
                  invalidMessage="Invalid location">
              <%
                  for (Iterator iterator = LocationDao.view().iterator(); iterator.hasNext();) {
                      LocationTo o = (LocationTo) iterator.next();
                      out.print("\t<option value=\"" + o.getId());

                      if (isloc > -1) {
                          if (o.getId() == isloc)
                              out.print("\" selected=\"selected\">");
                          else
                              out.print("\">");
                      } else {
                          if (o.getName().compareTo("Not Specified") == 0)
                              out.print("\" selected=\"selected\">");
                          else
                              out.print("\">");
                      }

                      out.println(o.getName() + "</option>");
                  }
              %>
          </select>
	  </span>
</div>

<div class="row">
    <span class="label"><label for="mood"><a href="moodlist.jsp" title="List of Moods">Mood</a></label></span>
	  <span class="formw">
	  	<select id="mood" name="mood" size="1" dojoType="dijit.form.FilteringSelect" autoComplete="false"
                  invalidMessage="Invalid mood">
              <%


                  for (Iterator iterator = MoodDao.view().iterator(); iterator.hasNext();) {
                      MoodTo o = (MoodTo) iterator.next();
                      out.print("\t<option value=\"" + o.getId());

                      if (ismood > -1) {
                          if (o.getId() == ismood)
                              out.print("\" selected=\"selected\">");
                          else
                              out.print("\">");
                      } else {
                          if (o.getName().compareTo("Not Specified") == 0)
                              out.print("\" selected=\"selected\">");
                          else
                              out.print("\">");
                      }

                      out.println(o.getName() + "</option>");
                  }
              %>
          </select>
	  </span>
</div>

<div class="row">
    <span class="label"><label for="tags">Tags</label></span>
    <span class="formw"><input type="text" name="tags" id="tags" size="30" value="<%=stags%>"
                               dojoType="dijit.form.TextBox"
                               trim="true" lowercase="true"/></span>
</div>

<div class="row">
    <span class="label"><label for="music">Music</label></span>
    <span class="formw"><input type="text" name="music" id="music" size="30" value="<%=smusic%>"
                               dojoType="dijit.form.TextBox"
                               trim="true" propercase="true"/></span>
</div>

<div class="row">
    <span class="label"><label for="trackback">Trackback</label></span>
    <span class="formw"><input type="text" name="trackback" id="trackback" size="30" value="<%=strackback%>"
                               dojoType="dijit.form.ValidationTextBox"
                               regExpGen="dojox.regexp.url"
                               trim="true"
                               required="false"
                               constraints={scheme:true}
                               invalidMessage="Invalid URL.  Be sure to include the scheme, http://..."/></span>
</div>

<div class="row">
			<span class="formw"><input type="checkbox" name="allow_comment" id="allow_comment"
                                       value="checked" checked="checked"/>
			<label for="allow_comment">Allow comments on this entry</label>
			</span>
</div>

<div class="row">
			<span class="formw"><input type="checkbox" name="email_comment" id="email_comment"
                                       value="checked" checked="checked"/>
			<label for="email_comment">Email comments to me</label>
			</span>
</div>

<div class="row" id="aformatrow">
			<span class="formw"><input type="checkbox" name="aformat" id="aformat"
                                       value="checked" checked="checked"/>
			<label for="aformat">Auto-formatting</label>
			</span>
</div>

<!-- Hack to fix spacing problem.. especially with text boxes -->
<div class="spacer">&nbsp;</div>
</fieldset>

<div class="row"><input type="submit" name="submit" value="submit"/></div>

</form>

</div>

</div>

<jsp:include page="footer.inc" flush="false"/>

</body>
</html>

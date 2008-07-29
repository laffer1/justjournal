<%@ page contentType="text/html; charset=iso-8859-1" language="java" %>
<%--
Form to add a journal entry.  Forwards the request to a servlet.

$Id: add.jsp,v 1.3 2008/07/29 11:59:45 laffer1 Exp $
--%>
<%
    int eid;

    try {
        eid = new Integer(request.getParameter("id")).intValue();
    } catch (NumberFormatException ex1) {
        eid = 0;
    }

    // Get the session user input
    String sbody = (String) session.getAttribute("spell.cbody");
    String ssubject = (String) session.getAttribute("spell.csubject");

    if (sbody == null)
        sbody = "";

    if (ssubject == null)
        ssubject = "";

%>
<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <title>JustJournal.com: Add Comment</title>
    <link rel="stylesheet" type="text/css" href="../layout.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="../font-normal.css" media="all"/>
    <link rel="home" title="Home" href="../index.jsp"/>
    <link rel="contents" title="Site Map" href="../sitemap.jsp"/>
    <link rel="help" title="Technical Support" href="../support/index.jsp"/>
    <style type="text/css" media="all">
        <!--

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
            width: 340px;
            text-align: left;
        }

        div.spacer {
            clear: both;
        }

        -->
    </style>
</head>

<body>

<jsp:include page="../header.inc" flush="false"/>

<div id="content">
    <h2>Add Comment</h2>

    <jsp:include page="../inc_login.jsp" flush="false"/>

    <div style="width: 505px; padding: 5px; margin: 0">
        <form method="post" action="../addComment" name="frmAddComment">
            <input type="hidden" name="id" value="<%=eid%>"/>
            <%
                Integer userID = (Integer) session.getAttribute("auth.uid");
                int ival = 0;
                if (userID != null) {
                    ival = userID.intValue();
                }

                if (ival < 1) {
            %>
            <fieldset>
                <legend><strong>Login Information</strong><br/></legend>

                <div class="row">
                    <span class="label"><label for="username">Username</label></span>
                    <span class="formw"><input type="text" name="username" id="username" size="25"/></span>
                </div>

                <div class="row">
                    <span class="label"><label for="password">Password</label></span>
			<span class="formw"><input type="password" name="password" id="password" size="25"/>
			</span>
                </div>

                <!-- Hack to fix spacing problem.. especially with text boxes -->
                <div class="spacer">&nbsp;</div>

            </fieldset>
            <% } %>

            <% if (eid > 0) { %>
            <fieldset>
                <legend><strong>Comment</strong><br/></legend>

                <div class="row">
                    <span class="label"><label for="subject">Subject</label></span>
	  <span class="formw"><input name="subject" type="text" id="subject" size="25" maxlength="150"
                                 value="<%=ssubject%>"/>
	  </span>
                </div>
                <%
                    String spellcheck = (String) session.getAttribute("spell.ccheck");

                    if (spellcheck == null)
                        spellcheck = "";

                    if (spellcheck.compareTo("true") == 0) {
                %>

                <div class="row">
                    <strong>Your spell-checked post:</strong><br/>
                    <%=sbody%>
                </div>

                <div class="row">
                    <strong>Suggestions:</strong><br/>
                    <%=(String) session.getAttribute("spell.csuggest")%>
                </div>
                <%
                    }
                %>
                <div class="row">
                    <span class="label"><label for="body">Body</label></span>
                    <span class="formw"><textarea id="body" name="body" style="width: 100%" wrap="soft" cols="50"
                                                  rows="20"><%=sbody%></textarea></span>
                </div>

                <div class="row">
			<span class="formw"><input type="checkbox" name="spellcheck" id="spellcheck"
                                       value="checked"/>
			<label for="spellcheck">Check Spelling</label>
			</span>
                </div>

                <!-- Hack to fix spacing problem.. especially with text boxes -->
                <div class="spacer">&nbsp;</div>

            </fieldset>

            <div class="row"><input type="submit" name="submit" value="submit"/></div>

            <!-- Hack to fix spacing problem.. especially with text boxes -->
            <div class="spacer">&nbsp;</div>
            <% } else { %>
            <p><strong>You must select a journal entry to comment on.</strong></p>
            <% } %>
        </form>

    </div>

</div>

<jsp:include page="../footer.inc" flush="false"/>

</body>
</html>
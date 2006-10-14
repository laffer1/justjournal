<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" indent="yes" encoding="utf-8"
                media-type="text/html" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"
                doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN" omit-xml-declaration="no"/>

    <xsl:variable name="currentLogin" select="myLogin"/>

    <!-- This identity transform allows XHTML (or other) tags to propagate -->
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

    <!-- The overall structure of an outside page -->
    <xsl:template match="/page">
        <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
                <title>
                    <xsl:value-of select="title"/>
                </title>
                <link rel="stylesheet" type="text/css" href="/layout.css" media="all"/>
                <link rel="stylesheet" type="text/css" href="/font-normal.css" media="all"/>
                <link rel="stylesheet" type="text/css" href="/chrometheme/chromestyle.css"/>
                <script type="text/javascript" src="/chromejs/chrome.js">/*menu script */</script>
                <link rel="home" title="Home" href="/"/>
                <link rel="contents" title="Site Map" href="/sitemap.jsp"/>
                <link rel="help" title="Technical Support" href="/support/"/>
                <xsl:apply-templates select="head/*"/>
            </head>
            <body>
                <!-- Header: Begin -->
                <div id="header">
                    <span id="title">
                        <img src="/images/userclass_32.png" alt="JJ user" id="titleicon"/>
                        <img src="/images/jjngtitle2.gif" alt="JustJournal.com: Blogging for life" id="titlelogo"/>
                    </span>
                    <div id="menuiconrow">
                        <a href="/login.jsp">
                            <img src="/images/icon_login.gif" alt="login" class="menuicons" id="menuiconfirst"/>
                        </a>
                        <a href="/update.jsp">
                            <img src="/images/icon_write.gif" alt="write" class="menuicons"/>
                        </a>
                        <a href="/search/index.jsp">
                            <img src="/images/icon_search.gif" alt="search" class="menuicons"/>
                        </a>
                    </div>
                    <br clear="all"/>

                    <div id="chromemenu" style="width: 100%">
                        <ul>
                            <li>
                                <a href="/index.jsp">Home</a>
                            </li>
                            <li>
                                <a href="#" onmouseover="cssdropdown.dropit(this,event,'dropmenu1')">Journal</a>
                            </li>
                            <li>
                                <a href="#" onmouseover="cssdropdown.dropit(this,event,'dropmenu2')">Account</a>
                            </li>
                            <li>
                                <a href="/support/index.jsp" onmouseover="cssdropdown.dropit(this,event,'dropmenu3')">
                                    Support</a>
                            </li>
                            <li>
                                <a href="/software/index.jsp" onmouseover="cssdropdown.dropit(this,event,'dropmenu4')">
                                    Software</a>
                            </li>
                            <li>
                                <a href="/privacy.jsp">Privacy</a>
                            </li>
                            <li>
                                <a href="/sitemap.jsp">Site Map</a>
                            </li>
                            <li>
                                <a href="http://www.cafepress.com/justjournal">Store</a>
                            </li>
                        </ul>
                    </div>

                    <!--1st drop down menu -->
                    <div id="dropmenu1" class="dropmenudiv">
                        <a href="/update.jsp">Create Entry</a>
                    </div>

                    <!--2nd drop down menu -->
                    <div id="dropmenu2" class="dropmenudiv">
                        <a href="/login.jsp">Login</a>
                        <a href="/create.jsp">Create</a>
                        <a href="/cancel.jsp">Cancel</a>
                        <a href="/memberlist.jsp">Member List</a>
                    </div>

                    <!--3rd anchor link and menu -->
                    <div id="dropmenu3" class="dropmenudiv">
                        <a href="/support/">Support Home</a>
                        <a href="/users/jjsite">Site Journal</a>
                        <a href="/moodlist.jsp">Mood List</a>
                    </div>

                    <!--4th anchor link and menu -->
                    <div id="dropmenu4" class="dropmenudiv">
                        <a href="/software/index.jsp">Software Home</a>
                        <a href="/software/windows.jsp">Windows Client</a>
                        <a href="/opensource/index.jsp">Open Source</a>
                        <a href="http://sourceforge.net/projects/justjournal/">Source Forge CVS</a>
                    </div>
                </div>
                <br style="clear: both;"/>
                <!-- Header: End -->

                <div id="content">
                    <xsl:apply-templates select="content/*"/>
                </div>

                <!-- Footer: Begin -->
                <div id="footer">
                    <p id="copyright">
                        <xsl:text disable-output-escaping="yes">&amp;copy;</xsl:text>
                        2003-2006 Lucas Holt. All rights reserved.
                    </p>
                </div>
                <!-- Footer: End -->

            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>

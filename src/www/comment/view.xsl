<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml" indent="yes" encoding="UTF-8"/>

    <xsl:template match="/model">
        <xsl:variable name="currentLogin" select="myLogin"/>

        <page>
            <title>
                <xsl:value-of select="entry/userName"/>
                :
                <xsl:value-of select="entry/subject"/>
            </title>

            <content>
                <xsl:for-each select="entry">
                    <p>
                        <a>
                            <xsl:attribute name="href">/users/
                                <xsl:value-of select="userName"/>
                            </xsl:attribute>
                            <xsl:attribute name="title">
                                <xsl:value-of select="userName"/>
                            </xsl:attribute>
                            <img src="/images/userclass_16.png" alt="user"/>
                        </a>

                        <a>
                            <xsl:attribute name="href">/users/
                                <xsl:value-of select="userName"/>
                            </xsl:attribute>
                            <xsl:attribute name="title">
                                <xsl:value-of select="userName"/>
                            </xsl:attribute>
                            <xsl:value-of select="userName"/>
                        </a>

                        wrote @
                        <xsl:value-of select="date/year"/>
                        -
                        <xsl:if test="date/month &lt; 10">0</xsl:if>
                        <xsl:value-of select="date/month"/>
                        -
                        <xsl:if test="date/day &lt; 10">0</xsl:if>
                        <xsl:value-of select="date/day"/>
                        <xsl:text></xsl:text>
                        <xsl:if test="date/hour &lt; 10">0</xsl:if>
                        <xsl:value-of select="date/hour"/>
                        :
                        <xsl:if test="date/minutes &lt; 10">0</xsl:if>
                        <xsl:value-of select="date/minutes"/>
                    </p>

                    <h3>
                        <xsl:value-of select="subject"/>
                    </h3>
                    <p>
                        <xsl:call-template name="add-line-breaks">
                            <xsl:with-param name="string" select="body"/>
                        </xsl:call-template>
                    </p>

                    <div style="width: 100%; background: silver">
                        <xsl:value-of select="commentCount"/>
                        comments
                    </div>
                    <div class="rightflt">
                        <a>
                            <xsl:attribute name="href">add.jsp?id=
                                <xsl:value-of select="id"/>
                            </xsl:attribute>
                            <xsl:attribute name="title">add comment</xsl:attribute>
                            add comment
                        </a>
                    </div>
                </xsl:for-each>

                <xsl:for-each select="comments/item">

                    <div class="comment">

                        <div class="chead">
                            <h3>
                                <span class="subject">
                                    <xsl:value-of select="subject"/>
                                </span>
                            </h3>

                            <a>
                                <xsl:attribute name="href">/users/
                                    <xsl:value-of select="userName"/>
                                </xsl:attribute>
                                <xsl:attribute name="title">
                                    <xsl:value-of select="userName"/>
                                </xsl:attribute>
                                <img src="/images/userclass_16.png" alt="user"/>
                            </a>

                            <a>
                                <xsl:attribute name="href">/users/
                                    <xsl:value-of select="userName"/>
                                </xsl:attribute>
                                <xsl:attribute name="title">
                                    <xsl:value-of select="userName"/>
                                </xsl:attribute>
                                <xsl:value-of select="userName"/>
                            </a>

                            <br/>
                            <span class="time">
                                <xsl:value-of select="date/year"/>
                                -
                                <xsl:if test="date/month &lt; 10">0</xsl:if>
                                <xsl:value-of select="date/month"/>
                                -
                                <xsl:if test="date/day &lt; 10">0</xsl:if>
                                <xsl:value-of select="date/day"/>
                                <xsl:text></xsl:text>
                                <xsl:if test="date/hour &lt; 10">0</xsl:if>
                                <xsl:value-of select="date/hour"/>
                                :
                                <xsl:if test="date/minutes &lt; 10">0</xsl:if>
                                <xsl:value-of select="date/minutes"/>
                            </span>
                            <xsl:if test="$currentLogin = userName">
                                <br/>
                                <span class="actions">
                                    <a>
                                        <xsl:attribute name="href">edit.h?commentId=
                                            <xsl:value-of select="id"/>
                                        </xsl:attribute>
                                        <xsl:attribute name="title">Edit Comment</xsl:attribute>
                                        <img src="/images/compose-message.png" width="24" height="24"/>
                                    </a>

                                    <a>
                                        <xsl:attribute name="href">delete.h?commentId=
                                            <xsl:value-of select="id"/>
                                        </xsl:attribute>
                                        <xsl:attribute name="title">Delete Comment</xsl:attribute>
                                        <img src="/images/stock_calc-cancel.png" width="24" height="24"/>
                                    </a>
                                </span>
                            </xsl:if>
                        </div>

                        <p>
                            <xsl:call-template name="add-line-breaks">
                                <xsl:with-param name="string" select="body"/>
                            </xsl:call-template>
                        </p>
                    </div>

                </xsl:for-each>

            </content>
        </page>
    </xsl:template>

    <xsl:template name="add-line-breaks">
        <xsl:param name="string" select="."/>
        <xsl:choose>
            <xsl:when test="contains($string, '&#xA;')">
                <xsl:value-of select="substring-before($string, '&#xA;')"/>
                <br/>
                <xsl:call-template name="add-line-breaks">
                    <xsl:with-param name="string"
                                    select="substring-after($string, '&#xA;')"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$string"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>
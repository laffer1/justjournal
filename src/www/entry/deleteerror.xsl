<?xml version="1.0"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml" indent="yes"/>

    <xsl:template match="/model">
        <xsl:variable name="currentLogin" select="myLogin"/>

        <page>
            <title>JustJournal.com: Delete Entry: Error</title>

            <content>
                <h2>Edit Entry</h2>

                <p>You are logged in as
                    <a>
                        <xsl:attribute name="href">/users/
                            <xsl:value-of select="$currentLogin"/>
                        </xsl:attribute>
                        <xsl:attribute name="title">
                            <xsl:value-of select="$currentLogin"/>
                        </xsl:attribute>
                        <img src="/images/userclass_16.png" alt="user"/>
                    </a>

                    <a>
                        <xsl:attribute name="href">/users/
                            <xsl:value-of select="$currentLogin"/>
                        </xsl:attribute>
                        <xsl:attribute name="title">
                            <xsl:value-of select="$currentLogin"/>
                        </xsl:attribute>
                        <xsl:value-of select="$currentLogin"/>
                    </a>
                    . If you want to post to another journal,
                    <a href="/logout.jsp" title="log out">log out</a>
                    first.
                </p>

                <p>An error occured during the delete entry process.</p>
                <p>
                    <xsl:value-of select="error"/>
                </p>

            </content>
        </page>
    </xsl:template>

</xsl:stylesheet>
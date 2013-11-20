<?xml version="1.0"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml" indent="yes"/>

    <xsl:template match="/model">
        <xsl:variable name="currentLogin" select="myLogin"/>
        <page>
            <title>JustJournal.com: Add Favorite</title>

            <content>
                <h2>Favorite Added</h2>
                <p>
                    <a href="view.h" title="View Favorites">View Favorites</a>
                </p>
            </content>
        </page>
    </xsl:template>

</xsl:stylesheet>
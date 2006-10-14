<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml" indent="yes" encoding="UTF-8"/>

    <xsl:template match="/model">
        <xsl:variable name="currentLogin" select="myLogin"/>

        <page>
            <title>Links</title>

            <content>
                <h2>Links</h2>

                <p>View and delete your links below. You may also
                    <a href="add.h" title="Add Links">add</a>
                    a link.
                </p>

                <table>
                    <thead>
                        <tr>
                            <th>Link</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <xsl:for-each select="links/item">
                            <tr>
                                <td>
                                    <a>
                                        <xsl:attribute name="href">
                                            <xsl:value-of select="uri"/>
                                        </xsl:attribute>
                                        <xsl:attribute name="title">
                                            <xsl:value-of select="title"/>
                                        </xsl:attribute>
                                        <xsl:value-of select="title"/>
                                    </a>
                                </td>
                                <td>
                                    <a>
                                        <xsl:attribute name="href">delete.h?linkTitle=
                                            <xsl:value-of select="title"/>
                                            &amp;linkUri=
                                            <xsl:value-of select="uri"/>
                                        </xsl:attribute>
                                        <xsl:attribute name="title">Delete Link</xsl:attribute>
                                        delete
                                    </a>
                                </td>
                            </tr>
                        </xsl:for-each>
                    </tbody>
                </table>

            </content>
        </page>
    </xsl:template>

</xsl:stylesheet>

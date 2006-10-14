<?xml version="1.0"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml" indent="yes"/>

    <xsl:template match="/model">
        <xsl:variable name="currentLogin" select="myLogin"/>

        <page>
            <title>JustJournal.com: Edit Comment</title>

            <content>
                <h2>Edit Comment</h2>

                <div style="width: 500px; padding: 5px; margin: 0px;">
                    <form method="post" action="editsubmit.h" name="frmEditComment">
                        <input>
                            <xsl:attribute name="name">commentId</xsl:attribute>
                            <xsl:attribute name="type">hidden</xsl:attribute>
                            <xsl:attribute name="value">
                                <xsl:value-of select="comment/id"/>
                            </xsl:attribute>
                        </input>

                        <input>
                            <xsl:attribute name="name">userId</xsl:attribute>
                            <xsl:attribute name="type">hidden</xsl:attribute>
                            <xsl:attribute name="value">
                                <xsl:value-of select="comment/userId"/>
                            </xsl:attribute>
                        </input>

                        <input>
                            <xsl:attribute name="name">eid</xsl:attribute>
                            <xsl:attribute name="type">hidden</xsl:attribute>
                            <xsl:attribute name="value">
                                <xsl:value-of select="comment/eid"/>
                            </xsl:attribute>
                        </input>

                        <input>
                            <xsl:attribute name="name">date</xsl:attribute>
                            <xsl:attribute name="type">hidden</xsl:attribute>
                            <xsl:attribute name="value">
                                <xsl:value-of select="comment/date"/>
                            </xsl:attribute>
                        </input>

                        <fieldset>
                            <legend>
                                <strong>Comment</strong>
                                <br/>
                            </legend>

                            <div class="row">
                                <span class="label">
                                    <label for="subject">Subject</label>
                                </span>
                                <span class="formw">
                                    <input>
                                        <xsl:attribute name="name">subject</xsl:attribute>
                                        <xsl:attribute name="type">text</xsl:attribute>
                                        <xsl:attribute name="id">subject</xsl:attribute>
                                        <xsl:attribute name="size">25</xsl:attribute>
                                        <xsl:attribute name="maxlength">150</xsl:attribute>
                                        <xsl:attribute name="value">
                                            <xsl:value-of select="comment/subject"/>
                                        </xsl:attribute>
                                    </input>
                                </span>
                            </div>

                            <div class="row">
                                <span class="label">
                                    <label for="body">Body</label>
                                </span>
                                <span class="formw">
                                    <textarea>
                                        <xsl:attribute name="name">body</xsl:attribute>
                                        <xsl:attribute name="id">body</xsl:attribute>
                                        <xsl:attribute name="style">width: 100%</xsl:attribute>
                                        <xsl:attribute name="wrap">soft</xsl:attribute>
                                        <xsl:attribute name="cols">50</xsl:attribute>
                                        <xsl:attribute name="rows">20</xsl:attribute>
                                        <xsl:value-of select="comment/body"/>
                                    </textarea>
                                </span>
                            </div>

                            <div class="row">
                                <span class="formw">
                                    <input type="checkbox" name="spellcheck" id="spellcheck"
                                           value="checked"/>
                                    <label for="spellcheck">Spell check comment before posting</label>
                                </span>
                            </div>

                            <!-- Hack to fix spacing problem.. especially with text boxes -->
                            <div class="spacer">
                                <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
                            </div>
                        </fieldset>

                        <div class="row">
                            <input type="submit" name="submit" value="submit"/>
                        </div>

                    </form>
                </div>
            </content>
        </page>
    </xsl:template>

</xsl:stylesheet>
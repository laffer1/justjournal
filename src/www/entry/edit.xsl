<?xml version="1.0"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml" indent="yes"/>

    <xsl:template match="/model">
        <xsl:variable name="currentLogin" select="myLogin"/>
        <xsl:variable name="currentMood" select="entry/moodId"/>
        <xsl:variable name="currentSecurity" select="entry/securityLevel"/>
        <xsl:variable name="currentLocation" select="entry/locationId"/>

        <page>
            <title>JustJournal.com: Edit Entry</title>

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
                        <img src="/images/user_class16.png" alt="user"/>
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

                <div style="width: 500px; padding: 5px; margin: 0px;">
                    <form method="post" action="editsubmit.h" name="frmEditEntry">
                        <input>
                            <xsl:attribute name="name">entryId</xsl:attribute>
                            <xsl:attribute name="type">hidden</xsl:attribute>
                            <xsl:attribute name="value">
                                <xsl:value-of select="entry/id"/>
                            </xsl:attribute>
                        </input>

                        <input>
                            <xsl:attribute name="name">userId</xsl:attribute>
                            <xsl:attribute name="type">hidden</xsl:attribute>
                            <xsl:attribute name="value">
                                <xsl:value-of select="entry/userId"/>
                            </xsl:attribute>
                        </input>

                        <fieldset>
                            <legend>
                                <strong>Journal Entry</strong>
                                <br/>
                            </legend>

                            <div class="row">
                                <span class="label">Date</span>
                                <span class="formw">
                                    <input>
                                        <xsl:attribute name="name">year</xsl:attribute>
                                        <xsl:attribute name="type">text</xsl:attribute>
                                        <xsl:attribute name="id">year</xsl:attribute>
                                        <xsl:attribute name="size">4</xsl:attribute>
                                        <xsl:attribute name="maxlength">4</xsl:attribute>
                                        <xsl:attribute name="value">
                                            <xsl:value-of select="entry/date/year"/>
                                        </xsl:attribute>
                                    </input>
                                    /
                                    <input>
                                        <xsl:attribute name="name">month</xsl:attribute>
                                        <xsl:attribute name="type">text</xsl:attribute>
                                        <xsl:attribute name="id">month</xsl:attribute>
                                        <xsl:attribute name="size">2</xsl:attribute>
                                        <xsl:attribute name="maxlength">2</xsl:attribute>
                                        <xsl:attribute name="value">
                                            <xsl:if test="entry/date/month &lt; 10">0</xsl:if>
                                            <xsl:value-of select="entry/date/month"/>
                                        </xsl:attribute>
                                    </input>
                                    /
                                    <input>
                                        <xsl:attribute name="name">day</xsl:attribute>
                                        <xsl:attribute name="type">text</xsl:attribute>
                                        <xsl:attribute name="id">day</xsl:attribute>
                                        <xsl:attribute name="size">2</xsl:attribute>
                                        <xsl:attribute name="maxlength">2</xsl:attribute>
                                        <xsl:attribute name="value">
                                            <xsl:if test="entry/date/day &lt; 10">0</xsl:if>
                                            <xsl:value-of select="entry/date/day"/>
                                        </xsl:attribute>
                                    </input>
                                    (YYYY/MM/DD)
                                </span>
                            </div>

                            <div class="row">
                                <span class="label">Time</span>
                                <span class="formw">
                                    <input>
                                        <xsl:attribute name="name">hour</xsl:attribute>
                                        <xsl:attribute name="type">text</xsl:attribute>
                                        <xsl:attribute name="id">hour</xsl:attribute>
                                        <xsl:attribute name="size">2</xsl:attribute>
                                        <xsl:attribute name="maxlength">2</xsl:attribute>
                                        <xsl:attribute name="value">
                                            <xsl:if test="entry/date/hour &lt; 10">0</xsl:if>
                                            <xsl:value-of select="entry/date/hour"/>
                                        </xsl:attribute>
                                    </input>
                                    :
                                    <input>
                                        <xsl:attribute name="name">minute</xsl:attribute>
                                        <xsl:attribute name="type">text</xsl:attribute>
                                        <xsl:attribute name="id">minute</xsl:attribute>
                                        <xsl:attribute name="size">2</xsl:attribute>
                                        <xsl:attribute name="maxlength">2</xsl:attribute>
                                        <xsl:attribute name="value">
                                            <xsl:if test="entry/date/minutes &lt; 10">0</xsl:if>
                                            <xsl:value-of select="entry/date/minutes"/>
                                        </xsl:attribute>
                                    </input>

                                    (HH:MM)
                                </span>
                            </div>

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
                                            <xsl:value-of select="entry/subject"/>
                                        </xsl:attribute>
                                    </input>
                                    (optional)
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
                                        <xsl:value-of select="entry/body"/>
                                    </textarea>
                                </span>
                            </div>

                            <div class="row">
                                <span class="formw">by default, newlines will be auto-formatted to &lt;br&gt;</span>
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

                        <fieldset>
                            <legend>
                                <strong>Optional Settings</strong>
                                <br/>
                            </legend>
                            <div class="row">
                                <span class="label">
                                    <label for="security">Security</label>
                                </span>
                                <span class="formw">
                                    <select id="security" name="security" size="1">
                                        <xsl:for-each select="security/item">
                                            <option>
                                                <xsl:attribute name="value">
                                                    <xsl:value-of select="id"/>
                                                </xsl:attribute>
                                                <xsl:if test="$currentSecurity = id">
                                                    <xsl:attribute name="selected">selected</xsl:attribute>
                                                </xsl:if>
                                                <xsl:value-of select="name"/>
                                            </option>
                                        </xsl:for-each>
                                    </select>
                                </span>
                            </div>

                            <div class="row">
                                <span class="label">
                                    <label for="location">Location</label>
                                </span>
                                <span class="formw">
                                    <select id="location" name="location" size="1">
                                        <xsl:for-each select="location/item">
                                            <option>
                                                <xsl:attribute name="value">
                                                    <xsl:value-of select="id"/>
                                                </xsl:attribute>
                                                <xsl:if test="$currentLocation = id">
                                                    <xsl:attribute name="selected">selected</xsl:attribute>
                                                </xsl:if>
                                                <xsl:value-of select="name"/>
                                            </option>
                                        </xsl:for-each>
                                    </select>
                                </span>
                            </div>

                            <div class="row">
                                <span class="label">
                                    <label for="mood">
                                        <a href="/moodlist.jsp" title="List of Moods">Mood</a>
                                    </label>
                                </span>
                                <span class="formw">
                                    <select id="mood" name="mood" size="1">
                                        <xsl:for-each select="moods/item">
                                            <option>
                                                <xsl:attribute name="value">
                                                    <xsl:value-of select="id"/>
                                                </xsl:attribute>
                                                <xsl:if test="$currentMood = id">
                                                    <xsl:attribute name="selected">selected</xsl:attribute>
                                                </xsl:if>
                                                <xsl:value-of select="name"/>
                                            </option>
                                        </xsl:for-each>
                                    </select>
                                </span>
                            </div>

                            <div class="row">
                                <span class="label">
                                    <label for="music">Music</label>
                                </span>
                                <span class="formw">
                                    <input>
                                        <xsl:attribute name="name">music</xsl:attribute>
                                        <xsl:attribute name="type">text</xsl:attribute>
                                        <xsl:attribute name="id">music</xsl:attribute>
                                        <xsl:attribute name="size">30</xsl:attribute>
                                        <xsl:attribute name="maxlength">125</xsl:attribute>
                                        <xsl:attribute name="value">
                                            <xsl:value-of select="entry/music"/>
                                        </xsl:attribute>
                                    </input>
                                </span>
                            </div>

                            <div class="row">
                                <span class="formw">
                                    <input type="checkbox" name="discomments" id="discomments"
                                           value="checked"/>
                                    <label for="discomments">Disallow comments (not working)</label>
                                </span>
                            </div>
                            <div class="row">
                                <span class="formw">
                                    <input type="checkbox" name="noemail" id="noemail"
                                           value="checked"/>
                                    <label for="noemail">Don't email comments (not working)</label>
                                </span>
                            </div>

                            <div class="row">
                                <span class="formw">
                                    <input type="checkbox" name="aformat" id="aformat"
                                           value="checked"/>
                                    <label for="aformat">Disable auto-formatting (not working)</label>
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

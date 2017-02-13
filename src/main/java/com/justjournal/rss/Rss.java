/*-
 * Copyright (c) 2003-2011 Lucas Holt
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */

package com.justjournal.rss;

import com.justjournal.model.DateTimeBean;
import com.justjournal.model.Entry;
import com.justjournal.utility.DateConvert;
import com.justjournal.utility.HTMLUtil;
import com.justjournal.utility.Xml;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Create an RSS feed as a string.  This should be a valid XML document. Implements RSS 2
 *
 * @author Lucas Holt
 * @version $Id: Rss.java,v 1.13 2011/05/29 22:32:59 laffer1 Exp $
 */
@Slf4j
@Component
public final class Rss {

    private static final int MAX_LENGTH = 15; // max size of RSS content
    private static final String USER_BASE_URL = "http://www.justjournal.com/users/";
    private static final String ALBUM_IMAGE_URL = "http://www.justjournal.com/AlbumImage?id=";

    private String title = "";
    private String link = "";
    private String description = "";
    private String language = "";
    private String copyright = "";
    private String webMaster = "";
    private String managingEditor = "";
    private String selfLink = "";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Date newestEntryDate = new Date();

    private List<RssItem> items = new ArrayList<RssItem>(MAX_LENGTH);

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getManagingEditor() {
        return managingEditor;
    }

    public void setManagingEditor(String managingEditor) {
        this.managingEditor = managingEditor;
    }

    public String getWebMaster() {
        return webMaster;
    }

    public void setWebMaster(String webMaster) {
        this.webMaster = webMaster;
    }

    public String getSelfLink() {
        return this.selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    public Date getNewestEntryDate() {
        return newestEntryDate;
    }

    // Methods

    public void populate(final Collection<Entry> entries) {

        RssItem item;

        // TODO: this sucks... need to make this reusable
        try {

            Entry o;
            final Iterator<Entry> itr = entries.iterator();

            for (int x = 0, n = entries.size(); x < n && x < MAX_LENGTH; x++) {
                o = itr.next();
                item = new RssItem();
                item.setTruncateFields(false);
                item.setTitle(o.getSubject());
                item.setLink(USER_BASE_URL + o.getUser().getUsername());
                // RSS feeds don't like &apos; and friends.  try to go unicode
                final String descUnicode = HTMLUtil.clean(o.getBody(), false);
                item.setDescription(HTMLUtil.convertCharacterEntities(descUnicode));
                item.setGuid(USER_BASE_URL + o.getUser().getUsername() + "/entry/" + o.getId());
                item.setPubDate(new DateTimeBean(o.getDate()).toPubDate());

                final Date date = o.getDate();
                if (newestEntryDate == null || date.compareTo(newestEntryDate) > 0)
                    newestEntryDate = date;
                add(item);
            }

        } catch (final Exception e) {
            log.error("Could not populate blog entries", e);
        }
    }

    public void populateImageList(final int userid, final String userName) {
        assert jdbcTemplate != null;

        RssItem item;
        String imageTitle;
        final String sqlStmt = "SELECT id, title, modified, mimetype, BIT_LENGTH(image) As imglen FROM user_images WHERE owner='" + userid + "' ORDER BY id DESC;";

        try {
            final List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlStmt);

            if (list.isEmpty())
                log.debug("No images loaded from database for Rss Image List");

            for (final Map<String, Object> rs : list) {
                final DateTimeBean dt = new DateTimeBean();

                imageTitle = rs.get("title").toString();
                item = new RssItem();
                item.setTruncateFields(false);
                item.setTitle(imageTitle);
                item.setLink(USER_BASE_URL + userName + "/pictures");
                item.setDescription("");
                item.setGuid(ALBUM_IMAGE_URL + rs.get("id"));
                item.setEnclosureURL(ALBUM_IMAGE_URL + rs.get("id"));
                item.setEnclosureType(rs.get("mimetype").toString().trim());
                item.setEnclosureLength(Integer.toString(Integer.parseInt(rs.get("imglen").toString()) / 8));

                dt.set(rs.get("modified").toString());
                item.setPubDate(dt.toPubDate());

                add(item);
            }
        } catch (final Exception e1) {
            log.error("Error populating image from list", e1);
        }
    }

    /**
     * Add RSS item
     *
     * @param item rss item
     */
    public void add(final RssItem item) {
        items.add(item);
    }

    public String toXml() {

        final StringBuilder sb = new StringBuilder();

        sb.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");

        sb.append("<rss version=\"2.0\" xmlns:atom=\"http://www.w3.org/2005/Atom\">\n");
        sb.append("\t<channel>\n");

        sb.append("\t\t<title>");
        sb.append(Xml.cleanString(title));
        sb.append("</title>\n");

        sb.append("\t\t<link>");
        sb.append(link);
        sb.append("</link>\n");

        sb.append("\t\t<description>");
        sb.append(Xml.cleanString(description));
        sb.append("</description>\n");

        sb.append("\t\t<language>");
        sb.append(language);
        sb.append("</language>\n");

        sb.append("\t\t<webMaster>");
        sb.append(webMaster);
        sb.append("</webMaster>\n");

        sb.append("\t\t<managingEditor>");
        sb.append(managingEditor);
        sb.append("</managingEditor>\n");

        sb.append("\t\t<copyright>");
        sb.append(copyright);
        sb.append("</copyright>\n");

        sb.append("\t\t<generator>JustJournal v2.2</generator>\n");
        sb.append("\t\t<docs>http://blogs.law.harvard.edu/tech/rss</docs>\n");
        sb.append("\t\t<ttl>360</ttl>\n\n");

        sb.append("\t\t<image>\n");

        sb.append("\t\t\t<url>");
        sb.append("http://www.justjournal.com/images/jj_icon_flower.png");
        sb.append("</url>\n");

        sb.append("\t\t\t<title>");
        sb.append(Xml.cleanString(title));
        sb.append("</title>\n");

        sb.append("\t\t\t<link>");
        sb.append(link);
        sb.append("</link>\n");

        // max width 144, default if not here is 88
        sb.append("\t\t\t<width>");
        sb.append(64);
        sb.append("</width>\n");

        // max height is 400, default is 31
        sb.append("\t\t\t<height>");
        sb.append(64);
        sb.append("</height>\n");

        sb.append("\t\t</image>\n");

        // last build date
        // Sat, 07 Sep 2002 09:43:33 GMT
        // someday get this format right
        sb.append("\t\t<lastBuildDate>");
        sb.append(DateConvert.encode822());
        sb.append("</lastBuildDate>\n");

        /* Iterator */
        RssItem o;
        final Iterator<RssItem> itr = items.listIterator();
        for (int i = 0, n = items.size(); i < n && i < MAX_LENGTH; i++)     // 15 is the limit for RSS
        {
            o = itr.next();

            sb.append("\t\t<item>\n");

            sb.append("\t\t\t<title>");
            sb.append(Xml.cleanString(o.getTitle()));
            sb.append("</title>\n");

            sb.append("\t\t\t<link>");
            sb.append(o.getLink());
            sb.append("</link>\n");

            sb.append("\t\t\t<description>");
            sb.append(Xml.cleanString(o.getDescription()));
            sb.append("...</description>\n");

            sb.append("\t\t\t<guid isPermaLink=\"true\">");
            sb.append(o.getGuid());
            sb.append("</guid>\n");

            sb.append("\t\t\t<pubDate>");
            sb.append(o.getPubDate());
            sb.append("</pubDate>\n");

            /* file attachment rss 2 feature
            <enclosure url="http://www.scripting.com/mp3s/touchOfGrey.mp3"
            length="5588242" type="audio/mpeg"/>
            */
            if (o.getEnclosureURL() != null && !o.getEnclosureURL().isEmpty()) {
                sb.append("\t\t\t<enclosure url=\"").append(o.getEnclosureURL());
                sb.append("\" length=\"").append(o.getEnclosureLength());
                sb.append("\" type=\"");
                sb.append(o.getEnclosureType());
                sb.append("\" />\n");
            }

            sb.append("\t\t</item>\n");
        }
        if (selfLink != null && selfLink.length() > 0)
            sb.append("<atom:link href=\"").append(selfLink).append("\" rel=\"self\" type=\"application/rss+xml\" />");
        sb.append("\t</channel>\n");
        sb.append("</rss>\n");

        return sb.toString();
    }

    /**
     * Number of RSS items populated.
     *
     * @return rss item count
     */
    public int size() {
        if (items == null)
            return 0;

        return items.size();
    }
}

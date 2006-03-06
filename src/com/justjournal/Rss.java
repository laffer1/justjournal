/*
Copyright (c) 2005, Lucas Holt
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are
permitted provided that the following conditions are met:

  Redistributions of source code must retain the above copyright notice, this list of
  conditions and the following disclaimer.

  Redistributions in binary form must reproduce the above copyright notice, this
  list of conditions and the following disclaimer in the documentation and/or other
  materials provided with the distribution.

  Neither the name of the Just Journal nor the names of its contributors
  may be used to endorse or promote products derived from this software without
  specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
*/

package com.justjournal;

import com.justjournal.db.EntryTo;
import com.justjournal.utility.Xml;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Lucas Holt
 * @version 1.1
 * @since 1.0
 *        User: laffer1
 *        Date: Aug 27, 2003
 *        Time: 11:54:38 PM
 */
public final class Rss {
    private final static int MAX_LENGTH = 15; // max size of RSS content

    private String title;
    private String link;
    private String description;
    private String language;
    private String copyright;
    private String webMaster;
    private String managingEditor;

    private ArrayList items = new ArrayList(MAX_LENGTH);


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

    // Methods

    public void populate(Collection entries) {

        RssItem item;

        // TODO: this sucks... need to make this reusable
        try {

            EntryTo o;
            Iterator itr = entries.iterator();

            for (int x = 0, n = entries.size(); x < n && x < MAX_LENGTH; x++) {
                o = (EntryTo) itr.next();
                item = new RssItem();
                item.setTitle(o.getSubject());
                item.setLink("http://www.justjournal.com/users/" + o.getUserName());
                item.setDescription(o.getBody());
                item.setGuid("http://www.justjournal.com/users/" + o.getUserName() + "/entry/" + o.getId());
                item.setPubDate(o.getDate().toPubDate());
                Add(item);
            }

        } catch (Exception e) {

        }
    }

    public void Add(RssItem item) {
        items.add(item);
    }

    public String toXml() {

        StringBuffer sb = new StringBuffer();

        sb.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");

        sb.append("<rss version=\"2.0\">\n");
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

        sb.append("\t\t<generator>JustJournal v1.0</generator>\n");
        sb.append("\t\t<docs>http://blogs.law.harvard.edu/tech/rss</docs>\n");
        sb.append("\t\t<ttl>360</ttl>\n\n");

        sb.append("\t\t<image>\n");

        sb.append("\t\t\t<url>");
        sb.append("http://www.justjournal.com/images/userclass_32.png");
        sb.append("</url>\n");

        sb.append("\t\t\t<title>");
        sb.append(Xml.cleanString(title));
        sb.append("</title>\n");

        sb.append("\t\t\t<link>");
        sb.append(link);
        sb.append("</link>\n");

        // max width 144, default if not here is 88
        sb.append("\t\t\t<width>");
        sb.append(32);
        sb.append("</width>\n");

        // max height is 400, default is 31
        sb.append("\t\t\t<height>");
        sb.append(32);
        sb.append("</height>\n");

        sb.append("\t\t</image>\n");

        // last build date
        // Sat, 07 Sep 2002 09:43:33 GMT
        // someday get this format right
        sb.append("\t\t<lastBuildDate>");
        sb.append(date());
        sb.append("</lastBuildDate>\n");

        /* Iterator */
        RssItem o;
        Iterator itr = items.listIterator();
        for (int i = 0, n = items.size(); i < n && i < MAX_LENGTH; i++)     // 15 is the limit for RSS
        {
            o = (RssItem) itr.next();

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

            sb.append("\t\t</item>\n");
        }

        sb.append("\t</channel>\n");
        sb.append("</rss>\n");

        return sb.toString();
    }

    private String date() {
        //Sat, 07 Sep 2002 09:43:33 GMT
        Calendar cal = new GregorianCalendar(java.util.TimeZone.getDefault());
        java.util.Date current = cal.getTime();
        final SimpleDateFormat formatmydate = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zz");

        return formatmydate.format(current);
    }

    public void recycle() {
        title = "";
        link = "";
        description = "";
        language = "en-us";
        copyright = "";
        webMaster = "";
        managingEditor = "";

        items.clear();
    }

}

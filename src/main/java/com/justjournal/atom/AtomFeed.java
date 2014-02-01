package com.justjournal.atom;

import com.justjournal.db.EntryTo;
import com.justjournal.utility.DateConvert;
import com.justjournal.utility.Xml;

import java.util.*;

/**
 * Create an atom feed.
 *
 * @author Lucas Holt
 * @version $Id: AtomFeed.java,v 1.5 2011/05/29 22:32:59 laffer1 Exp $
 */
public final class AtomFeed {

    /*
    <feed xmlns="http://www.w3.org/2005/Atom"
      xml:lang="en"
      xml:base="http://www.example.org">
  <id>http://www.example.org/myfeed</id>
  <title>My Simple Feed</title>
  <updated>2005-07-15T12:00:00Z</updated>
  <link href="/blog" />
  <link rel="self" href="/myfeed" />
  <entry>
    <id>http://www.example.org/entries/1</id>
    <title>A simple blog entry</title>
    <link href="/blog/2005/07/1" />
    <updated>2005-07-15T12:00:00Z</updated>
    <summary>This is a simple blog entry</summary>
  </entry>
  <entry>
    <id>http://www.example.org/entries/2</id>
    <title />
    <link href="/blog/2005/07/2" />
    <updated>2005-07-15T12:00:00Z</updated>
    <summary>This is simple blog entry without a title</summary>
  </entry>
    </feed>
    */

    private final static int MAX_LENGTH = 15;

    private String id;
    private String title;
    private String updated;
    private String alternateLink;
    private String selfLink;
    private String authorName;
    private String userName;

    private List<AtomEntry> items = new ArrayList<AtomEntry>(MAX_LENGTH);

    public void populate(Collection<EntryTo> entries) {
        AtomEntry item;

        // TODO: this sucks... need to make this reusable
        try {
            EntryTo o;
            Iterator<EntryTo> itr = entries.iterator();

            for (int x = 0, n = entries.size(); x < n && x < MAX_LENGTH; x++) {
                o = itr.next();
                item = new AtomEntry();
                item.setId("urn:jj:justjournal.com:atom1:" + o.getUserName()
                        + ":" + o.getId());
                item.setTitle(o.getSubject());
                item.setContent(o.getBody());
                item.setLink("http://www.justjournal.com/users/" + o.getUserName() + "/entry/" + o.getId());
                item.setPublished(o.getDateTime().toRFC3339());
                item.setUpdated(o.getDateTime().toRFC3339());
                Add(item);
            }
        } catch (Exception ignored) {
        }
    }

    public void Add(AtomEntry item) {
        items.add(item);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String toXml() {

        StringBuilder sb = new StringBuilder();

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");

        sb.append("<feed xmlns=\"http://www.w3.org/2005/Atom\"\n" +
                "      xml:lang=\"en\"\n" +
                "      xml:base=\"http://www.justjournal.com\">\n");
        sb.append("\t<id>urn:jj:justjournal.com:atom1:");
        sb.append(userName);
        sb.append("</id>\n");

        sb.append("\t\t<title>");
        sb.append(Xml.cleanString(title));
        sb.append("</title>\n");

        sb.append("\t<author>\n\t\t<name>");
        sb.append(authorName);
        sb.append("</name>\n\t</author>\n");

        sb.append("<link rel=\"alternate\" type=\"text/html\" href=\"");
        sb.append(alternateLink);
        sb.append("\"/>\n");

        sb.append("<link rel=\"self\" href=\"");
        sb.append(selfLink);
        sb.append("\"/>\n");

        sb.append("<updated>");
        sb.append(date());
        sb.append("</updated>\n");

        /*
        <link rel="service.feed" type="application/x.atom+xml"
        href="http://www.justjournal.com/users/laffer1/atom" title="Luke"/>
        */

        /* Iterator */
        AtomEntry o;
        Iterator<AtomEntry> itr = items.listIterator();
        for (int i = 0, n = items.size(); i < n && i < MAX_LENGTH; i++)     // 15 is the limit for RSS
        {
            o = itr.next();

            sb.append("\t\t<entry>\n");

            sb.append("\t\t\t<id>");
            sb.append(o.getId());
            sb.append("</id>\n");

            sb.append("\t\t\t<title>");
            sb.append(Xml.cleanString(o.getTitle()));
            sb.append("</title>\n");

            sb.append("\t\t\t<link rel=\"alternate\" type=\"text/html\" href=\"");
            sb.append(o.getLink());
            sb.append("\"/>\n");

            sb.append("\t\t\t<published>");
            sb.append(o.getPublished());
            sb.append("</published>\n");

            sb.append("\t\t\t<updated>");
            sb.append(o.getUpdated());
            sb.append("</updated>\n");

            if (o.getSummary() != null) {
                sb.append("\t\t\t<summary>");
                sb.append(o.getSummary());
                sb.append("</summary>\n");
            }

            if (o.getContent() != null) {
                sb.append("\t\t\t<content type=\"html\">");
                sb.append(Xml.cleanString(o.getContent()));
                sb.append("</content>\n");
            }

            sb.append("\t\t</entry>\n");
        }

        sb.append("\t</feed>\n");

        return sb.toString();
    }

    private String date() {
        //Sat, 07 Sep 2002 09:43:33 GMT
        Calendar cal = new GregorianCalendar(java.util.TimeZone.getDefault());
        java.util.Date current = cal.getTime();
        //final SimpleDateFormat formatmydate = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zz");

        return DateConvert.encode3339(current);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getAlternateLink() {
        return alternateLink;
    }

    public void setAlternateLink(String alternateLink) {
        this.alternateLink = alternateLink;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }


}

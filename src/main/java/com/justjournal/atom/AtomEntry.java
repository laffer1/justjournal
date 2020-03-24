package com.justjournal.atom;

/**
 * An Atom entry is a single record in an Atom feed.
 * @author Lucas Holt
 * @version $Id: AtomEntry.java,v 1.1 2007/06/04 05:55:13 laffer1 Exp $
 */
public final class AtomEntry {

    /*
    <entry>
    <id>http://www.example.org/entries/1</id>
    <title>A simple blog entry</title>
    <link href="/blog/2005/07/1" />
    <updated>2005-07-15T12:00:00Z</updated>
    <summary>This is a simple blog entry</summary>
    </entry>
    */

    private String id;
    private String title;
    private String link;
    private String updated;
    private String published;
    private String content;
    private String summary;

    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final AtomEntry atomEntry = (AtomEntry) o;

        if (!id.equals(atomEntry.id)) return false;
        if (link != null ? !link.equals(atomEntry.link) : atomEntry.link != null) return false;
        if (content != null ? !content.equals(atomEntry.content) : atomEntry.content != null) return false;
        if (title != null ? !title.equals(atomEntry.title) : atomEntry.title != null) return false;
        return updated != null ? updated.equals(atomEntry.updated) : atomEntry.updated == null;
    }

    public int hashCode() {
        int result;
        result = id.hashCode();
        result = 29 * result + (title != null ? title.hashCode() : 0);
        result = 29 * result + (link != null ? link.hashCode() : 0);
        result = 29 * result + (updated != null ? updated.hashCode() : 0);
        result = 29 * result + (content != null ? content.hashCode() : 0);
        return result;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(final String summary) {
        this.summary = summary;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(final String published) {
        this.published = published;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(final String link) {
        this.link = link;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(final String updated) {
        this.updated = updated;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }
}

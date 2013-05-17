package com.justjournal.content;

import com.justjournal.db.DateTime;

import java.util.Collection;

/**
 * User: laffer1
 * Date: Aug 8, 2005
 * Time: 10:56:28 PM
 */
public class ContentImpl implements Content {

    private String title;
    private Byte[] content;
    private String format;
    private String author;
    private DateTime dateCreated;
    private DateTime dateModified;
    private Collection metaData;


    /**
     * Retrieves a string representation of the content
     * entities title.
     *
     * @return Title of the content entity
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the content entity
     *
     * @param title The title of the content entity
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Retrieves the body or "meat" of the content entity.
     * (should have named this differently!)
     *
     * @return body of content entity
     */
    public Byte[] getContent() {
        return content;
    }

    /**
     * Sets the body of the content entity.
     *
     * @param content body of entry.
     */
    public void setContent(Byte[] content) {
        this.content = content;
    }

    /**
     * Sets the format of the content property.  Valid
     * examples might be "plain" "text" "xml" "binary"
     * or maybe content types depending on the implementation.
     *
     * @return Identifier of the type of content.
     */
    public String getFormat() {
        return format;
    }

    /**
     * Set the format of the content property.  Required
     * value varies by implemenation.
     *
     * @param format
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * The author of the content entity.
     *
     * @return A string representation of the author's name
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the authors name.
     *
     * @param author Name of author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Retrieve the date the item was created.
     *
     * @return Date created as a string.. format varies by
     *         implementation.
     */
    public DateTime getDateCreated() {
        return dateCreated;
    }

    /**
     * Sets the date the item was created.  Format varies
     * by implemenatation.
     *
     * @param dateCreated date as string item was created.
     */
    public void setDateCreated(DateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * Retrieves the last modified date for the item.
     *
     * @return string version of date modfied.  Varies by
     *         implemenation in date format.
     */
    public DateTime getDateModified() {
        return dateModified;
    }

    /**
     * Sets the date the item was last modified.  Varies
     * by implemnation in format.
     *
     * @param dateModified date as string of last modification.
     */
    public void setDateModified(DateTime dateModified) {
        this.dateModified = dateModified;
    }

    /**
     * Retrieve additional properties of the meta data.  Follows the
     * <code>MetaData</code> interface.
     *
     * @return MetaData formatted name/value pair.
     */
    public Collection getMetaData() {
        return metaData;
    }

    /**
     * Sets a colletion of properties in the <code>MetaData</code> interface.
     *
     * @param metaData MetaData collection of name/value pairs.
     */
    public void setMetaData(Collection metaData) {
        this.metaData = metaData;
    }
}

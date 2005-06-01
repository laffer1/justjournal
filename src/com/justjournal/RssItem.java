
package com.justjournal;

/**
 * @author Lucas Holt
 * User: laffer1
 * Date: Aug 28, 2003
 * Time: 12:19:36 AM
 */
public final class RssItem
{

    private String title;
    private String link;
    private String description;

    public String getTitle()
    {
        return title;
    }

    /**
     * Sets thte tile, triming the title to 100 characters
     * @param title
     */
    public void setTitle( String title )
    {

        if ( title.length() > 98 ) {
            this.title = title.substring( 0, 99 );
        } else {
            this.title = title;
        }

    }

    public String getLink()
    {
        return link;
    }

    public void setLink( String link )
    {
        this.link = link;
    }

    public String getDescription()
    {
        return description;
    }

    /**
     * Sets the description, trimming to 497
     * @param description
     */
    public void setDescription( String description )
    {

        if ( description.length() > 495 ) {
            this.description = description.substring( 0, 496 );
        } else {
            this.description = description;
        }

    }

    public void recycle() {
        title = "";
        link = "";
        description = "";
    }

}

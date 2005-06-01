
package com.justjournal;

import com.justjournal.db.EntryTo;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Lucas Holt
 * @version 1.1
 * @since 1.0
 * User: laffer1
 * Date: Aug 27, 2003
 * Time: 11:54:38 PM
 */
public final class Rss
{
    private final static int MAX_LENGTH = 15; // max size of RSS content

    private String title;
    private String link;
    private String description;
    private String language;
    private String copyright;
    private String webMaster;
    private String managingEditor;

    private ArrayList items = new ArrayList( MAX_LENGTH );


    public String getTitle()
    {
        return title;
    }

    public void setTitle( String title )
    {
        this.title = title;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink( String link )
    {
        this.link = link;
    }

    public String getCopyright()
    {
        return copyright;
    }

    public void setCopyright( String copyright )
    {
        this.copyright = copyright;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage( String language )
    {
        this.language = language;
    }

    public String getManagingEditor()
    {
        return managingEditor;
    }

    public void setManagingEditor( String managingEditor)
    {
        this.managingEditor = managingEditor;
    }

    public String getWebMaster()
    {
        return webMaster;
    }

    public void setWebMaster( String webMaster )
    {
        this.webMaster = webMaster;
    }

    // Methods

    public void populate( Collection entries, String userName ) {

        RssItem item;

        // TODO: this sucks... need to make this reusable
        try {

            EntryTo o;
            Iterator itr = entries.iterator();

            for ( int x = 0, n = entries.size(); x < n && x < MAX_LENGTH; x++ )
            {
                o = (EntryTo) itr.next();
                item = new RssItem();
                item.setTitle( o.getSubject() );
                item.setLink( "http://www.justjournal.com/users/" + userName );
                item.setDescription( o.getBody() );
                Add( item );
            }

        } catch (Exception e) {

        }
    }

    public void Add( RssItem item )
    {
        items.add( item );
    }

    public String toXml()
    {

        StringBuffer sb = new StringBuffer();
        String desc;

        sb.append( "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" );
        sb.append( "<!DOCTYPE rss SYSTEM \"http://my.netscape.com/publish/formats/rss-0.91.dtd\">\n\n" );

        sb.append( "<rss version=\"0.91\">\n" );
        sb.append( "\t<channel>\n" );

        sb.append( "\t\t<title>" );
        sb.append( title );
        sb.append("</title>\n" );

        sb.append( "\t\t<link>" );
        sb.append( link );
        sb.append( "</link>\n" );

        sb.append( "\t\t<description>" );
        sb.append( description );
        sb.append( "</description>\n" );

        sb.append( "\t\t<language>" );
        sb.append( language );
        sb.append( "</language>\n" );

        sb.append("\t\t<webMaster>");
        sb.append(webMaster);
        sb.append("</webMaster>\n");

        sb.append("\t\t<managingEditor>");
        sb.append(managingEditor);
        sb.append("</managingEditor>\n");

        sb.append( "\t\t<copyright>");
        sb.append(copyright);
        sb.append("</copyright>\n\n");

        /* Iterator */
        RssItem o;
        Iterator itr = items.listIterator();
        for ( int i = 0, n = items.size(); i < n && i < MAX_LENGTH; i++ )     // 15 is the limit for RSS
        {
            o = (RssItem) itr.next();

            sb.append( "\t\t<item>\n" );

            sb.append( "\t\t\t<title>" );
            sb.append( o.getTitle() );
            sb.append( "</title>\n" );

            sb.append( "\t\t\t<link>" );
            sb.append( o.getLink() );
            sb.append( "</link>\n" );

            // TODO: encode more characters!
            desc = o.getDescription();
            desc = StringUtil.replace( desc, '&', "&amp;" );
            desc = StringUtil.replace( desc, '<', "&lt;" );
            desc = StringUtil.replace( desc, '>', "&gt;" );

            sb.append( "\t\t\t<description>" );
            sb.append( desc );
            sb.append( "...</description>\n" );

            sb.append( "\t\t</item>\n" );
        }

        sb.append( "\t</channel>\n" );
        sb.append( "</rss>\n" );

        return sb.toString();
    }

    public void recycle()
    {
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

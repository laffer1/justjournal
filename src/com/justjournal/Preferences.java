
package com.justjournal;

import sun.jdbc.rowset.CachedRowSet;
import com.justjournal.db.PreferencesDao;

/** Loads and stores preferences for a just journal user
 * given their username.
 *
 * This class almost fits the bean concept now.
 *
 * private journal field was added 1/2004.  If this option is set,
 * the user does not want anyone to read their journal.  To read the
 * journal, the user must login.  Public access is denied.
 *
 * @author Lucas Holt
 * @version 1.1
 * @since 1.0
 * User: laffer1
 * Date: Jul 11, 2003
 * Time: 10:26:17 PM
 */

public final class Preferences
{

    /* Users real name */
    private String name = "";   // real name!
    private int id = 0; // user id
    /* Default style */
    private int styleId = 1;  // theme of journal?
    private boolean allowSpider = false;
    private boolean privateJournal = false;  // journal viewable only by owner.
    private String styleDoc = "";
    private String styleUrl = "";
    private int emoticon = 1;  // default emoticon theme
    private int startYear = 2003;

    public Preferences( String userName )
            throws Exception
    {
        try {

            CachedRowSet RS = PreferencesDao.ViewJournalPreferences( userName );

            if ( RS.next() ) {
                this.name = RS.getString( "name" );
                this.id = RS.getInt( "id" );
                this.styleId = RS.getInt( "style" );
                this.styleDoc = RS.getString( "cssdoc" );
                this.styleUrl = RS.getString( "cssurl" );

                if ( RS.getInt("since") > 2003 )
                    startYear = RS.getInt("since");

                // TODO: is this right?
                if ( RS.getString( "allow_spider" ).equals( "Y" ) ) {
                    this.allowSpider = true;
                } else {
                    this.allowSpider = false;
                }

                // TODO: is this right?
                if ( RS.getString( "owner_view_only" ).equals( "Y" ) ) {
                    this.privateJournal = true;
                } else {
                    this.privateJournal = false;
                }
            }

            RS.close();
        } catch ( Exception ePrefs ) {
            throw new Exception( "Error loading preferences", ePrefs );
        }
    }

    public Preferences()
    {
        // shouldn't use this constructor.
    }

    public String getName()
    {
        return this.name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public int getId()
    {
        return this.id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public boolean isSpiderAllowed()
    {
        return this.allowSpider;
    }

    public boolean isSpiderAllowed( boolean allowSpider )
    {
        this.allowSpider = allowSpider;
        return allowSpider;
    }

    public boolean isPrivateJournal()
    {
        return this.privateJournal;
    }

    public boolean isPrivateJournal( boolean privateJournal )
    {
        this.privateJournal = privateJournal;
        return privateJournal;
    }

    public int getEmoticon()
    {
        return emoticon;
    }

    public void setEmoticon( int value )
    {
        this.emoticon = value;
    }

    public int getStyleId()
    {
        return this.styleId;
    }

    public void setStyleId( int styleId )
    {
        this.styleId = styleId;
    }

    public String getStyleDoc()
    {
        return this.styleDoc;
    }

    public void setStyleDoc( String doc )
    {
        this.styleDoc = doc;
    }

    public String getStyleUrl()
    {
        return this.styleUrl;
    }

    public void setStyleUrl( String url )
    {
        this.styleUrl = url;
    }

    public int getStartYear()
    {
        return this.startYear;
    }

    public void setStartYear( int startYear )
    {
        this.startYear = startYear;
    }

    public void recycle()
    {
        name = "";
        id = 0;  // user id
        styleId = 1;
        allowSpider = false;
        privateJournal = false;
        styleDoc = "";
        styleUrl = "";
        startYear = 2003;
    }
}
package com.justjournal.ctl;

import com.justjournal.db.*;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Jan 1, 2004
 * Time: 8:46:20 PM
 */
public class EditEntry extends Protected {

    protected int entryId;
    protected EntryTo entry;
    protected Collection security;
    protected Collection location;
    protected Collection moods;

    public int getEntryId()
    {
        return this.entryId;
    }

    public void setEntryId( int entryId )
    {
        this.entryId = entryId;
    }

    public EntryTo getEntry()
    {
        return this.entry;
    }

    public Collection getSecurity()
    {
        return this.security;
    }

    public Collection getLocation()
    {
        return this.location;
    }

    public Collection getMoods()
    {
        return this.moods;
    }

    public String getMyLogin()
    {
        return this.currentLoginName();
    }

    protected String insidePerform() throws Exception
    {
        final EntryDAO edao = new EntryDAO();
        final SecurityDao sdao = new SecurityDao();
        final MoodDao mdao = new MoodDao();
        final LocationDao ldao = new LocationDao();

        if ( this.entryId < 1 )
            addError( "entryId", "The entry id was invalid." );

        if ( this.currentLoginId() < 1 )
            addError( "login", "The login timed out or is invalid." );

        if ( this.hasErrors() == false )
        {
            this.entry = edao.viewSingle( this.entryId, this.currentLoginId() );
            this.security = sdao.view();
            this.moods = mdao.view();
            this.location = ldao.view();
        }

        if ( this.hasErrors() )
            return ERROR;
        else
            return SUCCESS;
    }

}

package com.justjournal.ctl;

import com.justjournal.db.EntryDAO;
import com.justjournal.db.CommentDao;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Jan 1, 2004
 * Time: 8:46:34 PM
 * To change this template use Options | File Templates.
 */
public class DeleteEntry extends Protected {
    int entryId;

    public int getEntryid()
    {
        return entryId;
    }

    public void setEntryId( int entryId )
    {
        this.entryId = entryId;
    }

    protected String insidePerform() throws Exception
    {
        EntryDAO edao = new EntryDAO();
        CommentDao cdao = new CommentDao();
        boolean result;
        boolean result2 = false;

        if ( entryId < 1 )
            addError("entryId", "The entry id was invalid.");

        if ( this.currentLoginId() < 1 )
            addError( "login", "The login timed out or is invalid.");

        if ( this.hasErrors() == false )
        {
            // we need to delete the entry and comments associated
            // with the entry
            result = edao.delete( entryId, this.currentLoginId() );

            if (result)
                result2 = cdao.deleteByEntry( entryId );

            if ( result == false )
                addError("Unknown", "Could not delete entry.");
            if ( result2 == false )
                addError("Unknown", "Unable to delete comments associated with entry.");
        }

        if ( this.hasErrors() )
            return ERROR;
        else
            return SUCCESS;
    }
}

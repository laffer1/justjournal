package com.justjournal.ctl;

import com.justjournal.db.CommentDao;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Dec 31, 2003
 * Time: 9:44:13 PM
 * To change this template use Options | File Templates.
 */
public class DeleteComment extends Protected {

    protected int commentId;

    public int getCommentId()
    {
        return this.commentId;
    }

    public void setCommentId( int commentId )
    {
        this.commentId = commentId;
    }

    protected String insidePerform() throws Exception
    {
        CommentDao cdao = new CommentDao();

        if ( commentId < 1 )
            addError("commentId", "The comment id was invalid.");

        if ( this.currentLoginId() < 1 )
            addError( "login", "The login timed out or is invalid.");

        if ( this.hasErrors() == false )
        {
            boolean result = cdao.delete( commentId, this.currentLoginId() );

            if ( result == false )
                addError("Unknown", "Could not delete comment.");
        }

        if ( this.hasErrors() )
            return ERROR;
        else
            return SUCCESS;
    }
}

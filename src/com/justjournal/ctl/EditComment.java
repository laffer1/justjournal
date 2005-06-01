
package com.justjournal.ctl;

import com.justjournal.db.CommentDao;
import com.justjournal.db.CommentTo;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Jan 1, 2004
 * Time: 1:25:30 AM
 */
public class EditComment extends Protected
{
    protected int commentId;
    protected CommentTo comment;

    public int getCommentId()
    {
        return commentId;
    }

    public void setCommentId( int commentId )
    {
        this.commentId = commentId;
    }

    public CommentTo getComment()
    {
        return this.comment;
    }

    protected String insidePerform() throws Exception
    {
        final CommentDao cdao = new CommentDao();

        if ( commentId < 1 )
            addError( "commentId", "The comment id was invalid." );

        if ( this.currentLoginId() < 1 )
            addError( "login", "The login timed out or is invalid." );

        if ( this.hasErrors() == false )
        {
            comment = cdao.viewSingle( commentId );
        }

        if ( this.hasErrors() )
            return ERROR;
        else
            return SUCCESS;
    }
}

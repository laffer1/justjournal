
package com.justjournal.ctl;

import com.justjournal.db.CommentDao;
import com.justjournal.db.CommentTo;
import com.justjournal.StringUtil;
import org.apache.log4j.Category;


/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Dec 31, 2003
 * Time: 9:44:03 PM
 * To change this template use Options | File Templates.
 */
public class EditCommentSubmit extends Protected
{

    private static Category log = Category.getInstance(EditCommentSubmit.class.getName());

    protected int commentId;
    protected String body;
    protected String date;
    protected String subject;
    protected int eid;
    protected int userId;

    public int getCommentId()
    {
        return this.commentId;
    }

    public void setCommentId( int commentId )
    {
        this.commentId = commentId;
    }

    public void setEid( int eid )
    {
        this.eid = eid;
    }

    public void setUserId( int userId )
    {
        this.userId = userId;
    }

    public void setSubject( String subject )
    {
        this.subject = subject;
    }

    public void setBody( String body )
    {
        this.body = body;
    }

    public void setDate( String date )
    {
        this.date = date;
    }

    protected String insidePerform() throws Exception
    {

        if (log.isDebugEnabled())
				log.debug("Loading DAO Objects  " );

        final CommentDao cdao = new CommentDao();
        final CommentTo comment = new CommentTo();

        if ( commentId < 1 )
            addError( "commentId", "The comment id was invalid." );

        if ( this.currentLoginId() < 1 )
            addError( "login", "The login timed out or is invalid." );

        try
        {
            comment.setBody( StringUtil.replace( body, '\'', "\\\'" ) );
            comment.setDate( date );
            comment.setSubject( StringUtil.replace( subject, '\'', "\\\'" ) );
            comment.setEid( eid );
            comment.setUserId( userId );
            comment.setId( commentId );

            if (log.isDebugEnabled())
				log.debug("comment to add:\n" + comment.toString() );
        }
        catch ( IllegalArgumentException e )
        {
            addError( "Input", e.getMessage() );
        }

        if ( this.hasErrors() == false )
        {
            boolean result = cdao.update( comment );

            if (log.isDebugEnabled())
				log.debug("Was there an error with data tier?  " + !result );

            if ( result == false )
                addError( "Unknown", "Could not update comment." );
        }

        if ( this.hasErrors() )
            return ERROR;
        else
            return SUCCESS;
    }
}

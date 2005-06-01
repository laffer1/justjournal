
package com.justjournal.db;

import com.justjournal.SQLHelper;
import com.justjournal.StringUtil;
import sun.jdbc.rowset.CachedRowSet;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Dec 25, 2003
 * Time: 2:19:28 PM
 */
public final class CommentDao
{

    /** Add a comment on a journal entry
     * @param comment A user comment to change
     * @return true if no error occured.
     */
    public boolean add( final CommentTo comment )
    {
        boolean noError = true;
        int records = 0;


        final String sqlStmt =
                "Insert INTO comments (id,uid,eid,date,subject,body) values(NULL,'"
                + comment.getUserId() + "','"
                + comment.getEid() + "','"
                + comment.getDate() + "','"
                + StringUtil.replace( comment.getSubject(), '\'', "\\\'" ) + "','"
                + StringUtil.replace( comment.getBody(), '\'', "\\\'" ) + "');";


        try
        {
            records = SQLHelper.executeNonQuery( sqlStmt );
        }
        catch ( Exception e )
        {
            noError = false;
        }

        if ( records != 1 )
            noError = false;

        return noError;
    }

    /** change a comment on a journal entry.
     * @param comment A user comment to change
     * @return true if no error occured.
     */
    public boolean update( final CommentTo comment )
    {
        boolean noError = true;


        final String sqlStmt = "Update comments SET subject='" +
                StringUtil.replace( comment.getSubject(), '\'', "\\\'" )
                + "', body='" +
                StringUtil.replace( comment.getBody(), '\'', "\\\'" )
                + "' WHERE id='" +
                comment.getId() + "' AND uid='" +
                comment.getUserId() + "' AND eid='" +
                comment.getEid() + "' LIMIT 1;";

            /*    final String sqlStmt = "Update comments SET subject='" +
                 comment.getSubject() + "' AND body='" +
                 comment.getBody() + "' WHERE id='" +
                comment.getId() + "' AND uid='" +
                comment.getUserId() + "' AND eid='" +
                comment.getEid() + "' LIMIT 1;";  */

        try
        {
            SQLHelper.executeNonQuery( sqlStmt );
        }
        catch ( Exception e )
        {
            noError = false;
        }

        return noError;
    }

    public boolean delete( final int commentId, final int userId )
    {
        boolean noError = true;
        final String sqlStmt = "DELETE FROM comments WHERE id='" + commentId + "' AND uid='" + userId + "' LIMIT 1;";

        if ( commentId > 0 && userId > 0 )
        {
            try
            {
                SQLHelper.executeNonQuery( sqlStmt );
            }
            catch ( Exception e )
            {
                noError = false;
            }
        }
        else
        {
            noError = false;
        }

        return noError;
    }

    /**
     * Deletes comments belonging to a specific entry id.
     * used by the delete entry logic.  Not recommended for
     * direct calls by users.
     *
     * @param entryId
     * @return
     */
    public boolean deleteByEntry( final int entryId )
    {
        boolean noError = true;
        final String sqlStmt = "DELETE FROM comments WHERE eid='" + entryId + "' LIMIT 1;";

        if ( entryId > 0 )
        {
            try
            {
                SQLHelper.executeNonQuery( sqlStmt );
            }
            catch ( Exception e )
            {
                noError = false;
            }
        }
        else
        {
            noError = false;
        }

        return noError;
    }

    public CommentTo viewSingle( final int commentId )
    {
        CachedRowSet rs = null;
        final CommentTo comment = new CommentTo();
        final String sqlStmt =
                "Select user.username, comments.date,comments.subject,comments.body, comments.uid, comments.id As cid, comments.eid FROM comments,user WHERE comments.id='"
                + commentId + "' AND comments.uid = user.id;";

        try
        {
            rs = SQLHelper.executeResultSet( sqlStmt );

            if ( rs.next() )
            {
               // set the properites on the bean
                comment.setId( rs.getInt( "cid" ) );
                comment.setUserName( rs.getString( "username" ) );
                comment.setDate( rs.getString( "date" ) );
                comment.setSubject( rs.getString( "subject" ) );
                comment.setBody( rs.getString( "body" ) );
                comment.setEid( rs.getInt( "eid" ) );
                comment.setUserId( rs.getInt( "uid" ) );
            }

            rs.close();

        }
        catch ( Exception e1 )
        {
            if ( rs != null )
            {
                try
                {
                    rs.close();
                }
                catch ( Exception e )
                {
                    // NOTHING TO DO
                }
            }
        }

        return comment;
    }

    public ArrayList view( final int entryId )
    {
        final ArrayList comments = new ArrayList( 5 );  // 5 is average comments on entry?
        CachedRowSet rs = null;
        CommentTo comment;
        final String sqlStmt =
                "Select user.username, comments.date,comments.subject,comments.body, comments.uid, comments.id As cid FROM comments,user WHERE comments.eid='"
                + entryId + "' AND comments.uid = user.id;";

        try
        {
            rs = SQLHelper.executeResultSet( sqlStmt );

            while ( rs.next() )
            {
                // create a new comment to put in the array list
                comment = new CommentTo();

                // set the properites on the bean
                comment.setId( rs.getInt( "cid" ) );
                comment.setUserName( rs.getString( "username" ) );
                comment.setDate( rs.getString( "date" ) );
                comment.setSubject( rs.getString( "subject" ) );
                comment.setBody( rs.getString( "body" ) );
                comment.setEid( entryId );
                comment.setUserId( rs.getInt( "uid" ) );

                // add to the array list
                comments.add( comment );
            }

            rs.close();

        }
        catch ( Exception e1 )
        {
            if ( rs != null )
            {
                try
                {
                    rs.close();
                }
                catch ( Exception e )
                {
                    // NOTHING TO DO
                }
            }
        }

        return comments;

    }

}

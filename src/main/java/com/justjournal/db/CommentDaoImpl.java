/*
Copyright (c) 2003-2006, 2014 Lucas Holt
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are
permitted provided that the following conditions are met:

  Redistributions of source code must retain the above copyright notice, this list of
  conditions and the following disclaimer.

  Redistributions in binary form must reproduce the above copyright notice, this
  list of conditions and the following disclaimer in the documentation and/or other
  materials provided with the distribution.

  Neither the name of the Just Journal nor the names of its contributors
  may be used to endorse or promote products derived from this software without
  specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
*/

package com.justjournal.db;

import com.justjournal.utility.StringUtil;
import com.sun.istack.internal.NotNull;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Manipulate and fetch comments
 *
 * @author Lucas Holt
 */
@Component
public final class CommentDaoImpl implements CommentDao {
    private static final Logger log = Logger.getLogger(CommentDaoImpl.class);

    /**
     * Add a comment on a journal entry
     *
     * @param comment A user comment to change
     * @return true if no error occured.
     */
    public boolean add(final Comment comment) {

        final String sqlStmt =
                "Insert INTO comments (id,uid,eid,date,subject,body) values(NULL,'"
                        + comment.getUserId() + "','"
                        + comment.getEid() + "','"
                        + comment.getDate() + "','"
                        + StringUtil.replace(comment.getSubject(), '\'', "\\\'") + "','"
                        + StringUtil.replace(comment.getBody(), '\'', "\\\'") + "');";

        return BaseDao.add(sqlStmt);
    }

    /**
     * change a comment on a journal entry.
     *
     * @param comment A user comment to change
     * @return true if no error occured.
     */
    public boolean update(final Comment comment) {

        final String sqlStmt = "Update comments SET subject='" +
                StringUtil.replace(comment.getSubject(), '\'', "\\\'")
                + "', body='" +
                StringUtil.replace(comment.getBody(), '\'', "\\\'")
                + "' WHERE id='" +
                comment.getId() + "' AND uid='" +
                comment.getUserId() + "' AND eid='" +
                comment.getEid() + "' LIMIT 1;";

        return BaseDao.edit(sqlStmt);
    }

    public boolean delete(final int commentId, final int userId) {
        boolean noError = true;
        final String sqlStmt = "DELETE FROM comments WHERE id='" + commentId + "' AND uid='" + userId + "' LIMIT 1;";

        if (commentId > 0 && userId > 0) {
            try {
                SQLHelper.executeNonQuery(sqlStmt);
            } catch (Exception e) {
                noError = false;
            }
        } else {
            noError = false;
        }

        return noError;
    }

    /**
     * Deletes comments belonging to a specific entry id. used by the delete entry logic.  Not recommended for direct
     * calls by Users.
     *
     * @param entryId Unique identifier of entry to delete
     * @return true on success, false on any error.
     */
    public boolean deleteByEntry(final int entryId) {
        boolean noError = true;
        final String sqlStmt = "DELETE FROM comments WHERE eid='" + entryId + "' LIMIT 1;";

        if (entryId > 0) {
            try {
                SQLHelper.executeNonQuery(sqlStmt);
            } catch (Exception e) {
                noError = false;
            }
        } else {
            noError = false;
        }

        return noError;
    }

    public
    @NotNull
    Comment get(final int commentId) {
        ResultSet rs = null;
        final Comment comment = new CommentTo();
        final String sqlStmt =
                "Select user.username, comments.date,comments.subject,comments.body, comments.uid, comments.id As cid, comments.eid FROM comments,user WHERE comments.id='"
                        + commentId + "' AND comments.uid = user.id;";

        try {
            rs = SQLHelper.executeResultSet(sqlStmt);

            if (rs.next()) {
                // set the properites on the bean
                comment.setId(rs.getInt("cid"));
                comment.setUserName(rs.getString("username"));
                comment.setDate(rs.getString("date"));
                comment.setSubject(rs.getString("subject"));
                comment.setBody(rs.getString("body"));
                comment.setEid(rs.getInt("eid"));
                comment.setUserId(rs.getInt("uid"));
            }

            rs.close();

        } catch (Exception e1) {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    // NOTHING TO DO
                }
            }
        }

        return comment;
    }

    public
    @NotNull
    List<Comment> list(final int entryId) {
        final List<Comment> comments = new ArrayList<Comment>(5);  // 5 is average comments on entry?
        ResultSet rs = null;
        Comment comment;
        final String sqlStmt =
                "Select user.username, comments.date,comments.subject,comments.body, comments.uid, comments.id As cid FROM comments,user WHERE comments.eid='"
                        + entryId + "' AND comments.uid = user.id;";

        try {
            rs = SQLHelper.executeResultSet(sqlStmt);

            while (rs.next()) {
                // create a new comment to put in the array list
                comment = new CommentTo();

                // set the properites on the bean
                comment.setId(rs.getInt("cid"));
                comment.setUserName(rs.getString("username"));
                comment.setDate(rs.getString("date"));
                comment.setSubject(rs.getString("subject"));
                comment.setBody(rs.getString("body"));
                comment.setEid(entryId);
                comment.setUserId(rs.getInt("uid"));

                // add to the array list
                comments.add(comment);
            }

            rs.close();

        } catch (Exception e1) {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    // NOTHING TO DO
                }
            }
        }

        return comments;

    }

    /**
     * Count the number of blog comments this user has made.
     *
     * @param userName the blog user
     * @return number of comments
     * @throws Exception SQL
     */
    public int count(@NotNull final String userName) throws Exception {
        String sqlStatement;

        if (userName == null || userName.isEmpty()) {
            log.trace("CommentDaoImpl.count(): username is null or empty");
            return 0;
        }

        sqlStatement = "SELECT count(*) as cnt" +
                " FROM user As us, comments As com " +
                " WHERE us.username = '" + userName +
                "' AND us.id = com.uid;";
        log.trace("CommentDaoImpl.count(): sql is " + sqlStatement);

        return SQLHelper.scalarInt(sqlStatement);
    }

}

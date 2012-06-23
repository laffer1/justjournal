/*
Copyright (c) 2005, Lucas Holt
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

package com.justjournal.ctl;

import com.justjournal.db.CommentDao;
import com.justjournal.db.CommentTo;
import com.justjournal.utility.StringUtil;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;


/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Dec 31, 2003
 * Time: 9:44:03 PM
 * To change this template use Options | File Templates.
 */
public class EditCommentSubmit extends Protected {

    private static final Logger log = Logger.getLogger(EditCommentSubmit.class.getName());

    protected int commentId;
    protected String body;
    protected String date;
    protected String subject;
    protected int eid;
    protected int userId;

    public int getCommentId() {
        return this.commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setDate(String date) {
        this.date = date;
    }

    protected String insidePerform() throws Exception {

        if (log.isDebugEnabled())
            log.debug("Loading DAO Objects  ");

        final CommentDao cdao = new CommentDao();
        final CommentTo comment = new CommentTo();

        if (commentId < 1)
            addError("commentId", "The comment id was invalid.");

        if (this.currentLoginId() < 1)
            addError("login", "The login timed out or is invalid.");

        try {
            comment.setBody(StringUtil.replace(body, '\'', "\\\'"));
            comment.setDate(date);
            comment.setSubject(StringUtil.replace(subject, '\'', "\\\'"));
            comment.setEid(eid);
            comment.setUserId(userId);
            comment.setId(commentId);

            if (log.isDebugEnabled())
                log.debug("comment to add:\n" + comment.toString());
        } catch (IllegalArgumentException e) {
            addError("Input", e.getMessage());
        }

        if (this.hasErrors() == false) {
            boolean result = cdao.update(comment);

            if (log.isDebugEnabled())
                log.debug("Was there an error with data tier?  " + !result);

            if (result == false)
                addError("Unknown", "Could not edit comment.");
        }

        if (this.hasErrors())
            return ERROR;
        else
            return SUCCESS;
    }
}

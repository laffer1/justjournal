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

import com.justjournal.db.LJFriendDao;
import com.justjournal.db.LJFriendTo;
import org.apache.log4j.Category;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Jan 9, 2004
 * Time: 2:16:04 PM
 *
 * @author Lucas Holt
 * @version 1.0
 */
public class AddLJFriendSubmit extends Protected {
    private static Category log = Category.getInstance(AddLJFriendSubmit.class.getName());

    protected String userName; // lj username
    protected String community; // is a community on LJ?

    public String getUserName() {
        return userName;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    protected String insidePerform() throws Exception {
        if (log.isDebugEnabled())
            log.debug("Loading DAO Objects  ");

        LJFriendDao ljdao = new LJFriendDao();
        LJFriendTo lj = new LJFriendTo();
        boolean result;
        boolean bcommunity;


        if (userName == null || userName.length() < 1 || userName.length() > 15)
            addError("userName", "The LJ friend username is invalid.");

        if (this.currentLoginId() < 1)
            addError("login", "The login timed out or is invalid.");

        if (this.community != null && this.community.equals("checked"))
            bcommunity = true;
        else
            bcommunity = false;

        if (this.hasErrors() == false) {

            lj.setId(this.currentLoginId());
            lj.setUserName(userName);
            lj.setIsCommunity(bcommunity);

            result = ljdao.add(lj);

            if (log.isDebugEnabled())
                log.debug("Was there an error with data tier?  " + !result);

            if (result == false)
                addError("Unknown", "Could not add friend.");
        }

        if (this.hasErrors())
            return ERROR;
        else
            return SUCCESS;
    }
}

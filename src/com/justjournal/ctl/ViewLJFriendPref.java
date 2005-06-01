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

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Jan 9, 2004
 * Time: 2:17:23 PM
 */
public class ViewLJFriendPref extends Protected {
    private Collection ljfriends;

    public String getMyLogin() {
        return this.currentLoginName();
    }

    public Collection getljfriends() {
        return this.ljfriends;
    }

    protected String insidePerform() throws Exception {
        LJFriendDao ljdao = new LJFriendDao();

        if (this.currentLoginId() < 1)
            addError("login", "The login timed out or is invalid.");

        if (this.hasErrors() == false) {
            try {
                ljfriends = ljdao.view(this.currentLoginId());

            } catch (Exception e) {
                return ERROR;
            }
        } else {
            return ERROR;
        }

        return SUCCESS;
    }
}
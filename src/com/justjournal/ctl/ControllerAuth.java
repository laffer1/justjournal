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

import com.justjournal.webLogin;

/**
 * Controller provides framework for authenticated services.
 * The parent is a bean based controller using this as its
 * bean unless otherwise specified.
 *
 * @author Lucas Holt
 * @version 1.0
 * @see ControllerErrorable
 * @since 1.0
 */
public class ControllerAuth extends ControllerErrorable {
    /**
     * The name of the session attribute which stores the login name.
     */
    protected static final String LOGIN_ATTRNAME = "auth.user";
    protected static final String LOGIN_ATTRID = "auth.uid";

    /**
     * @return the login of the user currently logged in, or null if no
     *         user is logged in.
     */
    protected String currentLoginName() {
        return (String) this.getCtx().getRequest().getSession().getAttribute(LOGIN_ATTRNAME);
    }

    protected int currentLoginId() {
        int aUserID = 0;
        Integer userIDasi = (Integer) this.getCtx().getRequest().getSession().getAttribute(LOGIN_ATTRID);

        if (userIDasi != null) {
            aUserID = userIDasi.intValue();
        }

        return aUserID;
    }

    /**
     * Try to log in with the specified credentials.
     *
     * @return true if it worked, false if the credentials are bad.
     */
    protected boolean login(String userName, String password) {

        int userId = webLogin.validate(userName, password);


        if (userId < 1) {
            return false;
        } else {
            this.getCtx().getRequest().getSession().setAttribute(LOGIN_ATTRNAME, userName);
            this.getCtx().getRequest().getSession().setAttribute(LOGIN_ATTRID, new Integer(userId));
            return true;
        }
    }

    /**
     * Disable the login credentials in the session.
     * username and id are removed.
     */
    protected void logout() {
        this.getCtx().getRequest().getSession().removeAttribute(LOGIN_ATTRNAME);
        this.getCtx().getRequest().getSession().removeAttribute(LOGIN_ATTRID);
    }

    /**
     * Determines if the user is logged in during this session.
     */
    protected boolean isLoggedIn() {
        return this.getCtx().getRequest().getSession().getAttribute(LOGIN_ATTRNAME) != null;
    }

}

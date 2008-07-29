/*
Copyright (c) 2005 Lucas Holt
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

import com.justjournal.WebLogin;
import org.apache.log4j.Category;

/**
 * Change the password for a blog owner
 * 
 * User: laffer1
 * Date: Mar 30, 2005
 * Time: 4:37:00 PM
 *
 * @author Lucas Holt
 * @version $Id: ChangePasswordSubmit.java,v 1.6 2008/07/29 11:59:42 laffer1 Exp $
 */
public class ChangePasswordSubmit
        extends Protected {
    private static Category log = Category.getInstance(ChangePasswordSubmit.class.getName());

    protected String passCurrent;
    protected String passNew;

    public String getPassCurrent() {
        return passCurrent;
    }

    public void setPassCurrent(final String passCurrent) {
        this.passCurrent = passCurrent;
    }

    public String getPassNew() {
        return passNew;
    }

    public void setPassNew(final String passNew) {
        this.passNew = passNew;
    }

    protected String insidePerform() throws Exception {
        if (log.isDebugEnabled())
            log.debug("Loading DAO Objects  ");

        boolean result;

        if (passCurrent == null ||
                passCurrent.length() < 5 ||
                passCurrent.length() > 15)
            addError("passCurrent", "The current password is invalid.");

        if (passNew == null ||
                passNew.length() < 5 ||
                passNew.length() > 15)
            addError("passNew", "The new password is invalid.");

        if (this.currentLoginId() < 1)
            addError("login", "The login timed out or is invalid.");

        if (!this.hasErrors()) {
            result = WebLogin.changePass(currentLoginName(), passCurrent, passNew);

            if (log.isDebugEnabled())
                log.debug("Was there an error with data tier?  " + !result);

            if (!result)
                addError("Unknown", "Error changing password.  Did you type in your old password correctly?");
        }

        if (this.hasErrors())
            return ERROR;
        else
            return SUCCESS;
    }
}

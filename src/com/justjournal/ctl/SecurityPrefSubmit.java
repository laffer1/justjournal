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

import com.justjournal.db.PreferencesDao;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Jan 16, 2004
 * Time: 12:03:26 PM
 * To change this template use Options | File Templates.
 */
public class SecurityPrefSubmit extends Protected {
    private static final Logger log = Logger.getLogger(SecurityPrefSubmit.class.getName());

    protected String ownerOnly;

    public String getOwnerOnly() {
        return this.ownerOnly;
    }

    public void setOwnerOnly(String ownerOnly) {
        this.ownerOnly = ownerOnly;
    }

    protected String insidePerform() throws Exception {

        boolean bownerOnly;

        if (this.currentLoginId() < 1)
            addError("login", "The login timed out or is invalid.");

        if (log.isDebugEnabled())
            log.debug("Loading DAO Objects  ");

        final PreferencesDao pdao = new PreferencesDao();

        if (this.ownerOnly != null && this.ownerOnly.equals("checked"))
            bownerOnly = true;
        else
            bownerOnly = false;

        if (this.hasErrors() == false) {
            boolean result = pdao.updateSec(this.currentLoginId(), bownerOnly);

            if (log.isDebugEnabled())
                log.debug("Was there an error with data tier?  " + !result);

            if (result == false)
                addError("Unknown", "Could not update setting.");
        }

        if (this.hasErrors())
            return ERROR;
        else
            return SUCCESS;
    }
}

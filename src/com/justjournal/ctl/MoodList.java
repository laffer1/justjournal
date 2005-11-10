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

import org.apache.log4j.Category;
import com.justjournal.db.MoodDao;

import java.util.Collection;

/**
 * List moods used for journal entries.
 * 
 * @author Lucas Holt
 * @version 1.0
 * Date: Nov 10, 2005
 * Time: 3:51:59 PM
 * @since 1.0
 */
public class MoodList extends ControllerErrorable {
    private static Category log = Category.getInstance(MoodList.class.getName());
    protected Collection mood;

    public Collection getMood() {
        return mood;
    }

    public void setMood( Collection mood ) {
        this.mood = mood;
    }

    protected String insidePerform() throws Exception {
        if (log.isDebugEnabled())
            log.debug("Loading DAO Objects  ");

        MoodDao dao = new MoodDao();
        boolean result;

        if (!this.hasErrors()) {
            mood = dao.view();
            result = !mood.isEmpty();

            if (log.isDebugEnabled())
                log.debug("Was there an error with data tier?  " + !result);

            if (!result)
                addError("Unknown", "No moods found.");
        }

        if (this.hasErrors())
            return ERROR;
        else
            return SUCCESS;
    }

}

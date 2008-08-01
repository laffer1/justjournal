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

import com.justjournal.db.EntryDAO;
import com.justjournal.db.EntryTo;
import com.justjournal.db.SQLHelper;
import org.apache.log4j.Category;

import javax.sql.rowset.CachedRowSet;
import java.util.ArrayList;
import java.util.Collection;

/**
 * View the favorite entries list.
 *
 * @author Lucas Holt
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Created:
 *        Date: Dec 10, 2005
 *        Time: 8:44:39 PM
 */
public class ViewFavorite extends Protected {
    private static Category log = Category.getInstance(ViewFavorite.class.getName());
    protected Collection<EntryTo> entries = new ArrayList<EntryTo>(20);


    /**
     * Retrieve the collection of favorite entries
     *
     * @return an arraylist containing EntryTo objects
     */
    public Collection<EntryTo> getEntries() {
        return this.entries;
    }

    /**
     * Get the current logged in user's username
     *
     * @return username as string
     */
    public String getMyLogin() {
        return this.currentLoginName();
    }

    /**
     * Perform the work to retrieve the entries from our data store
     *
     * @return SUCCESS or ERROR views
     * @throws Exception
     */
    protected String insidePerform() throws Exception {
        EntryDAO edao = new EntryDAO();

        if (log.isDebugEnabled())
            log.debug("insidePerform(): Attempting to view favorites");

        if (this.currentLoginId() < 1)
            addError("login", "The login timed out or is invalid.");

        if (!this.hasErrors()) {
            try {
                String sql = "call viewfavorite(" + this.currentLoginId() + ");";
                CachedRowSet rs = SQLHelper.executeResultSet(sql);

                while (rs.next()) {
                    int eid = rs.getInt("entryid");
                    EntryTo et = edao.viewSingle(eid, false);
                    entries.add(et);
                }
            }
            catch (Exception e) {
                addError("View Favorites", "Could not retrieve favorites.");
                if (log.isDebugEnabled())
                    log.debug("insidePerform(): " + e.getMessage());
            }
        }

        if (this.hasErrors())
            return ERROR;
        else
            return SUCCESS;
    }

}

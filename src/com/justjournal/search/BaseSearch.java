/*
Copyright (c) 2007, Lucas Holt
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

package com.justjournal.search;

import com.justjournal.db.SQLHelper;
import org.apache.log4j.Category;
import sun.jdbc.rowset.CachedRowSet;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Lucas Holt
 * @version $Id: BaseSearch.java,v 1.4 2008/04/26 17:09:43 laffer1 Exp $
 */
public class BaseSearch {
    private static Category log = Category.getInstance(BaseSearch.class.getName());

    protected ArrayList<String> terms = new ArrayList<String>();
    protected ArrayList<String> fieldlist = new ArrayList<String>();
    protected int maxresults = 30;
    protected String baseQuery;
    protected String sort;

    public void setMaxResults(int results) {
        maxresults = results;
    }

    public void setBaseQuery(String base) {
        if (base != null && base.length() > 0)
            baseQuery = base;
    }

    public void setFields(String fields) {
        String q[] = fields.split("\\s");
        fieldlist.addAll(Arrays.asList(q));
    }

    public void setSortAscending(String field) {
        if (field != null && field.length() > 0)
            sort = "ORDER BY " + field;
    }

    public void setSortDescending(String field) {
        if (field != null && field.length() > 0)
            sort = "ORDER BY " + field + " DESC";
    }

    public CachedRowSet search(String query) {
        if (log.isDebugEnabled()) {
            log.debug("search() called with " + query);
        }
        CachedRowSet result;
        parseQuery(query);

        result = realSearch(terms);

        return result;
    }

    protected void parseQuery(String query) {
        String q[] = query.split("\\s");
        final int qLen = java.lang.reflect.Array.getLength(q);

        for (int i = 0; i < qLen; i++) {
            if (!(q[i].equalsIgnoreCase("and") ||
                    (q[i].contains("*") ||
                            q[i].contains(";") ||
                            q[i].contains("|"))))
                terms.add(q[i]);
        }
    }

    protected CachedRowSet realSearch(ArrayList<String> terms) {

        String sqlStmt = baseQuery;
        CachedRowSet rs = null;


        for (int i = 0; i < terms.size(); i++) {
            sqlStmt += " (";
            for (int y = 0; y < fieldlist.size(); y++) {
                if (y != 0)
                    sqlStmt += " or ";
                sqlStmt += fieldlist.get(y) + " like '%" + terms.get(i) + "%'";
            }
            sqlStmt += ") and ";
        }

        sqlStmt += " 1=1 " + sort + " LIMIT 0," + maxresults + ";";

        try {
            if (log.isDebugEnabled()) {
                log.debug("realSearch() called on " + sqlStmt);
            }
            rs = SQLHelper.executeResultSet(sqlStmt);

        } catch (Exception e) {
            log.debug(sqlStmt);
            log.debug(e.getMessage());
        }

        return rs;
    }
}

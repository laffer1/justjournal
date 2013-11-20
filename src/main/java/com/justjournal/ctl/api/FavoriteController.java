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
package com.justjournal.ctl.api;

import com.justjournal.WebLogin;
import com.justjournal.db.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.sql.ResultSet;
import java.util.Map;


/**
 * View the favorite entries list.
 *
 * @author Lucas Holt
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Created: Date: Dec 10, 2005 Time: 8:44:39 PM
 */
@Controller
@RequestMapping("/api/favorite")
final public class FavoriteController {
    private static final Logger log = Logger.getLogger(FavoriteController.class.getName());

    /**
     * Retrieve the collection of favorite entries
     *
     * @return an arraylist containing EntryTo objects
     */

    @RequestMapping(method = RequestMethod.GET, produces="application/json")
    public
    @ResponseBody
    Collection<EntryTo> getFavorites(HttpSession session, HttpServletResponse response) {
        Collection<EntryTo> entries = new ArrayList<EntryTo>(20);

        try {

            String sql = "call viewfavorite(" + WebLogin.currentLoginId(session) + ");";
            ResultSet rs = SQLHelper.executeResultSet(sql);

            while (rs.next()) {
                int eid = rs.getInt("entryid");
                EntryTo et = EntryDAO.viewSingle(eid, false);
                entries.add(et);
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(e.getMessage());
        }
        return entries;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public
    @ResponseBody
    Map<String, String> create(@RequestBody EntryTo favorite, HttpSession session, HttpServletResponse response) {

        try {
            String sql = "call addfavorite( " + WebLogin.currentLoginId(session) + "," + favorite.getId() + ");";
            int result = SQLHelper.executeNonQuery(sql);

            if (result != 1) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return java.util.Collections.singletonMap("error", "Error adding your favorite.  Perhaps its already a favorite?");
            }
            return java.util.Collections.singletonMap("id", ""); // XXX
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "Could not add the favorite.");
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public
    @ResponseBody
    Map<String, String> delete(@RequestBody EntryTo favorite, HttpSession session, HttpServletResponse response) throws Exception {
        if (!WebLogin.isAuthenticated(session)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "The login timed out or is invalid.");
        }

        try {
            String sql = "call deletefavorite( " + WebLogin.currentLoginId(session) + "," + favorite.getId() + ");";
            int result = SQLHelper.executeNonQuery(sql);

            if (result != 1) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return java.util.Collections.singletonMap("error", "Error deleting your favorite.");
            }
            return java.util.Collections.singletonMap("id", Integer.toString(favorite.getId()));
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "Could not delete the favorite.");
        }
    }
}

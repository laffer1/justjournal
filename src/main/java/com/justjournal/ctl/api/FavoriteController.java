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

import com.justjournal.Login;
import com.justjournal.model.Entry;
import com.justjournal.model.Favorite;
import com.justjournal.model.User;
import com.justjournal.repository.EntryRepository;
import com.justjournal.repository.FavoriteRepository;
import com.justjournal.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * View the favorite entries list.
 *
 * @author Lucas Holt
 * @version 1.0
 * @since 1.0
 * <p/>
 * Created: Date: Dec 10, 2005 Time: 8:44:39 PM
 */
@Slf4j
@RestController
@RequestMapping("/api/favorite")
public class FavoriteController {

    private final FavoriteRepository favoriteRepository;

    private final EntryRepository entryRepository;

    private final UserRepository userRepository;

    @Autowired
    public FavoriteController(final FavoriteRepository favoriteRepository, final EntryRepository entryRepository, final UserRepository userRepository) {
        this.favoriteRepository = favoriteRepository;
        this.entryRepository = entryRepository;
        this.userRepository = userRepository;
    }


    /**
     * Retrieve the collection of favorite entries
     *
     * @return an arraylist containing EntryTo objects
     */

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    Collection<Entry> getFavorites(final HttpSession session, final HttpServletResponse response) {
        final Collection<Entry> entries = new ArrayList<>();

        try {
            final User user = userRepository.findById(Login.currentLoginId(session)).orElse(null);
            final List<Favorite> favoriteList = favoriteRepository.findByUser(user);

            for (final Favorite f : favoriteList) {
                entries.add(f.getEntry());
            }

        } catch (final Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            log.error(e.getMessage());
        }
        return entries;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{entryId}")
    @ResponseBody
    public
    Map<String, String> create(@PathVariable("entryId") final int entryId,
                               final HttpSession session,
                               final HttpServletResponse response) {

        try {
            final User user = userRepository.findById(Login.currentLoginId(session)).orElse(null);
            final Entry e = entryRepository.findById(entryId).orElse(null);

            final Favorite f = new Favorite();
            f.setUser(user);
            f.setEntry(e);
            if (favoriteRepository.saveAndFlush(f) == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return java.util.Collections.singletonMap("error", "Error adding your favorite.  Perhaps its already a favorite?");
            }
            return java.util.Collections.singletonMap("id", ""); // XXX
        } catch (final Exception e) {
            log.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "Could not add the favorite.");
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{entryId}")
    public
    @ResponseBody
    Map<String, String> delete(@PathVariable("entryId") final int entryId,
                               final HttpSession session,
                               final HttpServletResponse response) throws Exception {

        if (!Login.isAuthenticated(session)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "The login timed out or is invalid.");
        }

        try {
            final User user = userRepository.findById(Login.currentLoginId(session)).orElse(null);
            final Entry e = entryRepository.findById(entryId).orElse(null);
            final Favorite f = favoriteRepository.findByUserAndEntry(user, e);
            favoriteRepository.delete(f);
            favoriteRepository.flush();

            return java.util.Collections.singletonMap("id", Integer.toString(entryId));
        } catch (final Exception e) {
            log.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "Could not delete the favorite.");
        }
    }
}

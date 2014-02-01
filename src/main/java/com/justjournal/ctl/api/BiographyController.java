/*
 * Copyright (c) 2013 Lucas Holt
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */

package com.justjournal.ctl.api;

import com.justjournal.WebLogin;
import com.justjournal.db.*;
import com.justjournal.utility.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Retrieve and manage user biography content.
 *
 * @author Lucas Holt
 */
@Controller
@RequestMapping("/api/biography")
final public class BiographyController {
    private static final Logger log = Logger.getLogger(BiographyController.class);
    public static final int BIO_MAX_LENGTH = 150;

    private BioDao bioDao = null;

    @Autowired
    public void setBioDao(BioDao bioDao) {
        this.bioDao = bioDao;
    }

    @Cacheable(value = "biography", key = "username")
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    Bio get(@RequestParam String username, HttpServletResponse response) {
        UserTo user = UserDao.get(username);
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        return bioDao.get(user.getId());
    }

    // TODO: API is bad for caching.
    @CacheEvict(value = "biography", allEntries = true)
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public
    @ResponseBody
    Map<String, String> post(@RequestBody String bio, HttpServletResponse response, HttpSession session) {

        int userID = WebLogin.currentLoginId(session);

        if (!StringUtil.lengthCheck(bio, 5, BIO_MAX_LENGTH)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "Biography must be > 5 characters");
        }

        try {
            if (userID > 0) {
                Bio biography = new BioTo();
                biography.setUserId(userID);
                biography.setBio(bio);
                if (bioDao.update(biography))
                    return java.util.Collections.singletonMap("status", "success");

            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return java.util.Collections.singletonMap("error", "Authentication Error");

            }
        } catch (Exception e3) {
            log.error(e3);
        }

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return java.util.Collections.singletonMap("error", "Unable to save biography");
    }

}

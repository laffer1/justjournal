/*
Copyright (c) 2005, 2008 Lucas Holt
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

package com.justjournal.db;

import com.sun.istack.internal.NotNull;
import org.apache.cayenne.Cayenne;
import org.apache.cayenne.CayenneRuntimeException;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.access.DataContext;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Manage biography for users
 */
@Component
public final class BioDao {
    private static final Logger log = Logger.getLogger(BioDao.class.getName());

    public static boolean add(BioTo bioTo) {
          try {
            ObjectContext dataContext = DataContext.getThreadObjectContext();
            com.justjournal.model.UserBio bio = dataContext.newObject(com.justjournal.model.UserBio.class);

              bio.setContent(bioTo.getBio());

            dataContext.commitChanges();
        } catch (CayenneRuntimeException ce) {
            log.error(ce);
            return true;
        }

       return false;
    }

    public static boolean update(BioTo bioTo) {
          try {
            ObjectContext dataContext = DataContext.getThreadObjectContext();

            com.justjournal.model.UserBio bio =
                    Cayenne.objectForPK(dataContext, com.justjournal.model.UserBio.class, bioTo.getUserId());
            bio.setContent(bioTo.getBio());
            dataContext.commitChanges();
        } catch (CayenneRuntimeException ce) {
            log.error(ce);
            return true;
        }
        return false;
    }

    public static boolean delete(int userId) {
        try {
            ObjectContext dataContext = DataContext.getThreadObjectContext();

            com.justjournal.model.UserBio bio =
                    Cayenne.objectForPK(dataContext, com.justjournal.model.UserBio.class, userId);
            dataContext.deleteObjects(bio);
            dataContext.commitChanges();
        } catch (CayenneRuntimeException ce) {
            log.error(ce);
            return true;
        }
        return false;
    }

    @NotNull
    public static BioTo get(int userId) {
        BioTo bioto = new BioTo();

        try {
            ObjectContext dataContext = DataContext.getThreadObjectContext();

            com.justjournal.model.UserBio bio =
                    Cayenne.objectForPK(dataContext, com.justjournal.model.UserBio.class, userId);
            bioto.setUserId(userId);
            bioto.setBio(bio.getContent());
        } catch (Exception e1) {
            log.error(e1);
        }

        return bioto;
    }
}

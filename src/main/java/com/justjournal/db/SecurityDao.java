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

package com.justjournal.db;

import com.justjournal.model.EntrySecurity;
import com.sun.istack.internal.NotNull;
import org.apache.cayenne.Cayenne;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.query.SelectQuery;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Entry Security
 */
public final class SecurityDao {

    private static final Logger log = Logger.getLogger(SecurityDao.class);

    @NotNull
    public static SecurityTo get(int id) {
        SecurityTo securityTo = new SecurityTo();

        try {
            ObjectContext dataContext = DataContext.getThreadObjectContext();

            com.justjournal.model.EntrySecurity item =
                    Cayenne.objectForPK(dataContext, EntrySecurity.class, id);

            securityTo.setId(id);
            securityTo.setName(item.getTitle());
        } catch (Exception e1) {
            log.error(e1);
        }

        return securityTo;
    }

    @NotNull
    public static Collection<SecurityTo> list() {
        Collection<SecurityTo> security = new ArrayList<SecurityTo>(4);
        SecurityTo sec;

        try {
            ObjectContext dataContext = DataContext.getThreadObjectContext();

            final SelectQuery query = new SelectQuery(EntrySecurity.class);
            List<EntrySecurity> securityList = dataContext.performQuery(query);

            for (EntrySecurity entrySecurity : securityList) {
                sec = new SecurityTo();

                sec.setId(Cayenne.intPKForObject(entrySecurity));
                sec.setName(entrySecurity.getTitle());

                security.add(sec);
            }
        } catch (Exception e) {
            log.error(e);
        }

        return security;
    }
}

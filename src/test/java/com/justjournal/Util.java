/*
 * Copyright (c) 2014 Lucas Holt
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

package com.justjournal;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.configuration.server.ServerRuntime;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author Lucas Holt
 */
public class Util {
    public static void setupDb() {
        try {
                   // Create initial context
                   System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                           "org.apache.naming.java.javaURLContextFactory");
                   System.setProperty(Context.URL_PKG_PREFIXES,
                           "org.apache.naming");
                   InitialContext ic = new InitialContext();

                   ic.createSubcontext("java:");
                   ic.createSubcontext("java:comp");
                   ic.createSubcontext("java:comp/env");
                   ic.createSubcontext("java:comp/env/jdbc");

                   // Construct DataSource
                   MysqlConnectionPoolDataSource ds = new MysqlConnectionPoolDataSource();
                   ds.setURL("jdbc:mysql://ds9.midnightbsd.org:3306/jj");
                   ds.setUser("jj");
                   ds.setPassword("");

                   ic.bind("java:comp/env/jdbc/jjDB", ds);

                   ServerRuntime cayenneRuntime = new ServerRuntime("cayenne-JustJournalDomain.xml");
                   DataContext.bindThreadObjectContext(cayenneRuntime.getContext());
               } catch (NamingException ex) {
                   System.err.println(ex.getMessage());
               }
    }
}

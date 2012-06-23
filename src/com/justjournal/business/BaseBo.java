/*
 * Copyright (c) 2011 Lucas Holt
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

package com.justjournal.business;

import java.util.List;

import org.apache.cayenne.BaseContext;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.log4j.Logger;


/**
 * @author Lucas Holt
 */
public abstract class BaseBo {
    protected Logger logger = Logger.getLogger(BaseBo.class);
    /**
     * Cayenne Data context
     */
    protected ObjectContext dataContext;

    public BaseBo() {
        try {
            dataContext = (DataContext) BaseContext.getThreadObjectContext();
        } catch (Exception e) {
            getLogger().debug("Creating new cayenne context instance");
            ServerRuntime cayenneRuntime = new ServerRuntime("cayenne-JustJournalDomain.xml");
            dataContext = cayenneRuntime.getContext();

            BaseContext.bindThreadObjectContext(dataContext);
        }
    }

    /**
     * Get a log4j logger for this class instance.
     *
     * @return logger
     */
    public Logger getLogger() {
        if (logger == null) {
            logger = Logger.getLogger(getClass());
        }
        return logger;
    }

    /**
     * List all the items that the model primarily represents.
     *
     * @return list of model items
     * @throws
     */
    abstract public List list() throws BoException;


}

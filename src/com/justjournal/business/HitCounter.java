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

import com.justjournal.model.Hitcount;
import org.apache.cayenne.CayenneRuntimeException;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;
import org.apache.cayenne.query.SortOrder;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.List;

/**
 * Count access attempts to a resource
 *
 * @author Lucas Holt
 * @version $Id: HitCounter.java,v 1.1 2012/06/23 18:15:32 laffer1 Exp $
 */
public class HitCounter extends BaseBo {
    private static final Logger log = Logger.getLogger(HitCounter.class);

    public HitCounter() {
        getLogger();
    }

    public List<Hitcount> list() throws BoException {
        try {
            final SelectQuery query = new SelectQuery(Hitcount.class);
            query.addOrdering("resource", SortOrder.ASCENDING);
            return dataContext.performQuery(query);
        } catch (CayenneRuntimeException ce) {
            logger.error("Could not list hit count: " + ce.getMessage());
            throw new BoException("Could not list hit count");
        }
    }

    /**
     * Lookup hit count for resource
     * @param resource the resource to view
     * @return hitcount
     * @throws BoException
     */
    public Hitcount lookup(String resource) throws BoException {
        Hitcount hitcount = null;

        if (resource == null || resource.length() > 200 || resource.length() < 1) {
            throw new IllegalArgumentException("Invalid resource");
        }

        try {
            Expression qualifier = Expression.fromString("resource = $res");
            qualifier = qualifier.expWithParameters(Collections.singletonMap("res", resource));
            final SelectQuery query = new SelectQuery(Hitcount.class, qualifier);
            @SuppressWarnings("unchecked")
            final List<Hitcount> hit = dataContext.performQuery(query);
            if (!hit.isEmpty()) {
                hitcount = hit.get(0);
            }
        } catch (CayenneRuntimeException ce) {
            logger.error("Unable to lookup hitcount " + resource + ", " + ce.getMessage());
            throw new BoException("An error occured while looking up the resource.");
        }

        return hitcount;
    }


    /**
     * Create a resource record
     *
     * @param resource url
     * @throws Exception Database access or creation error
     */
    public void create(String resource) throws Exception {
        try {
            com.justjournal.model.Hitcount hitcount = dataContext.newObject(Hitcount.class);
            hitcount.setCount(1);
            hitcount.setResource(resource);
            dataContext.commitChanges();
        } catch (CayenneRuntimeException e) {
            log.error("create(): resource: " + resource + " exception: " + e.getMessage());
            dataContext.rollbackChanges();
            throw new BoException("Could not create resource: " + resource);
        }
    }

    /**
     * Update a resource record
     *
     * @param resource url
     * @param count    counter value to save
     * @throws Exception database access error
     */
    public void edit(String resource, int count) throws Exception {
        try {
            if (count > 0) {
                Hitcount hit = lookup(resource);
                hit.setCount(count);
                dataContext.commitChanges();
            }
        } catch (CayenneRuntimeException e) {
            log.error("update(): resource: " + resource + " exception: " + e.getMessage());
            dataContext.rollbackChanges();
            throw new BoException("Could not update hitcount resource : " + resource);
        }
    }

    /**
     * Log a hit
     *
     * @param resource url
     * @return New value of the counter (assumes it worked)
     */
    public int increment(String resource) {
        int count = count(resource);

        try {
            // probably not in the database yet.
            if (count < 0)
                create(resource);
            else
                edit(resource, count + 1);
        } catch (Exception e) {
            log.error("increment(): " + e.getMessage());
        }

        return count + 1;
    }

    /**
     * Retrieve the counter value
     *
     * @param resource url
     * @return current counter value
     */
    public int count(String resource) {
        int count = -1;

        try {
            Hitcount hit = lookup(resource);
            if (hit != null)
                count = hit.getCount();
        } catch (Exception e) {
            log.error("count(): " + e.getMessage());
        }

        return count;
    }

}

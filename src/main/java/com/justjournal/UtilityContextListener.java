/*
Copyright (c) 2009 Lucas Holt
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

package com.justjournal;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.beans.Introspector;

final public class UtilityContextListener implements ServletContextListener {

    private static Logger log = Logger.getLogger(UtilityContextListener.class);

    public void contextInitialized(final ServletContextEvent sce) {
        log.info("Context Initialized");
    }

    public void contextDestroyed(final ServletContextEvent sce) {
        log.info("Context Destruction started");

        log.info("unloading drivers for classLoader: [" +
                Integer.toHexString(getClass().getClassLoader().hashCode()) + "]");
        try {
            for (java.util.Enumeration e = java.sql.DriverManager.getDrivers();
                 e.hasMoreElements();) {
                final java.sql.Driver driver = (java.sql.Driver) e.nextElement();
                if (driver.getClass().getClassLoader() ==
                        getClass().getClassLoader()) {
                    java.sql.DriverManager.deregisterDriver(driver);
                    log.info(driver.getClass().getName() + "unloaded.");
                } else
                    log.info(driver.getClass().getName() + " not unloaded.");
            }
        } catch (Throwable e) {
            log.error("Failed to cleanup ClassLoader for webapp");
            e.printStackTrace();
        }

        // unload log4j
        if (LogManager.class.getClassLoader()
                .equals(this.getClass().getClassLoader())) {
            log.info("Log4j was loaded by application classloader, shutting down.");
            org.apache.commons.logging.LogFactory.release(Thread.currentThread().getContextClassLoader());
            LogManager.shutdown();
            Introspector.flushCaches();
        }
    }
}            

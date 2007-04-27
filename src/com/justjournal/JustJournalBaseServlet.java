/*
Copyright (c) 2005-2006, Lucas Holt
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

import com.justjournal.core.Settings;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Base servlet to do some of the repetative servlet initialization stuff.
 *
 * @author Lucas Holt
 * @version $Id: JustJournalBaseServlet.java,v 1.11 2007/04/27 06:22:36 laffer1 Exp $
 * @since 1.0
 *        Date: Sep 25, 2005
 *        Time: 9:04:00 PM
 */
public class JustJournalBaseServlet extends HttpServlet {
    protected static final char endl = '\n';
    protected Settings set;

    /**
     * Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext ctx = config.getServletContext();
        set = Settings.getSettings(ctx);
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws java.io.IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws java.io.IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws java.io.IOException {
        String contentType = "text/html; charset=utf-8";
        final StringBuffer sb = new StringBuffer();
        final HttpSession session = request.getSession(true);

        response.setContentType(contentType);
        response.setBufferSize(8192);
        response.setDateHeader("Expires", System.currentTimeMillis());
        response.setDateHeader("Last-Modified", System.currentTimeMillis());
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");

        execute(request, response, session, sb);

        response.setContentLength(sb.length());
        final ServletOutputStream outstream = response.getOutputStream();
        outstream.print(sb.toString());
        outstream.flush();
        outstream.close();
    }

    public long getLastModified(HttpServletRequest request) {
        return new java.util.Date().getTime() / 1000 * 1000;
    }

    protected void execute(HttpServletRequest request, HttpServletResponse response, HttpSession session, StringBuffer sb) {

    }

    protected String fixInput(HttpServletRequest request, String input) {
        String fixed = request.getParameter(input);

        if (fixed == null)
            fixed = "";

        return fixed.trim();
    }

    protected String fixHeaderInput(HttpServletRequest request, String input) {
        String fixed = request.getHeader(input);

        if (fixed == null)
            fixed = "";
        return fixed.trim();
    }
}

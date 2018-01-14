/*
Copyright (c) 2005-2009 Lucas Holt
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

package com.justjournal.ctl;

import com.justjournal.core.Settings;
import com.justjournal.utility.ETag;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Base servlet to do some of the repetative servlet initialization stuff.
 * <p/>
 * Date: Sep 25, 2005 Time: 9:04:00 PM
 *
 * @author Lucas Holt
 * @version $Id: JustJournalBaseServlet.java,v 1.17 2009/07/11 02:03:43 laffer1 Exp $
 * @since 1.0
 */
public class JustJournalBaseServlet extends HttpServlet {
    protected static final char endl = '\n';  /* end of line character for output */
    public static final int BUFFER_SIZE = 8192;

    @Autowired
    protected Settings set;

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws java.io.IOException {
        processRequest(request, response, false);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws java.io.IOException {
        processRequest(request, response, false);
    }

    @Override
    protected void doHead(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        processRequest(httpServletRequest, httpServletResponse, true);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response, boolean head)
            throws java.io.IOException {
        final String contentType = "text/html; charset=utf-8";
        final StringBuffer sb = new StringBuffer(512);
        final HttpSession session = request.getSession(true);

        response.setContentType(contentType);
        response.setBufferSize(BUFFER_SIZE);
        response.setDateHeader("Expires", System.currentTimeMillis());
        response.setDateHeader("Last-Modified", System.currentTimeMillis());
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");

        execute(request, response, session, sb);

        /* create etag */
        ETag etag = new ETag(response);
        etag.writeFromString(sb.toString());

        response.setContentLength(sb.length());

        if (head) {
            response.flushBuffer();
        } else {
            final ServletOutputStream outstream = response.getOutputStream();
            outstream.print(sb.toString());
            outstream.flush();
            outstream.close();
        }
    }

    @Override
    public long getLastModified(HttpServletRequest request) {
        return new java.util.Date().getTime() / 1000 * 1000;
    }

    protected void execute(HttpServletRequest request, HttpServletResponse response, HttpSession session, StringBuffer sb) {

    }

    /**
     * Get a string input parameter guaranteed not to be null
     *
     * @param request Servlet Request
     * @param input   Name of the parameter
     * @return Trimmed, Not null string from parameter
     */
    protected String fixInput(ServletRequest request, String input) {
        String fixed = request.getParameter(input);

        if (fixed == null)
            fixed = "";

        return fixed.trim();
    }

    /**
     * Get a string header guaranteed not to be null
     *
     * @param request Servlet Request
     * @param input   Name of the header
     * @return Trimmed, Not null string from header
     */
    protected String fixHeaderInput(HttpServletRequest request, String input) {
        String fixed = request.getHeader(input);

        if (fixed == null)
            fixed = "";
        return fixed.trim();
    }
}

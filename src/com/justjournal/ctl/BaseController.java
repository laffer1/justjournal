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

package com.justjournal.ctl;

import com.justjournal.db.ContentDao;
import com.justjournal.db.ContentTo;
import com.justjournal.db.ResourceTo;
import com.justjournal.db.ResourcesDAO;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * User: laffer1
 * Date: Jul 21, 2005
 * Time: 7:12:44 PM
 */
public class BaseController extends HttpServlet {
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void destroy() {
    }


    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException
     * @throws java.io.IOException
     */
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, java.io.IOException {

        String path = getPath(request);
        ResourcesDAO resdata = new ResourcesDAO();

        ResourceTo res = resdata.view(path);

        if (res == null) {
            // return 404
            response.sendError(HttpServletResponse.SC_NOT_FOUND, path);

        } else {
            if (res.getActive() == false) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, path);
            } else {

                ContentDao con = new ContentDao();
                ArrayList contents = con.view(res.getId());

                final Iterator itr = contents.iterator();
                ContentTo o = null;

                for (int i = 0, n = contents.size(); i < n; i++) {
                    o = (ContentTo) itr.next();

                    if (o.getPreferred() == true)
                        break;

                }
                if (o != null) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType(o.getMimeType());
                    response.setDateHeader("Expires", System.currentTimeMillis());
                    response.setDateHeader("Last-Modified", System.currentTimeMillis());

                    final ServletOutputStream outstream = response.getOutputStream();
                    outstream.write(o.getData());
                    outstream.flush();
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, path);
                }

                // TODO: add security
                // PUBLIC = 2, PRIVATE = 0, FRIENDS = 1
                if (res.getSecurityLevel() == 2) {

                } else if (res.getSecurityLevel() == 1) {

                } else {
                    // private add forbidden message? or user handling?

                }
            }
        }

    }

    /**
     * Retrieves the path from the request url.
     * Based on code from Maverick Dispatch.java
     *
     * @param request
     * @return url path
     */
    protected String getPath(HttpServletRequest request) {
        // If we are include()ed from a RequestDispatcher, the real request
        // path will be obtained from this special attribute.  If we are
        // produced by a forward() or a normal request, we can use the
        // getServletPath() method.  See section 8.3 of the Servlet 2.3 API.
        String path = (String) request.getAttribute("javax.servlet.include.servlet_path");
        if (path == null)
            path = request.getServletPath();

        int firstChar = 0;
        if (path.startsWith("/"))
            firstChar = 1;

        //  lastIndexOf returns -1 if the substring is not found
        //  if the period is found, we need to remove the
        //  file extension.  If its not, we have a mapping
        //  that doesn't use file extensions.
        //  TODO: Add a way to know if non extension mapping
        //  is on.  This could be a problem otherwise.

        int period = path.lastIndexOf(".");

        if (period > -1)
            path = path.substring(firstChar, period);
        else
            path = path.substring(firstChar, path.length());

        return path;
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return description string
     */
    public String getServletInfo() {
        return "Just Journal Base Controller";
    }
}

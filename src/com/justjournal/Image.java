package com.justjournal;

import org.apache.log4j.Category;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;

import sun.jdbc.rowset.CachedRowSet;

import java.io.BufferedInputStream;

/**
 * Image viewer servlet to display userpics and other images
 * from the database.
 *
 * User: laffer1
 * Date: Nov 22, 2005
 * Time: 9:31:28 PM
 */
public class Image extends HttpServlet {

    private static Category log = Category.getInstance(Image.class.getName());

       /**
     * Initializes the servlet.
     *
     * @param config
     * @throws javax.servlet.ServletException
     */
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
    }
        // processes get requests
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws java.io.IOException {

          String id = request.getParameter("id");

          if (id == null) {
              response.sendError(HttpServletResponse.SC_NOT_FOUND);
              return;
          }

          try {
              response.reset();
              CachedRowSet rs = SQLHelper.executeResultSet("call getimage(" + id + ");");
              if (rs.next())
              {
                  response.setContentType(rs.getString("mimetype").trim());
                  final ServletOutputStream outstream = response.getOutputStream();
                  BufferedInputStream img = new BufferedInputStream(rs.getBinaryStream("image"));
                  byte[] buf = new byte[4*1024]; // 4k buffer
                  int len;
                  while ((len = img.read(buf,0,buf.length)) != -1)
                      outstream.write(buf, 0, len);

                  outstream.flush();
                  outstream.close();
              }
              else
                  response.sendError(HttpServletResponse.SC_NOT_FOUND);

              rs.close();
          } catch (Exception e) {
               log.debug("Could not load image: " + e.toString());
        }
    }
}

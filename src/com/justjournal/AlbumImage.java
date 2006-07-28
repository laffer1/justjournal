package com.justjournal;

import com.justjournal.db.SQLHelper;
import org.apache.log4j.Category;
import sun.jdbc.rowset.CachedRowSet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;

/**
 * Diplay individual images in the user's photo album.
 *
 * @author Lucas Holt
 * @version $Id: AlbumImage.java,v 1.2 2006/07/28 14:01:06 laffer1 Exp $
 */
public class AlbumImage extends HttpServlet {
    private static Category log = Category.getInstance(AlbumImage.class.getName());

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
            CachedRowSet rs = SQLHelper.executeResultSet("call getalbumimage(" + id + ");");
            if (rs.next()) {
                response.setContentType(rs.getString("mimetype").trim());
                final ServletOutputStream outstream = response.getOutputStream();
                BufferedInputStream img = new BufferedInputStream(rs.getBinaryStream("image"));
                byte[] buf = new byte[4 * 1024]; // 4k buffer
                int len;
                while ((len = img.read(buf, 0, buf.length)) != -1)
                    outstream.write(buf, 0, len);

                outstream.flush();
                outstream.close();
            } else
                response.sendError(HttpServletResponse.SC_NOT_FOUND);

            rs.close();
        } catch (Exception e) {
            log.debug("Could not load image: " + e.toString());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}

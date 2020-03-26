package com.justjournal.ctl;

import com.justjournal.model.User;
import com.justjournal.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * An implementation of Really Simple Discovery (RSD).
 * <p/>
 * A list of points that blogging clients can use to post entries by "autodiscovery".  This is used by blog clients such
 * as Microsoft's Live Writer.
 * <p/>
 * http://cyber.law.harvard.edu/blogs/gems/tech/rsd.html
 *
 * @author Lucas Holt
 * @version $Id: Rsd.java,v 1.5 2009/05/16 03:13:12 laffer1 Exp $
 *          <p/>
 *          User: laffer1 Date: Apr 26, 2008 Time: 10:22:20 AM
 */
@Slf4j
@Controller
@RequestMapping("/rsd")
public class RsdController {
    
    private static final String XML_HEADER =
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
    private static final String RSD_HEADER =
            "<rsd xmlns=\"http://archipelago.phrasewise.com/rsd\" version=\"1.0\">\n";
    private static final String RSD_FOOTER =
            "</rsd>\n";

    @Autowired
    private UserRepository userRepository;

    @GetMapping(produces = "application/rsd+xml")
    @ResponseBody
    public String get(HttpServletRequest request, HttpServletResponse response) {
        final StringBuilder sb = new StringBuilder();
        try {
            response.setContentType("application/rsd+xml; charset=utf-8");

            final String blogID = request.getParameter("blogID");
            if (blogID == null || blogID.length() < 2) {
                throw new IllegalArgumentException("Missing required parameter \"blogID\"");
            }

            final User user = userRepository.findByUsername(blogID);

            sb.append(XML_HEADER);
            sb.append(RSD_HEADER);
            sb.append("<service>\n");
            sb.append("\t<engineName>JustJournal</engineName>\n");
            sb.append("\t<engineLink>http://www.justjournal.com</engineLink>\n");
            sb.append("\t<homePageLink>http://www.justjournal.com/users/");
            sb.append(user.getUsername()); // yeah we already know this but it's for output and thus safer.
            sb.append("</homePageLink>\n");
            // APIS we support.
            sb.append("\t<apis>\n");
            sb.append("\t\t<api name=\"Blogger\" preferred=\"false\" apiLink=\"http://www.justjournal.com/xml-rpc\" blogID=\"");
            sb.append(user.getUsername());
            sb.append("\" />\n");
            sb.append("\t\t<api name=\"MetaWeblog\" preferred=\"true\" apiLink=\"http://www.justjournal.com/xml-rpc\" blogID=\"");
            sb.append(user.getUsername());
            sb.append("\" />\n");
            sb.append("\t</apis>\n");
            sb.append("</service>\n");
            sb.append(RSD_FOOTER);

        } catch (final Exception e) {
            log.error("Error generating RSD", e);
            sb.delete(0, sb.length() - 1);
            sb.append(XML_HEADER);
            sb.append(RSD_HEADER);
            sb.append(RSD_FOOTER);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        return sb.toString();
    }

}

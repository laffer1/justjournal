package com.justjournal.ctl.users.entry;

import com.justjournal.model.Entry;
import com.justjournal.model.FormatType;
import com.justjournal.model.User;
import com.justjournal.repository.UserRepository;
import com.justjournal.services.EntryService;
import com.justjournal.services.MarkdownService;
import com.justjournal.services.ServiceException;
import com.justjournal.utility.DateConvert;
import com.justjournal.utility.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;


/**
 * @author Lucas Holt
 */
@Transactional
@Controller
public class Amp {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntryService entryService;

    @Autowired
    private MarkdownService markdownService;

    @RequestMapping(value = "/users/{username}/entry/{id}/amp", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String ampEntry(@PathVariable("username") final String username,
                           @PathVariable("id") final int id,
                           final HttpSession session, final HttpServletResponse response) {

        final User user = userRepository.findByUsername(username);

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "";
        }

        if (user.getJournals().get(0).isOwnerViewOnly()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "";
        }


        try {
            final Entry entry = entryService.getPublicEntry(id, username);
            if (entry == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return "";
            }

            return createAmpPage(user, entry);
        } catch (final ServiceException se) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return "";
        }
    }

    private String createAmpPage(final User user, final Entry entry) {
        final StringBuilder sb = new StringBuilder();

        sb.append("<!doctype html>");
        sb.append("<html amp>");
        sb.append("<head>");
        sb.append("<meta charset=\"utf-8\">");
        sb.append("<script async src=\"https://cdn.ampproject.org/v0.js\"></script>");
        sb.append("<title>" + entry.getSubject() + "</title>");
        sb.append("<link rel=\"canonical\"  href=\"https://www.justjournal.com/users/" + user.getUsername() + "/entry/" + entry.getId() + "\">");
        sb.append("<meta name=\"viewport\" content=\"width=device-width,minimum-scale=1,initial-scale=1\">");
            /*   <script type="application/ld+json">
                 {
                   "@context": "http://schema.org",
                   "@type": "NewsArticle",
                   "headline": "Open-source framework for publishing content",
                   "datePublished": "2015-10-07T12:02:41Z",
                   "image": [
                     "logo.jpg"
                   ]
                 }
               </script>  */
        sb.append("<style amp-boilerplate>body{-webkit-animation:-amp-start 8s steps(1,end) 0s 1 normal both;-moz-animation:-amp-start 8s steps(1,end) 0s 1 normal both;-ms-animation:-amp-start 8s steps(1,end) 0s 1 normal both;animation:-amp-start 8s steps(1,end) 0s 1 normal both}@-webkit-keyframes -amp-start{from{visibility:hidden}to{visibility:visible}}@-moz-keyframes -amp-start{from{visibility:hidden}to{visibility:visible}}@-ms-keyframes -amp-start{from{visibility:hidden}to{visibility:visible}}@-o-keyframes -amp-start{from{visibility:hidden}to{visibility:visible}}@keyframes -amp-start{from{visibility:hidden}to{visibility:visible}}</style><noscript><style amp-boilerplate>body{-webkit-animation:none;-moz-animation:none;-ms-animation:none;animation:none}</style></noscript>");

        sb.append("<style amp-custom>body { background-color: white; margin: 16px; } h1 { color: navy } </style>");
        sb.append("   </head>");
        sb.append("  <body>");

        sb.append("<h1>" + entry.getSubject() + "</h1>");

        final SimpleDateFormat formatmydate = new SimpleDateFormat("EEE, d MMM yyyy");

        final String curDate = formatmydate.format(entry.getDate());

        sb.append("<h2 itemprop=\"author\" itemscope itemtype=\"https://schema.org/Person\"><span itemprop=\"name\">");
        sb.append(user.getUsername()); // TODO: real name?
        sb.append("</span></h2>");

        sb.append("<time itemprop=\"datePublished\" datetime=\"");
        sb.append(DateConvert.encode8601(entry.getDate()));
        sb.append("\">");
                sb.append(curDate);
                sb.append("</time>");

        sb.append("<div>");
        if (entry.getFormat().equals(FormatType.TEXT)) {

            if (entry.getBody() != null) {
                sb.append("\t\t\t\t<p>");
                if (entry.getBody().contains("\n"))
                    sb.append(StringUtil.replace(entry.getBody(), '\n', "<br>"));
                else if (entry.getBody().contains("\r"))
                    sb.append(StringUtil.replace(entry.getBody(), '\r', "<br>"));
                else
                    // we do not have any "new lines" but it might be
                    // one long line.
                    sb.append(entry.getBody());

                sb.append("</p>");
            }
        } else if (entry.getFormat().equals(FormatType.MARKDOWN))
            sb.append(markdownService.convertToHtml(entry.getBody()));
        else
            sb.append(entry.getBody());
        sb.append("</div>");

        sb.append(" </body></html>");

        return sb.toString();
    }
}

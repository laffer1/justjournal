package com.justjournal.ctl.api;

import com.justjournal.Login;
import com.justjournal.model.Journal;
import com.justjournal.repository.JournalRepository;
import com.justjournal.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.Map;

/**
 * Manage individual journals
 *
 * @author Lucas Holt
 */
@RestController("/api/journal")
public class JournalController {
    private static final Logger log = Logger.getLogger(JournalController.class.getName());

    private JournalRepository journalRepository;
    private UserRepository userRepository;

    @Autowired
    public JournalController(final JournalRepository journalRepository, final UserRepository userRepository) {
        this.journalRepository = journalRepository;
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "{slug}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Journal get(@PathVariable("slug") final String slug) {
        return journalRepository.findOneBySlug(slug);
    }

    @RequestMapping(value = "{slug}", method = RequestMethod.PUT)
    public
    @ResponseBody
    Map<String, String> put(@PathVariable("slug") final String slug,
                            @RequestBody Journal journal,
                            final HttpSession session, final HttpServletResponse response) {

        if (!Login.isAuthenticated(session)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "The login timed out or is invalid.");
        }

        try {
            Journal j = journalRepository.findOneBySlug(slug);
            if (j == null) {
                journal.setUser(userRepository.findOne(Login.currentLoginId(session)));
                journal.setSince(Calendar.getInstance().getTime());
                journal.setModified(Calendar.getInstance().getTime());
                journal = journalRepository.saveAndFlush(journal);
                response.setStatus(HttpServletResponse.SC_CREATED);
                return java.util.Collections.singletonMap("slug", journal.getSlug());
            }

            if (j.getUser().getId() != Login.currentLoginId(session)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return java.util.Collections.singletonMap("error", "You do not have permission to update this journal.");
            }

            j.setAllowSpider(journal.isAllowSpider());
            j.setName(journal.getName());
            j.setOwnerViewOnly(journal.isOwnerViewOnly());
            j.setPingServices(journal.isPingServices());
            j.setStyle(journal.getStyle());
            j.setModified(Calendar.getInstance().getTime());

            j = journalRepository.saveAndFlush(j);
            if (j != null)
                return java.util.Collections.singletonMap("slug", j.getSlug());

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "Error adding journal.");

        } catch (final Exception e) {
            log.error(e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "Error adding journal.");
        }
    }

    @RequestMapping(value = "{slug}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    Map<String, String> delete(@PathVariable("slug") final String slug,
                               final HttpSession session,
                               final HttpServletResponse response) throws Exception {


        if (!Login.isAuthenticated(session)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "The login timed out or is invalid.");
        }

        final Journal journal = journalRepository.findOneBySlug(slug);
        if (slug != null) {
            if (journal.getUser().getId() != Login.currentLoginId(session)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return java.util.Collections.singletonMap("error", "You do not have permission to delete this journal.");
            }

            journalRepository.delete(journal);
            journalRepository.flush();
            return java.util.Collections.singletonMap("slug", journal.getSlug());
        }

        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return java.util.Collections.singletonMap("error", "Error deleting your journal. Bad slug");
    }

}

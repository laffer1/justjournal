package com.justjournal.ctl.api;

import com.justjournal.Login;
import com.justjournal.model.Journal;
import com.justjournal.model.Style;
import com.justjournal.repository.JournalRepository;
import com.justjournal.repository.UserRepository;
import com.justjournal.services.StyleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Manage individual journals
 *
 * @author Lucas Holt
 */
@Slf4j
@RestController
@RequestMapping("/api/journal")
public class JournalController {

    private JournalRepository journalRepository;
    private UserRepository userRepository;
    private StyleService styleService;

    @Autowired
    public JournalController(final JournalRepository journalRepository, final UserRepository userRepository, final StyleService styleService) {
        this.journalRepository = journalRepository;
        this.userRepository = userRepository;
        this.styleService = styleService;
    }

    @RequestMapping(value = "user/{username}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<List<Journal>> listByUser(@PathVariable("username") final String username) {
        try {
            return ResponseEntity.ok(journalRepository.findByUsername(username));
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(value = "{slug}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<Journal> get(@PathVariable("slug") final String slug) {
        try {
            return ResponseEntity.ok(journalRepository.findOneBySlug(slug));
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, String> post(@RequestBody Journal journal, final HttpSession session, final HttpServletResponse response) {
        return put(journal.getSlug(), journal, session, response);
    }


    @RequestMapping(value = "{slug}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, String> put(@PathVariable("slug") final String slug,
                                   @RequestBody Journal journal,
                                   final HttpSession session, final HttpServletResponse response) {

        if (!Login.isAuthenticated(session)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "The login timed out or is invalid.");
        }

        try {
            Journal j = journalRepository.findOneBySlug(slug);
            if (j == null) {
                journal.setUser(userRepository.findById(Login.currentLoginId(session)).orElse(null));
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
            if (journal.getStyleId() > 0) {
                final Style s = styleService.get(journal.getStyleId());
                j.setStyle(s);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return java.util.Collections.singletonMap("error", "Missing style id.");
            }
            j.setModified(Calendar.getInstance().getTime());

            j = journalRepository.saveAndFlush(j);
            if (j != null)
                return java.util.Collections.singletonMap("slug", j.getSlug());

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "Error adding journal.");

        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return java.util.Collections.singletonMap("error", "Error adding journal.");
        }
    }

    @RequestMapping(value = "{slug}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, String> delete(@PathVariable("slug") final String slug,
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

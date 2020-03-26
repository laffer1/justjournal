package com.justjournal.ctl.api;

import com.justjournal.Login;
import com.justjournal.core.Constants;
import com.justjournal.ctl.error.ErrorHandler;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    public JournalController(final JournalRepository journalRepository, final UserRepository userRepository,
                             final StyleService styleService) {
        this.journalRepository = journalRepository;
        this.userRepository = userRepository;
        this.styleService = styleService;
    }

    @GetMapping(value = "user/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Journal>> listByUser(@PathVariable(Constants.PARAM_USERNAME) final String username) {
        try {
            return ResponseEntity.ok(journalRepository.findByUsername(username));
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(value = "{slug}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Journal> get(@PathVariable("slug") final String slug) {
        try {
            return ResponseEntity.ok(journalRepository.findOneBySlug(slug));
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, String> post(@RequestBody final Journal journal,
                                    final HttpSession session, final HttpServletResponse response) {
        return put(journal.getSlug(), journal, session, response);
    }


    @PutMapping(value = "{slug}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, String> put(@PathVariable("slug") final String slug,
                                   @RequestBody Journal journal,
                                   final HttpSession session, final HttpServletResponse response) {

        if (!Login.isAuthenticated(session)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ErrorHandler.modelError(Constants.ERR_INVALID_LOGIN);
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
                return ErrorHandler.modelError(  "You do not have permission to update this journal.");
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
                return ErrorHandler.modelError(  "Missing style id.");
            }
            
            j.setModified(Calendar.getInstance().getTime());
            j = journalRepository.saveAndFlush(j);

            return java.util.Collections.singletonMap("slug", j.getSlug());
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ErrorHandler.modelError(  "Error adding journal.");
        }
    }

    @DeleteMapping(value = "{slug}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, String> delete(@PathVariable("slug") final String slug,
                                      final HttpSession session,
                                      final HttpServletResponse response) {


        if (!Login.isAuthenticated(session)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ErrorHandler.modelError(Constants.ERR_INVALID_LOGIN);
        }

        final Journal journal = journalRepository.findOneBySlug(slug);
        if (slug != null) {
            if (journal.getUser().getId() != Login.currentLoginId(session)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return ErrorHandler.modelError(  "You do not have permission to delete this journal.");
            }

            journalRepository.delete(journal);
            journalRepository.flush();
            return java.util.Collections.singletonMap("slug", journal.getSlug());
        }

        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return ErrorHandler.modelError(  "Error deleting your journal. Bad slug");
    }

}

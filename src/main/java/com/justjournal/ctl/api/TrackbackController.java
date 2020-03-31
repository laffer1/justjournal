package com.justjournal.ctl.api;

import com.justjournal.Login;
import com.justjournal.core.Constants;
import com.justjournal.ctl.error.ErrorHandler;
import com.justjournal.model.Entry;
import com.justjournal.model.PrefBool;
import com.justjournal.model.api.TrackbackTo;
import com.justjournal.repository.EntryRepository;
import com.justjournal.services.TrackbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.justjournal.core.Constants.PARAM_ID;

/**
 * Return trackback responses for a given entry, similar to comments controller
 *
 * @author Lucas Holt
 */
@Slf4j
@RestController
@RequestMapping("/api/trackback")
public class TrackbackController {
    TrackbackService trackbackService;

    EntryRepository entryRepository;

    @Autowired
    public TrackbackController(EntryRepository entryRepository, TrackbackService trackbackService) {
        this.entryRepository = entryRepository;
        this.trackbackService = trackbackService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TrackbackTo> getTrackbacks(@RequestParam(Constants.PARAM_ENTRY_ID) final Integer entryId,
                                           final HttpServletResponse response) {
        final Entry entry = entryRepository.findById(entryId).orElse(null);

        if (entry == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return Collections.emptyList();
        }

        try {
            if (new ArrayList<>(entry.getUser().getJournals()).get(0).isOwnerViewOnly() ||
                    entry.getAllowComments() == PrefBool.N ||
                    entry.getSecurity().getId() == 0) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        } catch (final Exception e) {
            log.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        return trackbackService.getByEntry(entryId);
    }

    @DeleteMapping(value = "{id}")
    public Map<String, String> delete(@PathVariable(PARAM_ID) final int id, final HttpSession session,
                                      final HttpServletResponse response) {

        if (!Login.isAuthenticated(session)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return ErrorHandler.modelError(Constants.ERR_INVALID_LOGIN);
        }

        try {
            final Optional<TrackbackTo> trackbackTo = trackbackService.getById(id);
            if (!trackbackTo.isPresent()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return ErrorHandler.modelError("Trackback not found.");
            }

            final Entry entry = entryRepository.findById(trackbackTo.get().getEntryId()).orElse(null);
            if (entry == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return ErrorHandler.modelError("Trackback entry not found.");
            }

            if (entry.getUser().getId() == Login.currentLoginId(session))
                trackbackService.deleteById(id);

            return java.util.Collections.singletonMap(PARAM_ID, Integer.toString(id));
        } catch (final Exception e) {
            log.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ErrorHandler.modelError("Could not delete the trackback.");
        }
    }

}

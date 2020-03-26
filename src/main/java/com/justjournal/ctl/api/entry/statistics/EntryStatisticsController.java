package com.justjournal.ctl.api.entry.statistics;

import com.justjournal.model.EntryStatistic;
import com.justjournal.services.EntryStatisticService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import static com.justjournal.core.Constants.PARAM_USERNAME;

/**
 * Get entry statistics
 * @author Lucas Holt
 */
@Slf4j
@RestController
@RequestMapping("/api/entry")
public class EntryStatisticsController {

    @Autowired
    public EntryStatisticService entryStatisticService;

    @GetMapping(value = "{username}/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<EntryStatistic>> getStatistics(@PathVariable(PARAM_USERNAME) final String username) {

        final Iterable<EntryStatistic> myIterator = entryStatisticService.getEntryCounts(username).toIterable();
        final List<EntryStatistic> e = IteratorUtils.toList(myIterator.iterator());
        Collections.sort(e);

        return ResponseEntity
                .ok()
                .eTag(Integer.toString(e.hashCode()))
                .body(e);
    }
}

package com.justjournal.ctl.api;

import com.justjournal.model.Style;
import com.justjournal.services.StyleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Lucas Holt
 */
@RestController
@RequestMapping("/api/styles")
public class StylesController {

    @Autowired
    private StyleService styleService;

    @Cacheable("styles")
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Style>> getStyles() {

        final List<Style> styles = styleService.getStyles();

        return ResponseEntity.ok()
                .eTag(Integer.toString(styles.hashCode()))
                .body(styles);
    }

    @Cacheable(value = "styles", key = "#id")
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Style> getById(@PathVariable("id") final Integer id) {

        final Style style = styleService.get(id);

        if (style == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return ResponseEntity.ok()
                .eTag(Integer.toString(style.hashCode()))
                .body(style);
    }
}

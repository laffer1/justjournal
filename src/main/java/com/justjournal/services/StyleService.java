package com.justjournal.services;

import com.justjournal.model.Style;
import com.justjournal.repository.StyleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Style and theme selection for just journal blogs
 * @author Lucas Holt
 */
@Slf4j
@Service
public class StyleService {

    private static final String DEFAULT_JOURNAL_THEME = "Journal";
    
    private StyleRepository styleRepository;

    @Autowired
    public StyleService(final StyleRepository styleRepository) {
        this.styleRepository = styleRepository;
    }

    public Style getDefaultStyle() {
       return styleRepository.findOneByTitle(DEFAULT_JOURNAL_THEME);
    }

    public List<Style> getStyles() {
        return styleRepository.findAll();
    }

    public Style get(final int id) {
        return styleRepository.findOne(id);
    }
}

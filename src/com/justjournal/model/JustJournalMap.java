package com.justjournal.model;

import com.justjournal.model.auto._JustJournalMap;

public class JustJournalMap extends _JustJournalMap {

    private static JustJournalMap instance;

    private JustJournalMap() {}

    public static JustJournalMap getInstance() {
        if(instance == null) {
            instance = new JustJournalMap();
        }

        return instance;
    }
}

package com.justjournal.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The entry.modified column is NOT NULL, and Hibernate writes the field's value
 * rather than letting the database default apply.
 */
class EntryTest {

    @Test
    void newEntryHasModified() {
        Assertions.assertNotNull(new Entry().getModified());
    }
}

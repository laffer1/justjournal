package com.justjournal.utility;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpellingTests {

    @Test
    void testSpelling() {
        Spelling spelling = new Spelling();
        var result = spelling.checkSpelling("foobar");
        assertEquals("", result); // dict file not available for tests
    }
}

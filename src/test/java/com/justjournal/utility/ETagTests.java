package com.justjournal.utility;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import jakarta.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.mock;

class ETagTests {

    @Test
    void testWriteFromString_null() {
        var resp = mock(HttpServletResponse.class);
        var etag = new ETag(resp);
        Assertions.assertThrows(IllegalArgumentException.class, () -> etag.writeFromString(null));
    }

    @Test
    void testWrite_null() {
        var resp = mock(HttpServletResponse.class);
        var etag = new ETag(resp);
        Assertions.assertThrows(IllegalArgumentException.class, () -> etag.write(null));
    }
}

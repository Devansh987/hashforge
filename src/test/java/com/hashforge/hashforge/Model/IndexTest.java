package com.hashforge.hashforge.Model;

import com.hashforge.hashforge.model.Index;
import com.hashforge.hashforge.model.IndexEntry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IndexTest {

    @Test
    void shouldAddEntry() {

        Index index = new Index();

        IndexEntry entry =
                new IndexEntry("Main.java", "ABC123");

        index.addEntry(entry);

        assertEquals(1, index.getEntries().size());

        assertEquals(
                "Main.java",
                index.getEntries().get(0).getPath()
        );

        assertEquals(
                "ABC123",
                index.getEntries().get(0).getHash()
        );
    }

    @Test
    void shouldUpdateExistingEntry() {

        Index index = new Index();

        index.addEntry(
                new IndexEntry("Main.java", "ABC123")
        );

        index.addEntry(
                new IndexEntry("Main.java", "XYZ789")
        );

        // Same file, so there should still be only one entry
        assertEquals(1, index.getEntries().size());

        // Hash should be updated
        assertEquals(
                "XYZ789",
                index.getEntries().get(0).getHash()
        );
    }

    @Test
    void shouldAddMultipleEntries() {

        Index index = new Index();

        index.addEntry(
                new IndexEntry("Main.java", "ABC123")
        );

        index.addEntry(
                new IndexEntry("User.java", "DEF456")
        );

        assertEquals(2, index.getEntries().size());
    }

    @Test
    void shouldRemoveEntry() {

        Index index = new Index();

        index.addEntry(
                new IndexEntry("Main.java", "ABC123")
        );

        index.addEntry(
                new IndexEntry("User.java", "DEF456")
        );

        index.removeEntry("Main.java");

        assertEquals(1, index.getEntries().size());

        assertEquals(
                "User.java",
                index.getEntries().get(0).getPath()
        );
    }
}
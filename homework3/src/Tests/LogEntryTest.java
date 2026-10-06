package Tests;

import Logger.LogEntry;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNull;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

public class LogEntryTest {

    @Test
    public void testParseValidLogLine(){
        String line = "93.180.71.3 - - [11/Oct/2007:08:05:32 +0000] \"GET /downloads/product_1 HTTP/1.1\" 304 0 \"-\" \"Debian APT-HTTP/1.3\"";

        LogEntry entry = LogEntry.parse(line);


        assertNotNull(entry);
        assertEquals("/downloads/product_1", entry.resource);
        assertEquals(304, entry.status);
        assertEquals(0, entry.size);
        assertNotNull(entry.times);
    }

    @Test
    public void testParseInvalidLogLine() {
        String line = "invalid log line";
        LogEntry entry = LogEntry.parse(line);
        assertNull(entry);
    }
}

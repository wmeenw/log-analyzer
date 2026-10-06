package Tests;

import Logger.LogEntry;
import Logger.Statistic;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

public class StatisticTest {

    @Test
    public void testTotalRequests() {
        Statistic stats = new Statistic();
        LogEntry entry = LogEntry.parse("93.180.71.3 - - [11/Oct/2007:08:05:32 +0000] \"GET /test HTTP/1.1\" 200 1024 \"-\" \"Agent\"");

        stats.addEntry(entry);

        assertEquals(1, stats.getTotalRequest());
    }

    @Test
    public void testAvgAndMaxSize() {
        Statistic stats = new Statistic();

        LogEntry entry1 = LogEntry.parse("93.180.71.3 - - [11/Oct/2007:08:05:32 +0000] \"GET /test1 HTTP/1.1\" 200 1024 \"-\" \"Agent\"");
        LogEntry entry2 = LogEntry.parse("93.180.71.3 - - [11/Oct/2007:08:05:32 +0000] \"GET /test2 HTTP/1.1\" 200 2048 \"-\" \"Agent\"");

        stats.addEntry(entry1);
        stats.addEntry(entry2);

        assertEquals(1536.0, stats.getAvgSize(), 0.01);
        assertEquals(2048, stats.getMaxSize());
    }

    @Test
    public void testPercentile() {
        Statistic stats = new Statistic();

        for (int i = 1; i <= 20; i++) {
            LogEntry entry = LogEntry.parse(
                    String.format("93.180.71.3 - - [11/Oct/2007:08:05:32 +0000] \"GET /test HTTP/1.1\" 200 %d \"-\" \"Agent\"", i)
            );
            stats.addEntry(entry);
        }

        double p95 = stats.getPercentile();
        assertEquals(19.0, p95, 0.1);
    }

    @Test
    public void testResponseCodes() {
        Statistic stats = new Statistic();

        LogEntry entry1 = LogEntry.parse("93.180.71.3 - - [11/Oct/2007:08:05:32 +0000] \"GET /test HTTP/1.1\" 200 1024 \"-\" \"Agent\"");
        LogEntry entry2 = LogEntry.parse("93.180.71.3 - - [11/Oct/2007:08:05:32 +0000] \"GET /test HTTP/1.1\" 404 512 \"-\" \"Agent\"");

        stats.addEntry(entry1);
        stats.addEntry(entry2);

        var codes = stats.getTopCodes();
        assertEquals(1, (int) codes.get(200));
        assertEquals(1, (int) codes.get(404));
    }

    @Test
    public void testTopResources() {
        Statistic stats = new Statistic();

        LogEntry entry1 = LogEntry.parse("93.180.71.3 - - [11/Oct/2007:08:05:32 +0000] \"GET /index.html HTTP/1.1\" 200 1024 \"-\" \"Agent\"");
        LogEntry entry2 = LogEntry.parse("93.180.71.3 - - [11/Oct/2007:08:05:32 +0000] \"GET /about.html HTTP/1.1\" 200 512 \"-\" \"Agent\"");
        LogEntry entry3 = LogEntry.parse("93.180.71.3 - - [11/Oct/2007:08:05:32 +0000] \"GET /index.html HTTP/1.1\" 200 2048 \"-\" \"Agent\"");

        stats.addEntry(entry1);
        stats.addEntry(entry2);
        stats.addEntry(entry3);

        var resources = stats.getTopResources();
        assertEquals(2, (int) resources.get("/index.html"));
        assertEquals(1, (int) resources.get("/about.html"));
    }

    @Test
    public void testDateStats() {
        Statistic stats = new Statistic();

        LogEntry entry1 = LogEntry.parse("93.180.71.3 - - [11/Oct/2007:08:05:32 +0000] \"GET /test HTTP/1.1\" 200 1024 \"-\" \"Agent\"");
        LogEntry entry2 = LogEntry.parse("93.180.71.3 - - [12/Oct/2007:08:05:32 +0000] \"GET /test HTTP/1.1\" 200 1024 \"-\" \"Agent\"");
        LogEntry entry3 = LogEntry.parse("93.180.71.3 - - [12/Oct/2007:09:05:32 +0000] \"GET /test HTTP/1.1\" 200 1024 \"-\" \"Agent\"");

        stats.addEntry(entry1);
        stats.addEntry(entry2);
        stats.addEntry(entry3);

        var dateStats = stats.getDateStats();
        assertEquals(2, dateStats.size());
        assertEquals(33.33, (Double) dateStats.get(0).get("totalRequestsPercentage"), 0.01);
        assertEquals(66.67, (Double) dateStats.get(1).get("totalRequestsPercentage"), 0.01);
    }
}
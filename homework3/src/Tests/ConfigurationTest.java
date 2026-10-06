package Tests;

import Logger.Configuration;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.testng.Assert.assertThrows;
import static org.testng.AssertJUnit.assertEquals;

public class ConfigurationTest {
    @Test
    public void testParseValidArguments() {
        String[] args = {
                "--path", "access.log",
                "--format", "json",
                "--output", "report.json",
                "--from", "2025-03-01",
                "--to", "2025-03-31"
        };

        Configuration config = Configuration.parse(args);

        assertEquals("access.log", config.paths.get(0));
        assertEquals("json", config.format);
        assertEquals("report.json", config.output);
        assertEquals(LocalDate.parse("2025-03-01"), config.from);
        assertEquals(LocalDate.parse("2025-03-31"), config.to);
    }

    @Test
    public void testMissingPathThrowsException() {
        String[] args = {"--format", "json", "--output", "report.json"};

        assertThrows(IllegalArgumentException.class, () -> {
            Configuration.parse(args);
        });
    }

    @Test
    public void testInvalidFormatThrowsException() {
        String[] args = {"--path", "access.log", "--format", "xml", "--output", "report.json"};

        assertThrows(IllegalArgumentException.class, () -> {
            Configuration.parse(args);
        });
    }
}


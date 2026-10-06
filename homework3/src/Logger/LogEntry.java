package Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogEntry {
    private static final Pattern pattern = Pattern.compile("^(\\S+) - \\S+ \\[(.+?)\\] \"\\S+ (\\S+) \\S+\" (\\d+) (\\d+).*$");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);

    public final LocalDateTime times;
    public final String resource;
    public final int status;
    public final long size;

    private LogEntry(LocalDateTime times, String resource, int status, long size){
        this.times = times;
        this.resource = resource;
        this.status = status;
        this.size = size;
    }

    public static LogEntry parse(String line){
        try {
            Matcher m = pattern.matcher(line);
            if (!m.matches()) return null;

            return new LogEntry(
                    LocalDateTime.parse(m.group(2), formatter), m.group(3), Integer.parseInt(m.group(4)), Long.parseLong(m.group(5))
            );
        }
        catch (Exception e){
            return null;
        }
    }

}

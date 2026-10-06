package Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.logging.Logger;

public class LogAnalyzer {
    private static final Logger logger = Logger.getLogger(LogAnalyzer.class.getName());

    public static Statistic analyze(Configuration config) throws IOException{
        var stat = new Statistic();

        for (var path : config.paths){
            String fileName;
            boolean isUrl = path.toLowerCase().startsWith("http://") || path.toLowerCase().startsWith("https://");
            URL url = null;
            Path filePath = null;

            if (isUrl){
                try{
                    url = new URL(path);
                    fileName = url.getFile().toLowerCase();
                } catch (MalformedURLException e){
                    throw new IOException("Неверный URL: " +  path, e);
                }
            } else {
                filePath = Paths.get(path);
                if (!Files.exists(filePath))
                    throw new IOException("Файл не найден: " + path);
                fileName = filePath.toString().toLowerCase();
            }

            if (!fileName.endsWith(".log") && !fileName.endsWith(".txt"))
                throw new IOException("Неподдерживаемый формат файла: " + path);
            stat.addFile(path);

            try (BufferedReader reader = isUrl ?
                    new BufferedReader(new InputStreamReader(url.openStream())) : Files.newBufferedReader(filePath)){
                String line;
                int lineNum = 0;
                while ((line = reader.readLine()) != null){
                    lineNum++;
                    LogEntry entry = LogEntry.parse(line);

                    if (entry == null){
                        logger.warning("Пропущена строка " + lineNum + " в файле " + path);
                        continue;
                    }

                    if (isValideRange(entry, config.from, config.to)){
                        stat.addEntry(entry);
                    }
                }
            }
        }
        return stat;
    }

    private static boolean isValideRange(LogEntry entry, LocalDate from, LocalDate to){
        if (entry.times == null) return false;
        LocalDate date = entry.times.toLocalDate();

        if (from != null && date.isBefore(from)) return false;
        if (to != null && date.isAfter(to)) return false;
        return true;
    }
}

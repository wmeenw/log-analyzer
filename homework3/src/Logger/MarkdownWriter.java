package Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class MarkdownWriter {

    public static void write(Statistic stat, String outputPath) throws IOException {
        StringBuilder md = new StringBuilder();

        md.append("#### Общая информация\n");
        md.append("| Метрика | Значение |\n");
        md.append("|:--------|---------:|\n");

        md.append("| Файл(-ы) | ").append(String.join(", ", stat.getFiles())).append(" |\n");

        List<Map<String, Object>> dates = stat.getDateStats();
        if (!dates.isEmpty()) {
            md.append("| Начальная дата | ").append(dates.get(0).get("date")).append(" |\n");
            md.append("| Конечная дата | ").append(dates.get(dates.size()-1).get("date")).append(" |\n");
        }

        md.append("| Количество запросов | ").append(String.format("%,d", stat.getTotalRequest())).append(" |\n");
        md.append("| Средний размер ответа | ").append(String.format("%.2f b", stat.getAvgSize())).append(" |\n");
        md.append("| Максимальный размер ответа | ").append(stat.getMaxSize()).append(" b |\n");
        md.append("| 95p размера ответа | ").append(String.format("%.2f b", stat.getPercentile())).append(" |\n");

        md.append("\n#### Запрашиваемые ресурсы\n");
        md.append("| Ресурс | Количество |\n");
        md.append("|:-------|-----------:|\n");

        for (Map.Entry<String, Integer> e : stat.getTopResources().entrySet()) {
            md.append("| ").append(e.getKey()).append(" | ").append(String.format("%,d", e.getValue())).append(" |\n");
        }

        md.append("\n#### Коды ответа\n");
        md.append("| Код | Количество |\n");
        md.append("|:---:|-----------:|\n");

        for (Map.Entry<Integer, Integer> e : stat.getTopCodes().entrySet()) {
            md.append("| ").append(e.getKey()).append(" | ").append(String.format("%,d", e.getValue())).append(" |\n");
        }

        Files.writeString(Paths.get(outputPath), md.toString());
    }
}

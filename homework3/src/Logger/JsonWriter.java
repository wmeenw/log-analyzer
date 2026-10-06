package Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class JsonWriter {
    public static void write(Statistic stat, String outputPath) throws IOException{
        var json = new StringBuilder();
        json.append("{\n");

        json.append("  \"files\": [");
        List<String> files = stat.getFiles();
        for (int i = 0; i < files.size(); ++i){
            if (i > 0) json.append(",");
            json.append("\"").append(files.get(i)).append("\"");
        }
        json.append("],\n");

        json.append("  \"totalRequestsCount\": ").append(stat.getTotalRequest()).append(",\n");

        json.append("  \"responseSizeInBytes\": {\n");
        json.append("    \"average\": ").append(stat.getAvgSize()).append(",\n");
        json.append("    \"max\": ").append(stat.getMaxSize()).append(",\n");
        json.append("    \"p95\": ").append(stat.getPercentile()).append("\n");
        json.append("  },\n");

        json.append("  \"resources\": [\n");
        boolean first = true;
        for (Map.Entry<String, Integer> e : stat.getTopResources().entrySet()){
            if (!first) json.append(",\n");
            json.append("    {\"resource\": \"").append(e.getKey()).append("\", \"totalRequestsCount\": ").append(e.getValue()).append("}");
            first = false;
        }
        json.append("\n  ],\n");

        json.append("  \"responseCodes\": [\n");
        first = true;
        for (Map.Entry<Integer, Integer> e : stat.getTopCodes().entrySet()){
            if (!first) json.append(",\n");
            json.append("    {\"code\": ").append(e.getKey()).append(", \"totalResponsesCount\": ").append(e.getValue()).append("}");
            first = false;
        }
        json.append("\n  ],\n");

        json.append("  \"requestsPerDate\": [\n");
        first = true;
        for (Map<String, Object> d : stat.getDateStats()){
            if (!first) json.append(",\n");
            json.append("    {")
                    .append("\"date\": \"").append(d.get("date")).append("\", ")
                    .append("\"weekday\": \"").append(d.get("weekday")).append("\", ")
                    .append("\"totalRequestsCount\": ").append(d.get("totalRequestsCount")).append(", ")
                    .append("\"totalRequestsPercentage\": ").append(d.get("totalRequestsPercentage"))
                    .append("}");
            first = false;
        }
        json.append("\n  ]\n");

        json.append("}\n");

        Files.writeString(Paths.get(outputPath), json.toString());
    }
}

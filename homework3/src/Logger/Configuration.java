package Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Configuration {
    public List<String> paths = new ArrayList<>();
    public String format;
    public String output;
    public LocalDate from;
    public LocalDate to;

    public static Configuration parse(String[] args){
        var configuration = new Configuration();

        for (int i = 0; i < args.length; ++i){
            switch(args[i]){
                case "--path": case "-p":
                    if (i + 1 < args.length) configuration.paths.add(args[++i]);
                    break;
                case "--format": case "-f":
                    if (i + 1 < args.length) configuration.format = args[++i].toLowerCase();
                    break;
                case"--output": case "-o":
                    if (i + 1 < args.length) configuration.output = args[++i];
                    break;
                case "--from":
                    if (i + 1 < args.length) configuration.from = LocalDate.parse(args[++i]);
                    break;
                case "--to":
                    if (i + 1 < args.length) configuration.to = LocalDate.parse(args[++i]);
                    break;
            }
        }
        configuration.valid();
        return configuration;
    }

    private void valid(){
        if (paths.isEmpty()){
            throw new IllegalArgumentException("Не указан путь к логам (--path)");
        }
        if (format == null){
            throw new IllegalArgumentException("Не указан формат вывода (--format)");
        }
        if (!format.equals("json") && !format.equals("markdown")){
            throw new IllegalArgumentException("Не верный формат. Требуется json или markdown");
        }
        if (output == null){
            throw new IllegalArgumentException("Не указан выходной файл (--output)");
        }
        if (from != null && to != null && !from.isBefore(to)){
            throw new IllegalArgumentException("Дата from должна быть указана раньше to");
        }
    }

    public void validateOut() throws Exception{
        Path outPath = Paths.get(output);
        if (Files.exists(outPath)){
            throw new IllegalArgumentException("Файл уже существует - " + output);
        }

        String expected = format.equals("json") ? ".json" : ".md";
        if (!output.toLowerCase().endsWith(expected)){
            throw new IllegalArgumentException("Расширение файла должно быть " + expected);
        }

        Path parent = outPath.getParent();
        if (parent != null && !Files.isWritable(parent)){
            throw new IllegalArgumentException("Не удается произвести запись в дирекцию - " + parent);
        }
    }

    @Override
    public String toString(){
        return String.format("paths=%s, format=%s, output=%s, from=%s, to=%s",
                paths, format, output, from, to);
    }
}

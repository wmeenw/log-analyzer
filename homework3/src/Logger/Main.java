package Logger;

import java.util.logging.*;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            Configuration config = Configuration.parse(args);
            logger.info("Запуск с параметрами: " + config);

            config.validateOut();

            Statistic stats = LogAnalyzer.analyze(config);

            if (config.format.equals("json")) {
                JsonWriter.write(stats, config.output);
            } else {
                MarkdownWriter.write(stats, config.output);
            }

            logger.info("Анализ успешно завершен");
            System.exit(0);

        } catch (IllegalArgumentException e) {
            logger.severe("Ошибка: " + e.getMessage());
            System.err.println("Ошибка: " + e.getMessage());
            System.exit(2);
        } catch (Exception e) {
            logger.severe("Непредвиденная ошибка: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
# Домашнее задание 3 — анализатор логов

Утилита, которая читает `access.log`, считает статистику и сохраняет отчёт в JSON или Markdown.

- `Configuration` — разбор аргументов командной строки и проверка выходного файла.
- `LogEntry` — одна запись лога.
- `LogAnalyzer` — разбор лога и подсчёт статистики.
- `Statistic` — результаты анализа.
- `JsonWriter`, `MarkdownWriter` — запись отчёта.
- `Tests` — юнит-тесты.

## Запуск

Откройте проект в IntelliJ IDEA (файл `homework3.iml`) и запустите `Logger.Main` с аргументами конфигурации. Тесты лежат в `src/Tests`.

Исходный код взят из ветки `homework3` репозитория [wmeenw/JAVAhomework](https://github.com/wmeenw/JAVAhomework).

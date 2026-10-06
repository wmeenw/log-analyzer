# Анализатор логов

Домашнее задание 3 по Java. Утилита читает лог веб-сервера (`access.log`), считает статистику и сохраняет отчёт.

## Что сделано в качестве ДЗ

- Разбор аргументов командной строки и проверка выходного файла (`Configuration`).
- Разбор записей лога (`LogEntry`) и подсчёт статистики (`LogAnalyzer`, `Statistic`).
- Сохранение отчёта в JSON (`JsonWriter`) или в Markdown (`MarkdownWriter`).
- Логирование через `java.util.logging`.
- Юнит-тесты для конфигурации, записи лога и статистики (`src/Tests`).

## Запуск

Откройте проект в IntelliJ IDEA (файл `homework3.iml`) и запустите `Logger.Main` с аргументами конфигурации.

Пример данных: `access.log`, пример результата: `report.json`.

Исходный код: ветка `homework3` репозитория [wmeenw/JAVAhomework](https://github.com/wmeenw/JAVAhomework).

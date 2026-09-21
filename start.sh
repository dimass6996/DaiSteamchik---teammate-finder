#!/usr/bin/env bash
# Скрипт запуска DaiSteamchik Bot.
# Просто запусти:  ./start_instruction.md
#
# set -e — если на каком-то шаге произойдёт ошибка, скрипт остановится,
# а не продолжит выполнение дальше.
set -e

# Переходим в папку, где лежит этот скрипт (корень проекта).
# Это нужно, чтобы библиотека dotenv нашла файл .env — она ищет его
# относительно рабочей директории, а не относительно пути скрипта.
cd "$(dirname "$0")"

echo "Сборка и запуск DaiSteamchik Bot..."
mvn -q compile exec:java -Dexec.mainClass=ru.daisteamchik.Main
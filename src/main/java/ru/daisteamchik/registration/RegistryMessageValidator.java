package ru.daisteamchik.registration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistryMessageValidator {

    private static final Pattern NAME_PATTERN =
            Pattern.compile("^[a-zA-Zа-яА-ЯёЁ0-9 _-]+$");

    private static final Pattern STEAM_URL_PATTERN =
            Pattern.compile("^https?://(www\\.)?steamcommunity\\.com/(id/[a-zA-Z0-9_-]{2,32}|profiles/\\d{17})/?$");

    private static final Pattern PRIME_TIME_PATTERN =
            Pattern.compile("^(\\d{1,2}):(\\d{2})\\s*[-–]\\s*(\\d{1,2}):(\\d{2})$");

    private static final Set<String> ALLOWED_ROLES = new HashSet<>(Arrays.asList(
            "керри", "саппорт", "капитан", "мидер", "оффлейнер", "хардлейнер",
            "снайпер", "люркер", "энтри", "танк", "хилер", "дд", "универсал",
            "carry", "support", "awper", "mid", "offlane",
            "entry", "lurker", "flex", "tank", "healer", "dps"
    ));

    private static final Set<String> ALLOWED_GAMES = new HashSet<>(Arrays.asList(
            "dota 2", "dota2", "dota", "дота 2", "дота",
            "cs 2", "cs2", "cs:go", "csgo", "counter-strike 2", "кс 2", "кс2", "кс",
            "rust", "раст",
            "apex", "apex legends", "апекс",
            "valorant", "валорант",
            "pubg", "пабг",
            "overwatch 2", "overwatch", "овервотч",
            "league of legends", "lol", "лол",
            "deadlock", "дедлок",
            "minecraft", "майнкрафт",
            "rainbow six siege", "r6"
    ));

    public Boolean validateName(String name) {
        return getNameError(name) == null;
    }

    public String getNameError(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Имя не может быть пустым!";
        }
        String trimmed = name.trim();
        if (trimmed.length() < 2 || trimmed.length() > 32) {
            return "Имя должно содержать от 2 до 32 символов.";
        }
        if (!NAME_PATTERN.matcher(trimmed).matches()) {
            return "Имя может содержать только буквы, цифры, пробелы, дефис и нижнее подчёркивание.";
        }
        return null;
    }

    public Boolean validateUrl(String url) {
        return getUrlError(url) == null;
    }

    public String getUrlError(String url) {
        if (url == null || url.trim().isEmpty()) {
            return "Ссылка не может быть пустой!";
        }
        String trimmed = url.trim();
        if (!STEAM_URL_PATTERN.matcher(trimmed).matches()) {
            return "Некорректная ссылка на Steam. Укажи ссылку в формате https://steamcommunity.com/id/... (или https://steamcommunity.com/profiles/7656...)";
        }
        return null;
    }

    public Boolean validatePrimeTime(String primeTime) {
        return getPrimeTimeError(primeTime) == null;
    }

    public String getPrimeTimeError(String primeTime) {
        if (primeTime == null || primeTime.trim().isEmpty()) {
            return "Укажи время, когда обычно играешь!";
        }
        Matcher matcher = PRIME_TIME_PATTERN.matcher(primeTime.trim());
        if (!matcher.matches()) {
            return "Некорректный формат времени. Укажи прайм-тайм в формате ЧЧ:ММ - ЧЧ:ММ (например, 19:00 - 23:00).";
        }

        int startHour = Integer.parseInt(matcher.group(1));
        int startMinute = Integer.parseInt(matcher.group(2));
        int endHour = Integer.parseInt(matcher.group(3));
        int endMinute = Integer.parseInt(matcher.group(4));

        if (startHour > 23 || endHour > 23 || startMinute > 59 || endMinute > 59) {
            return "Некорректное время: часы должны быть от 00 до 23, а минуты — от 00 до 59.";
        }
        if (startHour == endHour && startMinute == endMinute) {
            return "Время начала и окончания прайм-тайма не должно совпадать.";
        }
        return null;
    }

    public Boolean validateRoles(String roles) {
        return getRolesError(roles) == null;
    }

    public String getRolesError(String roles) {
        if (roles == null || roles.trim().isEmpty()) {
            return "Укажи хотя бы одну роль!";
        }
        String[] parts = roles.split("[,;/]+");
        boolean hasAtLeastOne = false;

        for (String part : parts) {
            String role = part.trim().toLowerCase();
            if (role.isEmpty()) {
                continue;
            }
            hasAtLeastOne = true;
            if (!ALLOWED_ROLES.contains(role)) {
                return "Неизвестная роль: «" + part.trim() + "». Доступные роли: Керри, Саппорт, Капитан, Мидер, Авапер, Танк, Хилер, ДД.";
            }
        }

        if (!hasAtLeastOne) {
            return "Укажи хотя бы одну роль!";
        }
        return null;
    }

    public Boolean validateGames(String games) {
        return getGamesError(games) == null;
    }

    public String getGamesError(String games) {
        if (games == null || games.trim().isEmpty()) {
            return "Укажи хотя бы одну игру!";
        }
        String[] parts = games.split("[,;/]+");
        boolean hasAtLeastOne = false;

        for (String part : parts) {
            String game = part.trim().toLowerCase();
            if (game.isEmpty()) {
                continue;
            }
            hasAtLeastOne = true;
            if (!ALLOWED_GAMES.contains(game)) {
                return "Неизвестная игра: «" + part.trim() + "». Доступные игры: Dota 2, CS 2, Rust, Apex Legends, Valorant, PUBG, Overwatch 2, League of Legends, Deadlock.";
            }
        }

        if (!hasAtLeastOne) {
            return "Укажи хотя бы одну игру!";
        }
        return null;
    }
}
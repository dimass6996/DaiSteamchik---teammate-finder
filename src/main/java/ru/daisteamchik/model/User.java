package ru.daisteamchik.model;

import ru.daisteamchik.registration.RegistrationSession;

import java.util.Objects;

public class User {

    private final long userId;
    private String name;
    private String steamUrl;
    private String primeTime;
    private String role;
    private String games;

    public User(long userId, String name, String steamUrl, String primeTime, String role, String games) {
        this.userId = userId;
        this.name = name;
        this.steamUrl = steamUrl;
        this.primeTime = primeTime;
        this.role = role;
        this.games = games;
    }

    public User(long userId, RegistrationSession session) {
        this(
                userId,
                session.getName(),
                session.getSteamUrl(),
                session.getPrimeTime(),
                session.getRole(),
                session.getGames()
        );
    }

    public static User fromSession(long userId, RegistrationSession session) {
        return new User(userId, session);
    }

    public long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSteamUrl() {
        return steamUrl;
    }

    public void setSteamUrl(String steamUrl) {
        this.steamUrl = steamUrl;
    }

    public String getPrimeTime() {
        return primeTime;
    }

    public void setPrimeTime(String primeTime) {
        this.primeTime = primeTime;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getGames() {
        return games;
    }

    public void setGames(String games) {
        this.games = games;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId
                && Objects.equals(name, user.name)
                && Objects.equals(steamUrl, user.steamUrl)
                && Objects.equals(primeTime, user.primeTime)
                && Objects.equals(role, user.role)
                && Objects.equals(games, user.games);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, name, steamUrl, primeTime, role, games);
    }

    @Override
    public String toString() {
        return "Имя: " + name + "\n" +
                "Steam: " + steamUrl + "\n" +
                "Прайм-тайм: " + primeTime + "\n" +
                "Роль: " + role + "\n" +
                "Игры: " + games;
    }
}
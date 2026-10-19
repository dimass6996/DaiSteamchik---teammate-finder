package ru.daisteamchik.registration;

public class RegistrationSession {

    private String name;
    private String steamUrl;
    private String primeTime;
    private String role;
    private String games;

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
}
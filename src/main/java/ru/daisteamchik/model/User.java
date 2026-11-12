package ru.daisteamchik.model;

import ru.daisteamchik.registration.RegistrationSession;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.GetUserProfilePhotos;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.UserProfilePhotos;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    public InputFile generateProfilePicture(TelegramLongPollingBot bot)
            throws IOException {
        BufferedImage avatar = null;
        try {
            UserProfilePhotos photos = bot.execute(new GetUserProfilePhotos(userId, 0, 1));
            if (photos != null && !photos.getPhotos().isEmpty()) {
                PhotoSize largest = photos.getPhotos().get(0).stream()
                        .max((a, b) -> Integer.compare(a.getWidth() * a.getHeight(), b.getWidth() * b.getHeight()))
                        .orElse(null);
                if (largest != null) {
                    File file = bot.execute(new GetFile(largest.getFileId()));
                    try (InputStream stream = bot.downloadFileAsStream(file)) {
                        avatar = ImageIO.read(stream);
                    }
                }
            }
        } catch (TelegramApiException e) {
            // Карточка всё равно создаётся с инициалом вместо аватара.
        }

        BufferedImage card = new BufferedImage(1000, 600, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = card.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(20, 30, 44));
        g.fillRect(0, 0, card.getWidth(), card.getHeight());
        g.setColor(new Color(34, 48, 66));
        g.fillRoundRect(24, 24, 952, 552, 32, 32);

        g.setColor(Color.WHITE);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 34));
        g.drawString("Профиль игрока", 64, 92);

        Shape oldClip = g.getClip();
        g.setClip(new Ellipse2D.Double(64, 160, 220, 220));
        if (avatar != null) {
            g.drawImage(avatar, 64, 160, 220, 220, null);
        } else {
            g.setColor(new Color(75, 95, 120));
            g.fillRect(64, 160, 220, 220);
            g.setColor(Color.WHITE);
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 72));
            String initials = name == null || name.isBlank() ? "?" : name.substring(0, 1).toUpperCase();
            g.drawString(initials, 142, 292);
        }
        g.setClip(oldClip);

        String[] labels = {"Имя", "Игры", "Роль", "Прайм-тайм"};
        String[] values = {name, games, role, primeTime};
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 25));
        FontMetrics metrics = g.getFontMetrics();
        int y = 176;
        for (int i = 0; i < labels.length; i++) {
            g.setColor(new Color(155, 177, 200));
            g.drawString(labels[i], 340, y);
            g.setColor(Color.WHITE);
            String value = values[i] == null || values[i].isBlank() ? "Не указано" : values[i];
            int maxWidth = 580;
            while (metrics.stringWidth(value) > maxWidth && value.length() > 3) {
                value = value.substring(0, value.length() - 2) + "…";
            }
            g.drawString(value, 340, y + 34);
            y += 78;
        }
        g.dispose();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(card, "png", output);
        return new InputFile(new ByteArrayInputStream(output.toByteArray()), "profile.png");
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

    public String toShortString(){
        return "Имя: " + name + "\n" +
                "   id:" + userId + "\n" +
                "   Steam: " + steamUrl + "\n" +
                "   Прайм-тайм: " + primeTime + "\n" +
                "   Роль: " + role + "\n" +
                "   Игры: " + games + "\n";
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

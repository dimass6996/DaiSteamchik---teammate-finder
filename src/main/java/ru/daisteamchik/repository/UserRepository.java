package ru.daisteamchik.repository;

import ru.daisteamchik.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepository {
    private static final String URL = "jdbc:sqlite:daisteamchik.db";

    public UserRepository() {
        initDatabase();
    }

    private void initDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "user_id INTEGER PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "steam_url TEXT NOT NULL," +
                "prime_time TEXT NOT NULL," +
                "role TEXT NOT NULL," +
                "games TEXT NOT NULL" +
                ");";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Ошибка при создании таблицы users: " + e.getMessage());
        }
    }

    // сохраняем или обновляем пользователй
    public void saveUser(User user) {
        String sql = "INSERT INTO users (user_id, name, steam_url, prime_time, role, games) " +
                "VALUES (?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT(user_id) DO UPDATE SET " +
                "name=excluded.name, steam_url=excluded.steam_url, " +
                "prime_time=excluded.prime_time, role=excluded.role, games=excluded.games;";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, user.getUserId());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getSteamUrl());
            pstmt.setString(4, user.getPrimeTime());
            pstmt.setString(5, user.getRole());
            pstmt.setString(6, user.getGames());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка при сохранении пользователя: " + e.getMessage());
        }
    }

    public User getUserFromBd(Long userId){
        String sql = "SELECT user_id, name, steam_url, prime_time, role, games FROM users WHERE user_id = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setLong(1, userId);

            try (ResultSet rs = pstmt.executeQuery()){
                while(rs.next()){
                    Long user_id = rs.getLong("user_id");
                    String name = rs.getString("name");
                    String steam_url = rs.getString("steam_url");
                    String prime_time = rs.getString("prime_time");
                    String role = rs.getString("role");
                    String games = rs.getString("games");

                    return new User(user_id, name, steam_url, prime_time, role, games);
                }
            }

        } catch (SQLException e){
            System.err.println("Ошибка при получении пользователя: " + e.getMessage());
        }
        return null;
    }

    public List<User> getUsersFromBd(int limit) {
        String sql = "SELECT user_id, name, steam_url, prime_time, role, games " +
                "FROM users ORDER BY RANDOM() LIMIT ?";

        List<User> users = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(2, limit);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    users.add(new User(
                            rs.getLong("user_id"),
                            rs.getString("name"),
                            rs.getString("steam_url"),
                            rs.getString("prime_time"),
                            rs.getString("role"),
                            rs.getString("games")
                    ));
                }
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return users;
    }

    // загружаем всех пользователй в бд
    public Map<Long, User> loadAllUsers() {
        Map<Long, User> users = new HashMap<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User user = new User(
                        rs.getLong("user_id"),
                        rs.getString("name"),
                        rs.getString("steam_url"),
                        rs.getString("prime_time"),
                        rs.getString("role"),
                        rs.getString("games")
                );
                users.put(user.getUserId(), user);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при загрузке пользователей: " + e.getMessage());
        }
        return users;
    }
}
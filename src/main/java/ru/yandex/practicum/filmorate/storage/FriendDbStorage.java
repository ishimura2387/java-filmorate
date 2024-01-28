package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Slf4j
@Component("friendDbStorage")
@AllArgsConstructor
public class FriendDbStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getFriends(int id) {
        String sql = "SELECT a.friend_id, b.id, b.email, b.login, b.name, b.birthday FROM friendship_status AS a " +
                "LEFT OUTER JOIN users AS b ON a.friend_id = b.id WHERE user_id = ?";
        List<User> friends = jdbcTemplate.query(sql, userRowMapper(), id);
        return friends;
    }

    @Override
    public void addFriends(int idUser, int idFriend) {
        String sql = "INSERT INTO friendship_status (user_id, friend_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, idUser, idFriend);
    }

    @Override
    public void deleteFriends(int idUser, int idFriend) {
        String sql = "DELETE FROM friendship_status WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, idUser, idFriend);
    }

    @Override
    public List<User> findTotalFriends(int idUser1, int idUser2) {
        String sql = "SELECT u.id, u.email, u.login, u.name, u.birthday " +
                "FROM friendship_status f1 " +
                "JOIN friendship_status f2 ON f1.friend_id = f2.friend_id " +
                "JOIN users u ON f1.friend_id = u.id " +
                "WHERE f1.user_id = ? AND f2.user_id = ?";
        List<User> users = jdbcTemplate.query(sql, userRowMapper(), idUser1, idUser2);
        return users;
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> User.builder()
                .id(rs.getInt("id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }
}

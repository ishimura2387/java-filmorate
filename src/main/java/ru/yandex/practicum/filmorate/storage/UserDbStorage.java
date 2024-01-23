
package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;


@Slf4j
@Component("userDbStorage")
@AllArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;


    @Override
    public List<User> findAllUsers() {
        String sql = "SELECT * FROM users";
        List<User> users = jdbcTemplate.query(sql, userRowMapper());
        return users;
    }

    @Override
    public User createNewUser(User user) {
        String sql = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday().toString());
        user.setId(jdbcTemplate.queryForObject("SELECT MAX(id) AS last_id FROM users", (rs, rowNum) ->
                Integer.valueOf(rs.getInt("last_id"))));
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday().toString(),
                user.getId());
        return user;
    }

    @Override
    public User getUser(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        User user = jdbcTemplate.queryForObject(sql, userRowMapper(), id);
        return user;
    }

    @Override
    public void deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Integer> findAllUsersId() {
        String sql = "SELECT id FROM users";
        List<Integer> ids = jdbcTemplate.query(sql, (rs, rowNum) -> Integer.valueOf(rs.getInt("id")));
        return ids;
    }

    public List<User> getFriends(int id) {
        String sql = "SELECT a.friend_id, b.id, b.email, b.login, b.name, b.birthday FROM friendship_status AS a " +
                "LEFT OUTER JOIN users AS b ON a.friend_id = b.id WHERE user_id = ?";
        List<User> friends = jdbcTemplate.query(sql, userRowMapper(), id);
        return friends;
    }

    public void addFriends(int idUser, int idFriend) {
        String sql = "INSERT INTO friendship_status (user_id, friend_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, idUser, idFriend);
    }

    public void deleteFriends(int idUser, int idFriend) {
        String sql = "DELETE FROM friendship_status WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, idUser, idFriend);
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
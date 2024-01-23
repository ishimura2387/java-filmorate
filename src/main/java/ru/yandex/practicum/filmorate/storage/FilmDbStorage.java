package ru.yandex.practicum.filmorate.storage;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.TreeSet;

@Slf4j
@Component("filmDbStorage")
@AllArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> findAllFilms() {
        String sql = "SELECT * FROM films";
        List<Film> films = jdbcTemplate.query(sql, filmRowMapper());
        return films;
    }

    @Override
    public Film createNewFilm(Film film) {
        String sql = "INSERT INTO films (name, description, releaseDate, duration, mpa) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate().toString(),
                film.getDuration(), film.getMpa().getId());
        TreeSet<Genre> genres = film.getGenres();
        film.setId(jdbcTemplate.queryForObject("SELECT MAX(id) AS last_id FROM films", (rs, rowNum) ->
                Integer.valueOf(rs.getInt("last_id"))));
        if (genres != null) {
            for (Genre genreId : genres) {
                String sqlForGenres = "INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?)";
                jdbcTemplate.update(sqlForGenres, film.getId(), genreId.getId());
            }
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "UPDATE films SET name = ?, description = ?, releaseDate = ?, duration = ?, MPA = ? WHERE id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate().toString(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        TreeSet<Genre> genres = film.getGenres();
        if (genres != null) {
            String sqlForGenresClean = "DELETE FROM films_genres WHERE film_id = ?";
            jdbcTemplate.update(sqlForGenresClean, film.getId());
            String sqlForGenres = "INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?)";
            for (Genre genreId : genres) {
                jdbcTemplate.update(sqlForGenres, film.getId(), genreId.getId());
            }
        }
        return film;
    }

    @Override
    public Film getFilm(int id) {
        String sql = "SELECT * FROM films WHERE id = ?";
        Film film = jdbcTemplate.queryForObject(sql, filmRowMapper(), id);
        return film;
    }

    @Override
    public void deleteFilm(int id) {
        String sql = "DELETE FROM films WHERE id = ?";
        jdbcTemplate.update(sql, id);
        String sqlForGenresClean = "DELETE FROM films_genres WHERE film_id = ?";
        jdbcTemplate.update(sqlForGenresClean, id);
        String sqlForLikesClean = "DELETE FROM films_likes WHERE film_id = ?";
        jdbcTemplate.update(sqlForLikesClean, id);
    }

    @Override
    public List<Integer> findAllFilmsId() {
        String sql = "SELECT id FROM films";
        List<Integer> ids = jdbcTemplate.query(sql, (rs, rowNum) -> Integer.valueOf(rs.getInt("id")));
        return ids;
    }

    @Override
    public List<Genre> findAllGenres() {
        String sql = "SELECT * FROM genres";
        List<Genre> genres = jdbcTemplate.query(sql, genreRowMapper());
        return genres;
    }

    @Override
    public Genre getGenre(int id) {
        String sql = "SELECT * FROM genres WHERE id = ?";
        Genre genre = jdbcTemplate.queryForObject(sql, genreRowMapper(), id);
        return genre;
    }

    @Override
    public List<Mpa> findAllMpas() {
        String sql = "SELECT * FROM MPA";
        List<Mpa> mpas = jdbcTemplate.query(sql, (rs, rowNum) -> new Mpa(rs.getInt("id"), rs.getString("name")));
        return mpas;
    }

    @Override
    public Mpa getMpa(int id) {
        String sql = "SELECT * FROM mpa WHERE id = ?";
        Mpa mpa = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Mpa(rs.getInt("id"), rs.getString("name")), id);
        return mpa;
    }

    @Override
    public void addLike(int userId, int filmId) {
        String sql = "INSERT INTO films_likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, filmId);
    }

    @Override
    public void deleteLike(int userId, int filmId) {
        String sql = "DELETE FROM films_likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, userId, filmId);
    }

    private RowMapper<Film> filmRowMapper() {
        return (rs, rowNum) -> Film.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("releasedate").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(jdbcTemplate.queryForObject("SELECT * FROM mpa WHERE id = ?",
                        (rs12, rowNum12) -> new Mpa(rs12.getInt("id"),
                                rs12.getNString("name")), rs.getInt("mpa")))
                .genres(new TreeSet<>(jdbcTemplate.query("SELECT fg.film_id, fg.genre_id, g.name, g.id FROM films_genres fg " +
                                "LEFT JOIN genres g ON fg.genre_id = g.id WHERE fg.film_id = ? ",
                        genreRowMapper(), rs.getInt("id"))))
                .likes(jdbcTemplate.query("SELECT * FROM films_likes WHERE film_id = ?", (rs1, rowNum1) ->
                        Integer.valueOf(rs1.getInt("user_id")), rs.getInt("id")))
                .build();
    }

    private RowMapper<Genre> genreRowMapper() {
        return (rs, rowNum) -> Genre.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
    }

    public List<Integer> findAllGenresId() {
        String sql = "SELECT id FROM genres";
        List<Integer> ids = jdbcTemplate.query(sql, (rs, rowNum) -> Integer.valueOf(rs.getInt("id")));
        return ids;
    }

    public List<Integer> findAllMpasId() {
        String sql = "SELECT id FROM mpa";
        List<Integer> ids = jdbcTemplate.query(sql, (rs, rowNum) -> Integer.valueOf(rs.getInt("id")));
        return ids;
    }
}

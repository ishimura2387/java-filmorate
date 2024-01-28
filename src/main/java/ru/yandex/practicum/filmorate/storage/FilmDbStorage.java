package ru.yandex.practicum.filmorate.storage;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

@Slf4j
@Component("filmDbStorage")
@AllArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> findAllFilms() {
        String sqlForGenres = "SELECT fg.film_id, fg.genre_id, g.name, g.id FROM films_genres fg LEFT JOIN genres g ON fg.genre_id = g.id";
        String sql = "SELECT f.id film_id, f.name film_name, f.description, f.releasedate, f.duration, f.likes, m.id mpa_id, " +
                "m.name mpa_name FROM films f LEFT JOIN mpa m ON f.mpa = m.id";
        List<Integer> idFilms = jdbcTemplate.query(sqlForGenres, (RowMapper<Integer>) (rs, rowNum) ->
                Integer.valueOf(rs.getInt("film_id")));
        List<Genre> genres = jdbcTemplate.query(sqlForGenres, genreRowMapper());
        HashMap<Integer, List<Genre>> filmsGenres = new HashMap<>();
        if (idFilms.size() > 0) {
            Integer newCount = idFilms.get(0);
            Integer oldCount = idFilms.get(0);
            Integer countList = 0;
            List<Genre> listGenre = new ArrayList<>();
            for (Integer id : idFilms) {
                newCount = id;
                if (newCount == oldCount) {
                    listGenre.add(genres.get(countList));
                    countList++;
                    filmsGenres.put(id, new ArrayList<>(listGenre));
                } else {
                    listGenre.clear();
                    listGenre.add(genres.get(countList));
                    countList++;
                    filmsGenres.put(id, new ArrayList<>(listGenre));
                }
                oldCount = newCount;
            }
        }
        List<Film> films = jdbcTemplate.query(sql, filmRowMapper(filmsGenres));
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
        String sql = "UPDATE films SET name = ?, description = ?, releaseDate = ?, duration = ?, mpa = ? WHERE id = ?";
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
        String sql = "SELECT f.id film_id, f.name film_name, f.description, f.releasedate, f.duration, f.likes, m.id mpa_id, " +
                "m.name mpa_name FROM films f LEFT JOIN mpa m ON f.mpa = m.id WHERE f.id = ?";
        String sqlForGenres = "SELECT fg.film_id, fg.genre_id, g.name, g.id FROM films_genres fg LEFT JOIN genres g " +
                "ON fg.genre_id = g.id WHERE fg.film_id = ?";
        List<Genre> genres = jdbcTemplate.query(sqlForGenres, genreRowMapper(), id);
        HashMap<Integer, List<Genre>> mapa = new HashMap<>();
        mapa.put(id, genres);
        Film film = jdbcTemplate.queryForObject(sql, filmRowMapper(mapa), id);
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
    public void addLike(int userId, int filmId) {
        String sql = "INSERT INTO films_likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, filmId);
        String sqlLikes = "UPDATE films SET likes = ? WHERE id = ?";
        Integer countLikes = jdbcTemplate.queryForObject("SELECT likes FROM films WHERE id = ?", (rs, rowNum) ->
                Integer.valueOf(rs.getInt("likes")), filmId) + 1;
        jdbcTemplate.update(sqlLikes, countLikes, filmId);
    }

    @Override
    public void deleteLike(int userId, int filmId) {
        String sql = "DELETE FROM films_likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, userId, filmId);
        Integer countLikes = jdbcTemplate.queryForObject("SELECT likes FROM films WHERE id = ?", (rs, rowNum) ->
                Integer.valueOf(rs.getInt("likes")), filmId) - 1;
        String sqlLikes = "UPDATE films SET likes = ? WHERE id = ?";
        jdbcTemplate.update(sqlLikes, countLikes, filmId);
    }

    private RowMapper<Film> filmRowMapper(HashMap<Integer, List<Genre>> films_genres) {
        return (rs, rowNum) -> Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("film_name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("releasedate").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name")))
                .genres(new TreeSet<>(films_genres.getOrDefault(rs.getInt("film_id"), new ArrayList<>())))
                .likes(rs.getInt("likes"))
                .build();
    }

    private RowMapper<Genre> genreRowMapper() {
        return (rs, rowNum) -> Genre.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
    }
}

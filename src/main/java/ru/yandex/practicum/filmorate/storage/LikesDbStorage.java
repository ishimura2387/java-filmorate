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
@Component("likesDbStorage")
@AllArgsConstructor
public class LikesDbStorage implements LikesStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(int userId, int filmId) {
        String sql = "INSERT INTO films_likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
        String sqlLikes = "UPDATE films SET likes = ? WHERE id = ?";
        Integer countLikes = jdbcTemplate.queryForObject("SELECT likes FROM films WHERE id = ?", (rs, rowNum) ->
                Integer.valueOf(rs.getInt("likes")), filmId) + 1;
        jdbcTemplate.update(sqlLikes, countLikes, filmId);
    }

    @Override
    public void deleteLike(int userId, int filmId) {
        String sql = "DELETE FROM films_likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
        Integer countLikes = jdbcTemplate.queryForObject("SELECT likes FROM films WHERE id = ?", (rs, rowNum) ->
                Integer.valueOf(rs.getInt("likes")), filmId) - 1;
        String sqlLikes = "UPDATE films SET likes = ? WHERE id = ?";
        jdbcTemplate.update(sqlLikes, countLikes, filmId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        String sql = "SELECT f.id film_id, f.name film_name, f.description, f.releasedate, f.duration, f.likes, " +
                "m.id mpa_id, m.name mpa_name FROM films f LEFT JOIN mpa m ON f.mpa = m.id ORDER BY LIKES DESC";
        jdbcTemplate.setMaxRows(count);
        List<Integer> idFilmsForSecondScript = jdbcTemplate.query(sql, (rs, rowNum) ->
                Integer.valueOf(rs.getInt("id")));
        String countSizeList = new String();
        for (Integer id : idFilmsForSecondScript) {
            countSizeList = countSizeList + "?, ";
        }
        countSizeList = countSizeList.substring(0, countSizeList.length() - 2) + ")";
        String secondSql = "SELECT fg.film_id, fg.genre_id, g.name, g.id FROM films_genres fg " +
                "LEFT JOIN genres g ON fg.genre_id = g.id WHERE fg.film_id IN (" + countSizeList;
        List<Integer> idFilms = jdbcTemplate.query(secondSql, (rs, rowNum) -> Integer.valueOf(rs.getInt("film_id")),
                idFilmsForSecondScript.toArray());
        List<Film> films = jdbcTemplate.query(sql, filmRowMapper(getGenres(idFilms)));
        return films;
    }

    @Override
    public boolean checkLikes(int idUser, int idFilm) {
        String sql = "SELECT COUNT(*) q FROM films_likes WHERE film_id = ? AND user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                Integer.valueOf(rs.getInt("q")), idFilm, idUser);
        if (count >= 1) {
            return true;
        } else {
            return false;
        }
    }

    private RowMapper<Genre> genreRowMapper() {
        return (rs, rowNum) -> Genre.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
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

    private HashMap<Integer, List<Genre>> getGenres(List<Integer> idFilms) {
        HashMap<Integer, List<Genre>> filmsGenres = new HashMap<>();
        if (idFilms.size() > 0) {
            String countSizeList = new String();
            for (Integer id : idFilms) {
                countSizeList = countSizeList + "?, ";
            }
            countSizeList = countSizeList.substring(0, countSizeList.length() - 2) + ")";
            String sqlForGenres = "SELECT fg.film_id, fg.genre_id, g.name, g.id FROM films_genres fg " +
                    "LEFT JOIN genres g ON fg.genre_id = g.id WHERE fg.film_id IN (" + countSizeList;
            List<Genre> genres = jdbcTemplate.query(sqlForGenres, genreRowMapper(), idFilms.toArray());
            if (genres.size() > 0) {
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
                    } else if (countList < genres.size()) {
                        listGenre.clear();
                        listGenre.add(genres.get(countList));
                        countList++;
                        filmsGenres.put(id, new ArrayList<>(listGenre));
                    }
                    oldCount = newCount;
                }
            }
        }
        return filmsGenres;
    }
}

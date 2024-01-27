package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Slf4j
@Component("genreDbStorage")
@AllArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

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
    public List<Integer> findAllGenresId() {
        String sql = "SELECT id FROM genres";
        List<Integer> ids = jdbcTemplate.query(sql, (rs, rowNum) -> Integer.valueOf(rs.getInt("id")));
        return ids;
    }


    private RowMapper<Genre> genreRowMapper() {
        return (rs, rowNum) -> Genre.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
    }
}

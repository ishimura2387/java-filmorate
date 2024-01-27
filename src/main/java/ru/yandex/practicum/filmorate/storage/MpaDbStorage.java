package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Slf4j
@Component("mpaDbStorage")
@AllArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

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

    public List<Integer> findAllMpasId() {
        String sql = "SELECT id FROM mpa";
        List<Integer> ids = jdbcTemplate.query(sql, (rs, rowNum) -> Integer.valueOf(rs.getInt("id")));
        return ids;
    }

}

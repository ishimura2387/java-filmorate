package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.TreeSet;

import static org.junit.Assert.*;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@JdbcTest
public class FilmDbStorageTest {

    private final JdbcTemplate jdbcTemplate;
    public FilmDbStorage filmStorage;
    Film film1;
    Film film2;
    Film film3;


    @BeforeEach
    public void beforeEach() {
        filmStorage = new FilmDbStorage(jdbcTemplate);
        film1 = Film.builder()
                .name("Гравицапа")
                .description("Описание гравицапы")
                .releaseDate(LocalDate.parse("1966-05-21"))
                .duration(120)
                .mpa(jdbcTemplate.queryForObject("SELECT * FROM mpa WHERE id = 3", (rs, rowNum) ->
                        new Mpa(rs.getInt("id"), rs.getString("name"))))
                .build();
        film2 = Film.builder()
                .name("Гарри Поттер")
                .description("Описание Гарри Поттера")
                .releaseDate(LocalDate.parse("1966-05-21"))
                .duration(120)
                .mpa(jdbcTemplate.queryForObject("SELECT * FROM mpa WHERE id = 3", (rs, rowNum) ->
                        new Mpa(rs.getInt("id"), rs.getString("name"))))
                .build();
        film3 = Film.builder()
                .name("Форсаж")
                .description("Описание Форсажа")
                .releaseDate(LocalDate.parse("1966-05-21"))
                .duration(120)
                .mpa(jdbcTemplate.queryForObject("SELECT * FROM mpa WHERE id = 3", (rs, rowNum) ->
                        new Mpa(rs.getInt("id"), rs.getString("name"))))
                .build();
        film1.setGenres(new TreeSet<>());
        film2.setGenres(new TreeSet<>());
        film3.setGenres(new TreeSet<>());
        filmStorage.createNewFilm(film1);
        filmStorage.createNewFilm(film2);
        filmStorage.createNewFilm(film3);
    }

    @AfterEach
    public void afterEach() {
        jdbcTemplate.update("ALTER TABLE FILMS ALTER COLUMN ID RESTART WITH 1");
    }

    @Test
    public void testCreateNewFilmAndGetFilm() {
        Film film4 = Film.builder()
                .name("Гравицапа")
                .description("Описание гравицапы")
                .releaseDate(LocalDate.parse("1966-05-21"))
                .duration(120)
                .mpa(jdbcTemplate.queryForObject("SELECT * FROM mpa WHERE id = 3", (rs, rowNum) ->
                        new Mpa(rs.getInt("id"), rs.getString("name"))))
                .build();
        film4.setGenres(new TreeSet<>());
        filmStorage.createNewFilm(film4);
        Film film5 = filmStorage.getFilm(4);
        assertEquals(film5, film4);
    }

    @Test
    public void testFindAllFilms() {
        List<Film> films = filmStorage.findAllFilms();
        for (Film d : films) {
            System.out.println(d);
        }
        System.out.println(film3);
        assertTrue(films.contains(film1));
        assertTrue(films.contains(film2));
        assertTrue(films.contains(film3));
    }

    @Test
    public void testUpdateFilm() {
        Film film4 = Film.builder()
                .name("Доярка из Хацапетовки")
                .description("Описание Доярки")
                .releaseDate(LocalDate.parse("2009-05-21"))
                .duration(121)
                .mpa(jdbcTemplate.queryForObject("SELECT * FROM mpa WHERE id = 3", (rs, rowNum) ->
                        new Mpa(rs.getInt("id"), rs.getString("name"))))
                .build();
        film4.setGenres(new TreeSet<>());
        filmStorage.createNewFilm(film4);
        Film film4Update = Film.builder()
                .id(4)
                .name("Доярка из Хацапетовки улучшенная")
                .description("Описание Доярки улучшенной")
                .releaseDate(LocalDate.parse("2009-05-21"))
                .duration(121)
                .mpa(jdbcTemplate.queryForObject("SELECT * FROM mpa WHERE id = 3", (rs, rowNum) ->
                        new Mpa(rs.getInt("id"), rs.getString("name"))))
                .build();
        film4Update.setGenres(new TreeSet<>());
        filmStorage.updateFilm(film4Update);
        Film film5 = filmStorage.getFilm(4);
        assertEquals(film4Update, film5);
    }

    @Test
    public void testDeleteFilm() {
        filmStorage.deleteFilm(2);
        List<Film> films = filmStorage.findAllFilms();
        assertTrue(films.contains(film1));
        assertTrue(films.contains(film3));
        assertFalse(films.contains(film2));
    }

    @Test
    public void testFindAllUFilmsId() {
        List<Integer> filmsId = filmStorage.findAllFilmsId();
        assertTrue(filmsId.contains(1));
        assertTrue(filmsId.contains(2));
        assertTrue(filmsId.contains(3));
    }
}

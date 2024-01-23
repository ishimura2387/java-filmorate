package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.TreeSet;

import static org.junit.Assert.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    FilmDbStorage filmStorage;
    Mpa mpa1 = new Mpa(1, "G1");
    Mpa mpa2 = new Mpa(2, "G2");
    Mpa mpa3 = new Mpa(3, "G3");

    Genre genre1 = Genre.builder()
            .id(1)
            .name("Комедия")
            .build();
    Genre genre2 = Genre.builder()
            .id(2)
            .name("Комедия")
            .build();
    Genre genre3 = Genre.builder()
            .id(3)
            .name("Триллер")
            .build();
    Film film1 = Film.builder()
            .name("Человек паук")
            .description("Описание к человеку пауку")
            .releaseDate(LocalDate.parse("2012-05-21"))
            .duration(120)
            .mpa(mpa1)
            .build();
    Film film2 = Film.builder()
            .name("Ирония судьбы")
            .description("Описание к иронии судьбы")
            .releaseDate(LocalDate.parse("1966-05-21"))
            .duration(120)
            .mpa(mpa2)
            .build();
    Film film3 = Film.builder()
            .name("Человек паук 2")
            .description("Описание к человеку пауку 2")
            .releaseDate(LocalDate.parse("2013-05-21"))
            .duration(122)
            .mpa(mpa2)
            .build();

    @BeforeEach
    public void beforeEach() {
        filmStorage = new FilmDbStorage(jdbcTemplate);
        jdbcTemplate.update("INSERT INTO mpa (id, name) VALUES (?, ?)", mpa1.getId(), mpa1.getName());
        jdbcTemplate.update("INSERT INTO mpa (id, name) VALUES (?, ?)", mpa2.getId(), mpa2.getName());
        jdbcTemplate.update("INSERT INTO mpa (id, name) VALUES (?, ?)", mpa3.getId(), mpa3.getName());
        jdbcTemplate.update("INSERT INTO genres (id, name) VALUES (?, ?)", genre1.getId(), genre1.getName());
        jdbcTemplate.update("INSERT INTO genres (id, name) VALUES (?, ?)", genre2.getId(), genre2.getName());
        jdbcTemplate.update("INSERT INTO genres (id, name) VALUES (?, ?)", genre3.getId(), genre3.getName());
        film1.setGenres(new TreeSet<>()));
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
                .name("Бешеные псы")
                .description("Описание псов")
                .releaseDate(LocalDate.parse("2006-05-21"))
                .duration(120)
                .mpa(mpa1)
                .build();
        film4.setGenres(new TreeSet<>());
        filmStorage.createNewFilm(film4);
        Film film5 = filmStorage.getFilm(4);
        assertEquals(film4, film5);
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
        Film film6 = Film.builder()
                .name("Три кота")
                .description("Описание трех котов")
                .releaseDate(LocalDate.parse("2009-05-21"))
                .duration(121)
                .mpa(mpa1)
                .build();
        film6.setGenres(new TreeSet<>());
        filmStorage.createNewFilm(film6);
        Film film6Update = Film.builder()
                .id(4)
                .name("Три кота улучшенные")
                .description("Описание трех котов улучшенных")
                .releaseDate(LocalDate.parse("2009-05-21"))
                .duration(121)
                .mpa(mpa1)
                .build();
        film6Update.setGenres(new TreeSet<>());
        filmStorage.updateFilm(film6Update);
        Film film7 = filmStorage.getFilm(4);
        assertEquals(film6Update, film7);
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

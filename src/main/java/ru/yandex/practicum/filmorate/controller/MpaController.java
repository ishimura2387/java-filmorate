package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/mpa")
public class MpaController {
    private final FilmService mpaService;

    @GetMapping
    public List<Mpa> findAllMpa() {
        List<Mpa> mpas = mpaService.findAllMpas();
        log.debug("Обработка запроса GET /mpa; Текущее количество mpa: {}",
                mpas.size());
        return mpas;
    }

    @GetMapping("/{id}")
    public Mpa getMpa(@PathVariable int id) {
        if (mpaService.findAllMpasId().contains(id)) {
            Mpa mpa = mpaService.getMpa(id);
            log.debug("Запрошен mpa: {}", mpa);
            return mpa;
        } else {
            log.debug("mpa не найден!");
            throw new NullObjectException("mpa не найден!");
        }
    }
}

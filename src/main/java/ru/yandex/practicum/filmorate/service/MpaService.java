package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NullObjectException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MpaService {
    @Autowired
    @Qualifier("mpaDbStorage")
    private MpaStorage mpaDbStorage;

    public List<Mpa> findAllMpas() {
        List<Mpa> mpas = mpaDbStorage.findAllMpas();
        log.debug("Обработка запроса GET /mpa; Текущее количество mpa: {}",
                mpas.size());
        return mpas;
    }

    public Mpa getMpa(int id) {
        checkMpas(id);
        Mpa mpa = mpaDbStorage.getMpa(id);
        log.debug("Запрошен mpa: {}", mpa);
        return mpa;
    }

    private void checkMpas(int id) {
        if (!mpaDbStorage.findAllMpasId().contains(id)) {
            log.debug("mpa не найден!");
            throw new NullObjectException("mpa не найден!");
        }
    }
}

package ru.practicum.compilations.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.service.CompilationService;

import javax.validation.constraints.Min;
import java.util.Collection;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Slf4j
public class PublicCompilationController {

    private final CompilationService serviceCompilation;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Collection<CompilationDto> getCompilation(
            // искать только закрепленные/не закрепленные подборки, возможно null
            @RequestParam(required = false) Boolean pinned,

            // количество элементов, которые нужно пропустить для формирования текущего набора
            @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,

            // количество элементов в наборе
            @RequestParam(required = false, defaultValue = "10") @Min(1) Integer size) {

        log.info("Get compilations with parameters pinned {} from {} size {}", pinned, from, size);
        return serviceCompilation.getAllCompilation(pinned, from, size);
    }

    @GetMapping("/{compId}")
    @ResponseStatus(value = HttpStatus.OK)
    public CompilationDto getCompilationById(@PathVariable Long compId) {
        log.info("Get compilation with id {}", compId);
        return serviceCompilation.getCompilationById(compId);
    }
}
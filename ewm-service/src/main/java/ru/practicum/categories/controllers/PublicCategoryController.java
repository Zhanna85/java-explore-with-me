package ru.practicum.categories.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.service.CategoryService;

import javax.validation.constraints.Min;
import java.util.Collection;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PublicCategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Collection<CategoryDto> getCategory(@RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
                                              @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("Get category with parameters from {} size {}", from, size);
        return categoryService.getCategory(from, size);
    }

    @GetMapping("/{catId}")
    @ResponseStatus(value = HttpStatus.OK)
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        log.info("Get category by Id {}", catId);
        return categoryService.getCategoryById(catId);
    }
}
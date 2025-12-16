package ru.rtc.medline.application.api.controller.rest;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rtc.medline.application.api.dto.category.CategoryDto;
import ru.rtc.medline.application.api.dto.category.CreateCategoryDto;
import ru.rtc.medline.application.api.dto.category.ListCategoryDto;
import ru.rtc.medline.application.api.converter.CategoryConverter;
import ru.rtc.medline.application.domain.model.Category;
import ru.rtc.medline.application.domain.service.CategoryService;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ListCategoryDto getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return CategoryConverter.toList(categories);
    }


    @GetMapping("/{id}")
    public CategoryDto get(@PathVariable UUID id) {
        Category category = categoryService.findById(id);
        return CategoryConverter.toDto(category);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER') ")
    public void create(@RequestBody CreateCategoryDto createCategoryDto) {
        categoryService.create(createCategoryDto);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void update(@PathVariable UUID id, @RequestBody CreateCategoryDto createCategoryDto) {
        categoryService.update(id, createCategoryDto);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER') ")
    public void delete(@PathVariable UUID id) {
        categoryService.delete(id);
    }

}

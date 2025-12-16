package ru.rtc.medline.application.domain.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rtc.medline.application.api.dto.category.CreateCategoryDto;
import ru.rtc.medline.application.domain.model.Category;
import ru.rtc.medline.application.domain.repository.CategoryRepository;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.getAllCategories().orElseThrow(RuntimeException::new);
    }

    public Category findById(UUID id) {
        return categoryRepository.getCategoryById(id).orElseThrow(RuntimeException::new);
    }

    public void create(CreateCategoryDto createCategoryDto) {

        Category category = new Category();

        category.setName(createCategoryDto.getName());

        categoryRepository.createCategory(category);

    }

    public void update(UUID id, CreateCategoryDto createCategoryDto) {
        Category category = categoryRepository.getCategoryById(id).orElseThrow(RuntimeException::new);

        category.setName(createCategoryDto.getName());
        categoryRepository.updateCategory(category);
    }

    public void delete(UUID id) {
        categoryRepository.deleteCategory(id).orElseThrow(RuntimeException::new);
    }
}

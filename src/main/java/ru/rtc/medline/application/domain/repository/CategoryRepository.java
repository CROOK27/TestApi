package ru.rtc.medline.application.domain.repository;


import ru.rtc.medline.application.domain.model.Category;
import ru.rtc.medline.application.domain.wrapper.ResultWrapper;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository {

    ResultWrapper<List<Category>> getAllCategories();

    ResultWrapper<Category> getCategoryById(UUID id);

    ResultWrapper<Void> createCategory(Category category);

    ResultWrapper<Void> updateCategory(Category category);

    ResultWrapper<Void> deleteCategory(UUID id);

}

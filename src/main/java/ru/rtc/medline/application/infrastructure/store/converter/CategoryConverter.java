package ru.rtc.medline.application.infrastructure.store.converter;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import ru.rtc.medline.application.domain.model.Category;
import ru.rtc.medline.application.infrastructure.store.entity.CategoryEntity;

import java.util.Collections;
import java.util.List;

public class CategoryConverter {

    public static CategoryEntity toEntity(Category category) {
        if (ObjectUtils.isEmpty(category)) {
            return null;
        }

        CategoryEntity categoryEntity = new CategoryEntity();

        categoryEntity.setId(category.getId());
        categoryEntity.setName(category.getName());

        return categoryEntity;
    }

    public static Category toCategory(CategoryEntity entity) {
        if (ObjectUtils.isEmpty(entity)) {
            return null;
        }

        Category category = new Category();

        category.setId(entity.getId());
        category.setName(entity.getName());

        return category;
    }

    public static List<Category> toCategories(List<CategoryEntity> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return Collections.emptyList();
        }

        return entities.stream()
                .map(CategoryConverter::toCategory)
                .toList();
    }

}

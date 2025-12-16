package ru.rtc.medline.application.api.converter;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import ru.rtc.medline.application.api.dto.category.CategoryDto;
import ru.rtc.medline.application.api.dto.category.CreateCategoryDto;
import ru.rtc.medline.application.api.dto.category.ListCategoryDto;
import ru.rtc.medline.application.domain.model.Category;

import java.util.List;

public class CategoryConverter {

    public static CategoryDto toDto(Category category) {
        if (ObjectUtils.isEmpty(category)) {
            return null;
        }

        CategoryDto dto = new CategoryDto();

        dto.setId(category.getId());
        dto.setName(category.getName());

        return dto;
    }

    public static Category fromDto(CreateCategoryDto dto) {
        if (ObjectUtils.isEmpty(dto)) {
            return null;
        }

        Category category = new Category();

        category.setName(dto.getName());

        return category;
    }

    public static ListCategoryDto toList(List<Category> categories) {
        if (CollectionUtils.isEmpty(categories)) {
            return null;
        }

        ListCategoryDto dto = new ListCategoryDto();

        dto.setCategories(
                categories.stream().
                        map(CategoryConverter::toDto)
                        .toList());

        return dto;
    }

}

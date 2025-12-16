package ru.rtc.medline.application.infrastructure.store.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.rtc.medline.application.domain.model.Category;
import ru.rtc.medline.application.domain.repository.CategoryRepository;
import ru.rtc.medline.application.domain.wrapper.ResultWrapper;
import ru.rtc.medline.application.infrastructure.store.converter.CategoryConverter;
import ru.rtc.medline.application.infrastructure.store.dao.CategoryDao;
import ru.rtc.medline.application.infrastructure.store.entity.CategoryEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class CategoryRepositoryJpa implements CategoryRepository {

    private final CategoryDao categoryDao;

    @Override
    public ResultWrapper<List<Category>> getAllCategories() {
        List<CategoryEntity> categoryEntities = categoryDao.findAll();

        return new ResultWrapper<>(CategoryConverter.toCategories(categoryEntities));
    }

    @Override
    public ResultWrapper<Category> getCategoryById(UUID id) {
        Optional<CategoryEntity> categoryEntity = categoryDao.findById(id);

        return categoryEntity.map(entity -> new ResultWrapper<>(
                CategoryConverter.toCategory(entity))).orElseGet(ResultWrapper::new);
    }

    @Override
    public ResultWrapper<Void> createCategory(Category category) {
        CategoryEntity categoryEntity = CategoryConverter.toEntity(category);

        categoryEntity.setId(UUID.randomUUID());
        categoryDao.save(categoryEntity);

        return new ResultWrapper<>();
    }

    @Override
    public ResultWrapper<Void> updateCategory(Category category) {
        CategoryEntity categoryEntity = CategoryConverter.toEntity(category);

        categoryDao.save(categoryEntity);

        return new ResultWrapper<>();
    }

    @Override
    public ResultWrapper<Void> deleteCategory(UUID id) {
        categoryDao.deleteById(id);
        return new ResultWrapper<>();
    }
}

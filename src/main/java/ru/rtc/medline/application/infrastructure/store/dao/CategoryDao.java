package ru.rtc.medline.application.infrastructure.store.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rtc.medline.application.infrastructure.store.entity.CategoryEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryDao extends JpaRepository<CategoryEntity, UUID> {

    Optional<CategoryEntity> findById(UUID uuid);

}
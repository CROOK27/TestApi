package ru.rtc.medline.application.infrastructure.store.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.rtc.medline.application.infrastructure.store.entity.ProductEntity;

import java.util.UUID;

@Repository
public interface ProductDao extends JpaRepository<ProductEntity, UUID>, JpaSpecificationExecutor<ProductEntity> {

    @EntityGraph(attributePaths = {ProductEntity.PRODUCT_CATEGORY})
    Page<ProductEntity> findAll(Specification<ProductEntity> spec, Pageable pageable);

}

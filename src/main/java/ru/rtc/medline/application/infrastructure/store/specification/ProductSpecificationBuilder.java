package ru.rtc.medline.application.infrastructure.store.specification;

import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.rtc.medline.application.domain.model.filter.ProductFilter;
import ru.rtc.medline.application.infrastructure.store.entity.CategoryEntity;
import ru.rtc.medline.application.infrastructure.store.entity.ProductEntity;

import java.util.UUID;

@Component
public class ProductSpecificationBuilder implements SpecificationBuilder<ProductEntity> {

    JoinSpecification<Object, ProductEntity> configJoin = join(
            ProductEntity.PRODUCT_CATEGORY,
            JoinType.LEFT
    );

    public Specification<ProductEntity> toSpecification(ProductFilter filter) {

        String id = filter.getCategoryId();

        UUID categoryId = null;

        if (StringUtils.hasText(id)) {
            categoryId = UUID.fromString(id);
        }

        return like(
                ProductEntity.PRODUCT_NAME,
                filter.getName(),
                LikeType.CONTAINS
        ).and(
                        equalField(
                                CategoryEntity.CATEGORY_ID,
                                categoryId,
                                configJoin
                        )
                )
                .and(
                        between(
                                ProductEntity.PRODUCT_PRICE,
                                filter.getMinPrice(),
                                filter.getMaxPrice()
                        )
                );
    }

}

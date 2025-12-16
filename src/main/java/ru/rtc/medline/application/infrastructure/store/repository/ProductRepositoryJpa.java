package ru.rtc.medline.application.infrastructure.store.repository;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import ru.rtc.medline.application.domain.converter.WrapperConverter;
import ru.rtc.medline.application.domain.model.Product;
import ru.rtc.medline.application.domain.model.filter.PageFilter;
import ru.rtc.medline.application.domain.model.filter.ProductFilter;
import ru.rtc.medline.application.domain.repository.ProductRepository;
import ru.rtc.medline.application.domain.wrapper.PageWrapper;
import ru.rtc.medline.application.domain.wrapper.ResultWrapper;
import ru.rtc.medline.application.infrastructure.store.converter.PageFilterConverter;
import ru.rtc.medline.application.infrastructure.store.converter.ProductConverter;
import ru.rtc.medline.application.infrastructure.store.dao.ProductDao;
import ru.rtc.medline.application.infrastructure.store.entity.ProductEntity;
import ru.rtc.medline.application.infrastructure.store.specification.ProductSpecificationBuilder;

import java.util.UUID;

@Repository
@AllArgsConstructor
public class ProductRepositoryJpa implements ProductRepository {

    private final ProductDao productDao;
    private final ProductSpecificationBuilder productSpecificationBuilder;

    @Override
    public ResultWrapper<Void> save(Product product) {
        product.setId(UUID.randomUUID());
        productDao.save(ProductConverter.toProductEntity(product));
        return new ResultWrapper<>();
    }

    @Override
    public ResultWrapper<PageWrapper<Product>> getProducts(ProductFilter productFilter, PageFilter pageFilter) {
        Specification<ProductEntity> reportEntitySpecification = productSpecificationBuilder.toSpecification(productFilter);

        Pageable campaignPageable = PageFilterConverter.toPageable(pageFilter);

        Page<ProductEntity> reportEntityPage = productDao.findAll(reportEntitySpecification, campaignPageable);

        PageWrapper<Product> productPageWrapper = WrapperConverter.toPageWrapper(
                reportEntityPage,
                ProductConverter::toProduct
        );

        return new ResultWrapper<>(productPageWrapper);
    }

    @Override
    public ResultWrapper<Product> getProduct(UUID id) {
        ProductEntity productEntity = productDao.findById(id).orElse(null);
        return new ResultWrapper<>(ProductConverter.toProduct(productEntity));
    }


    @Override
    public ResultWrapper<Void> update(UUID uuid, Product product) {
        ProductEntity productEntity = productDao.findById(uuid).orElse(null);
        if (productEntity == null) {
            return new ResultWrapper<>();
        }
        productEntity.setName(product.getName());
        productEntity.setPrice(product.getPrice());
        productEntity.setCount(product.getCount());
        productEntity.setCharacteristics(product.getCharacteristics());

        productDao.save(productEntity);

        return new ResultWrapper<>();
    }

    @Override
    public ResultWrapper<Void> delete(UUID id) {
        productDao.deleteById(id);
        return new ResultWrapper<>();
    }
}

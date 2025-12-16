package ru.rtc.medline.application.domain.repository;

import ru.rtc.medline.application.domain.model.Product;
import ru.rtc.medline.application.domain.model.filter.PageFilter;
import ru.rtc.medline.application.domain.model.filter.ProductFilter;
import ru.rtc.medline.application.domain.wrapper.PageWrapper;
import ru.rtc.medline.application.domain.wrapper.ResultWrapper;

import java.util.UUID;

public interface ProductRepository {

    ResultWrapper<Void> save(Product product);

    ResultWrapper<PageWrapper<Product>> getProducts(ProductFilter productFilter, PageFilter pageFilter);

    ResultWrapper<Product> getProduct(UUID id);

    ResultWrapper<Void> update(UUID uuid,  Product product);

    ResultWrapper<Void> delete(UUID id);

}

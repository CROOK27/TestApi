package ru.rtc.medline.application.infrastructure.store.converter;

import org.springframework.util.ObjectUtils;
import ru.rtc.medline.application.domain.model.Product;
import ru.rtc.medline.application.infrastructure.store.entity.ProductEntity;

public class ProductConverter {

    public static Product toProduct(ProductEntity productEntity) {
        if (ObjectUtils.isEmpty(productEntity)) {
            return null;
        }

        Product product = new Product();

        product.setId(productEntity.getId());
        product.setName(productEntity.getName());
        product.setPrice(productEntity.getPrice());
        product.setCount(productEntity.getCount());
        product.setCharacteristics(productEntity.getCharacteristics());

        return product;
    }

    public static ProductEntity toProductEntity(Product product) {
        if (ObjectUtils.isEmpty(product)) {
            return null;
        }

        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(product.getId());
        productEntity.setName(product.getName());
        productEntity.setPrice(product.getPrice());
        productEntity.setCount(product.getCount());
        productEntity.setCharacteristics(product.getCharacteristics());
        productEntity.setCategory(CategoryConverter.toEntity(product.getCategory()));

        return productEntity;
    }

}

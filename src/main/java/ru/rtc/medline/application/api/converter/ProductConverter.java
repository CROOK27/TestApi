package ru.rtc.medline.application.api.converter;

import org.springframework.util.ObjectUtils;
import ru.rtc.medline.application.api.dto.CreateProductDto;
import ru.rtc.medline.application.api.dto.ProductDto;
import ru.rtc.medline.application.domain.model.Product;

public class ProductConverter {

    public static ProductDto toProductDto(Product product) {
        if (ObjectUtils.isEmpty(product)) return null;

        ProductDto productDto = new ProductDto();

        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setCount(product.getCount());
        productDto.setCharacteristics(product.getCharacteristics());
        productDto.setRating(product.getRating());

        return productDto;
    }

    public static Product toProduct(CreateProductDto productDto) {
        if (ObjectUtils.isEmpty(productDto)) return null;

        Product product = new Product();

        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setCount(productDto.getCount());
        product.setCharacteristics(productDto.getCharacteristics());
        product.setId(productDto.getCategoryId());

        return product;
    }


}

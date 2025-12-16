package ru.rtc.medline.application.domain.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rtc.medline.application.domain.model.Product;
import ru.rtc.medline.application.domain.model.filter.PageFilter;
import ru.rtc.medline.application.domain.model.filter.ProductFilter;
import ru.rtc.medline.application.domain.repository.ProductRepository;
import ru.rtc.medline.application.domain.wrapper.PageWrapper;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public PageWrapper<Product> getAllProducts(ProductFilter productFilter, PageFilter pageFilter) {
        return productRepository.getProducts(productFilter, pageFilter)
                .orElseThrow(RuntimeException::new);
    }

    public Product getProduct(UUID id) {
        return productRepository.getProduct(id).orElseThrow(RuntimeException::new);
    }

    public void deleteProduct(UUID id) {
        productRepository.delete(id);
    }

    public void updateProduct(UUID id, Product product) {
        productRepository.update(id, product);
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

}

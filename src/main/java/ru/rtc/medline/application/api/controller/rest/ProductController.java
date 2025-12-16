package ru.rtc.medline.application.api.controller.rest;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rtc.medline.application.api.converter.ProductConverter;
import ru.rtc.medline.application.api.converter.WrapperConverter;
import ru.rtc.medline.application.api.converter.filter.PageFilterConverter;
import ru.rtc.medline.application.api.converter.filter.ProductFilterConverter;
import ru.rtc.medline.application.api.dto.CreateProductDto;
import ru.rtc.medline.application.api.dto.PageDto;
import ru.rtc.medline.application.api.dto.ProductDto;
import ru.rtc.medline.application.api.dto.filter.PageFilterDto;
import ru.rtc.medline.application.api.dto.filter.ProductFilterDto;
import ru.rtc.medline.application.domain.model.Product;
import ru.rtc.medline.application.domain.model.filter.PageFilter;
import ru.rtc.medline.application.domain.model.filter.ProductFilter;
import ru.rtc.medline.application.domain.service.ProductService;
import ru.rtc.medline.application.domain.wrapper.PageWrapper;

import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public PageDto<ProductDto> getProducts(
            @ParameterObject @Parameter(description = "Параметры фильтрации продуктов")
            ProductFilterDto productFilterDto,
            @ParameterObject @Parameter(description = "Параметры пагинации")
            PageFilterDto pageFilterDto
    ) {
        ProductFilter productFilter = ProductFilterConverter.toReportFilter(productFilterDto);
        PageFilter pageFilter = PageFilterConverter.toPageFilter(pageFilterDto);

        PageWrapper<Product> productPage = productService.getAllProducts(productFilter, pageFilter);

        return WrapperConverter.toPageDto(
                productPage,
                ProductConverter::toProductDto
        );
    }

    @GetMapping("/{id}")
    public ProductDto getProduct(@PathVariable UUID id) {
        return ProductConverter.toProductDto(productService.getProduct(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER') ")
    public void create(@RequestBody CreateProductDto createCategoryDto) {
        productService.saveProduct(ProductConverter.toProduct(createCategoryDto));
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER') ")
    public void update(@PathVariable UUID id, @RequestBody CreateProductDto createCategoryDto) {
        productService.updateProduct(id, ProductConverter.toProduct(createCategoryDto));
    }
    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        productService.deleteProduct(id);
    }


}

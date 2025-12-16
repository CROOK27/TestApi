package ru.rtc.medline.application.api.converter.filter;

import org.springframework.util.ObjectUtils;
import ru.rtc.medline.application.api.dto.filter.ProductFilterDto;
import ru.rtc.medline.application.domain.model.filter.ProductFilter;

public class ProductFilterConverter {

    public static ProductFilter toReportFilter(ProductFilterDto reportFilterDto) {
        if (ObjectUtils.isEmpty(reportFilterDto)) {
            return null;
        }

        ProductFilter productFilter = new ProductFilter();

        productFilter.setCategoryId(reportFilterDto.getCategoryId());
        productFilter.setName(reportFilterDto.getName());
        productFilter.setMaxPrice(reportFilterDto.getMaxPrice());
        productFilter.setMinPrice(reportFilterDto.getMinPrice());

        return productFilter;
    }

}

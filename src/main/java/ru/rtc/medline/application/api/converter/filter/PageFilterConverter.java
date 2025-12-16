package ru.rtc.medline.application.api.converter.filter;

import org.springframework.util.ObjectUtils;
import ru.rtc.medline.application.api.dto.filter.PageFilterDto;
import ru.rtc.medline.application.domain.model.filter.PageFilter;

public class PageFilterConverter {

    public static PageFilter toPageFilter(PageFilterDto pageFilterDto) {
        if (ObjectUtils.isEmpty(pageFilterDto)) {
            return null;
        }

        PageFilter pageFilter = new PageFilter();

        pageFilter.setPageNumber(pageFilterDto.getPageNumber());
        pageFilter.setPageSize(pageFilterDto.getPageSize());

        return pageFilter;
    }

}

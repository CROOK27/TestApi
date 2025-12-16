package ru.rtc.medline.application.infrastructure.store.converter;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;
import ru.rtc.medline.application.domain.model.filter.PageFilter;

public class PageFilterConverter {

    public static Pageable toPageable(PageFilter pageFilter, Sort sort) {
        Sort currentSort = sort;

        if (ObjectUtils.isEmpty(currentSort)) {
            currentSort = Sort.unsorted();
        }

        if (ObjectUtils.isEmpty(pageFilter)) {
            return Pageable.unpaged(currentSort);
        }

        return PageRequest.of(
                pageFilter.getPageNumber(),
                pageFilter.getPageSize(),
                currentSort
        );
    }

    public static Pageable toPageable(PageFilter pageFilter) {
        Sort currentSort = Sort.unsorted();

        if (ObjectUtils.isEmpty(pageFilter)) {
            return Pageable.unpaged(currentSort);
        }

        return PageRequest.of(
                pageFilter.getPageNumber(),
                pageFilter.getPageSize(),
                currentSort
        );
    }

}

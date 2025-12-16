package ru.rtc.medline.application.api.converter;

import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import ru.rtc.medline.application.api.dto.PageDto;
import ru.rtc.medline.application.domain.wrapper.PageWrapper;


import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class WrapperConverter {

    public static <R, T> PageDto<R> toPageDto(
            PageWrapper<T> page,
            Function<T, R> objectMapFunction
    ) {
        if (ObjectUtils.isEmpty(page)) {
            return null;
        }

        PageDto<R> wrapper = new PageDto<>();

        wrapper.setElements(
                toList(
                        page.getElements(),
                        objectMapFunction
                )
        );
        wrapper.setPageNumber(page.getPageNumber());
        wrapper.setPageSize(page.getPageSize());
        wrapper.setTotalElementsCount(page.getTotalElementsCount());

        return wrapper;
    }

    public static <R, T> PageDto<R> toPageDto(
            List<T> elements,
            Function<T, R> objectMapFunction
    ) {
        PageDto<R> wrapper = new PageDto<>();

        if (CollectionUtils.isEmpty(elements)) {
            wrapper.setElements(Collections.emptyList());
            return wrapper;
        }

        wrapper.setElements(
                toList(
                        elements,
                        objectMapFunction
                )
        );

        return wrapper;
    }

    public static <R, T> List<R> toList(
            Collection<T> objects,
            Function<T, R> objectMapFunction
    ) {
        if (CollectionUtils.isEmpty(objects)) {
            return Collections.emptyList();
        }

        return objects.stream()
                .map(objectMapFunction)
                .toList();
    }

}

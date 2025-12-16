package ru.rtc.medline.application.domain.converter;

import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import ru.rtc.medline.application.domain.wrapper.PageWrapper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class WrapperConverter {

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

    public static <R, T> PageWrapper<R> toPageWrapper(
            Page<T> page,
            Function<T, R> objectMapFunction
    ) {
        if (ObjectUtils.isEmpty(page)) {
            return null;
        }

        PageWrapper<R> wrapper = new PageWrapper<>();

        wrapper.setElements(
                toList(
                        page.getContent(),
                        objectMapFunction
                )
        );
        wrapper.setPageNumber(page.getNumber());
        wrapper.setPageSize(page.getSize());
        wrapper.setTotalElementsCount(page.getTotalElements());

        return wrapper;
    }

}

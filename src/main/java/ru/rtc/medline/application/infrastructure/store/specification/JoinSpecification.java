package ru.rtc.medline.application.infrastructure.store.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;

@FunctionalInterface
public interface JoinSpecification<Z,X> {

    Join<Z,X> toJoin(Root<X> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder);

}

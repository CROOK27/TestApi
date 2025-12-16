package ru.rtc.medline.application.infrastructure.store.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.CastUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Locale;
import java.util.UUID;

public interface SpecificationBuilder<T> {

    String DATE_FUNCTION_NAME = "DATE";
    Specification<?> EMPTY_SPECIFICATION = (root, query, criteriaBuilder) -> null;

    default Specification<T> isFieldNull(String field) {
        return (root, query, criteriaBuilder) -> root.get(field).isNull();
    }

    default Specification<T> isFieldNotNull(String field) {
        return (root, query, criteriaBuilder) -> root.get(field).isNotNull();
    }

    default Specification<T> inField(String field, Collection<?> values) {
        if (CollectionUtils.isEmpty(values)) {
            return empty();
        }

        return (root, query, criteriaBuilder) -> root.get(field).in(values);
    }

    default Specification<T> lessThanOrEqualTo(String field, LocalDate value) {
        if (ObjectUtils.isEmpty(value)) {
            return empty();
        }

        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(
                root.get(field),
                value
        );
    }

    default Specification<T> lessThanOrEqualTo(String field, OffsetDateTime temporalAccessor, JoinSpecification<?, T> joinSpecification) {
        return (root, query, criteriaBuilder) -> {
            Join<?, T> join = joinSpecification.toJoin(root, query, criteriaBuilder);

            return criteriaBuilder.lessThanOrEqualTo(
                    join.get(field),
                    temporalAccessor
            );
        };
    }

    default Specification<T> lessThanOrEqualTo(String field, Long value) {
        if (ObjectUtils.isEmpty(value)) {
            return empty();
        }

        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(
                root.get(field),
                value
        );
    }

    default Specification<T> greaterThanOrEqualTo(String field, LocalDate value) {
        if (ObjectUtils.isEmpty(value)) {
            return empty();
        }

        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(field), value);
    }

    default Specification<T> greaterThanOrEqualTo(String field, Long value) {
        if (ObjectUtils.isEmpty(value)) {
            return empty();
        }

        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(field), value);
    }

   default Specification<T> greaterThanOrEqualTo(String field, OffsetDateTime value, JoinSpecification<?, T> joinSpecification) {
        return (root, query, criteriaBuilder) -> {
            Join<?, T> join = joinSpecification.toJoin(root, query, criteriaBuilder);

            return criteriaBuilder.greaterThanOrEqualTo(join.get(field), value);
        };
    }

    default Specification<T> and(Specification<T> first, Specification<T> second) {
        if (first.equals(EMPTY_SPECIFICATION)) {
            return second;
        }

        if (second.equals(EMPTY_SPECIFICATION)) {
            return first;
        }

        return first.and(second);
    }

    default Specification<T> empty() {
        return CastUtils.cast(EMPTY_SPECIFICATION);
    }

    default Specification<T> equalField(String field, Object value) {
        if (value == null) {
            return empty();
        }

        if (value instanceof String && !StringUtils.hasText((CharSequence) value)) {
            return empty();
        }

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(field), value);
    }

    default Specification<T> equalFieldUuid(String field, String value) {
        if (value == null) {
            return empty();
        }

        if (!StringUtils.hasText((CharSequence) value)) {
            return empty();
        }

        UUID uuidValue = UUID.fromString(value);
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(field), uuidValue);
    }

    default JoinSpecification<Object, T> join(String field, JoinType joinType) {
        return (root, query, criteriaBuilder) -> root.join(field, joinType);
    }

    default JoinSpecification<Object, T> join(String field, JoinType joinType, JoinSpecification<?, T> joinSpecification) {
        return (root, query, criteriaBuilder) -> {
            Join<?, T> join = joinSpecification.toJoin(root, query, criteriaBuilder);
            return join.join(field, joinType);
        };
    }

    default Specification<T> isFieldNull(JoinSpecification<?, T> joinSpecification) {
        return (root, query, criteriaBuilder) -> {
            Join<?, T> join = joinSpecification.toJoin(root, query, criteriaBuilder);
            return criteriaBuilder.isNull(join);
        };
    }

    default Specification<T> equalField(String field, Object value, JoinSpecification<?, T> joinSpecification) {
        if (value == null) {
            return empty();
        }

        if (value instanceof String && !StringUtils.hasText((CharSequence) value)) {
            return empty();
        }

        return (root, query, criteriaBuilder) -> {
            Join<?, T> join = joinSpecification.toJoin(root, query, criteriaBuilder);
            return criteriaBuilder.equal(join.get(field), value);
        };
    }

    default Specification<T> inField(String field, Collection<?> values, JoinSpecification<?, T> joinSpecification) {
        if (CollectionUtils.isEmpty(values)) {
            return empty();
        }

        return (root, query, criteriaBuilder) -> {
            Join<?, T> join = joinSpecification.toJoin(root, query, criteriaBuilder);
            return join.get(field).in(values);
        };
    }

    default Specification<T> inField(String field, String value, JoinSpecification<?, T> joinSpecification) {
        if (ObjectUtils.isEmpty(value)) {
            return empty();
        }

        return (root, query, criteriaBuilder) -> {
            Join<?, T> join = joinSpecification.toJoin(root, query, criteriaBuilder);
            return join.get(field).in(value);
        };
    }

    default Specification<T> between(String field, LocalDate startDate, LocalDate endDate) {
        if (ObjectUtils.isEmpty(startDate) || ObjectUtils.isEmpty(endDate)) {
            return empty();
        }

        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get(field), startDate, endDate);
    }

    default Specification<T> dateFilter(String field, LocalDate startDate, LocalDate endDate) {
        if (!ObjectUtils.isEmpty(startDate) && !ObjectUtils.isEmpty(endDate)) {
            return between(field, startDate, endDate);
        } else if (!ObjectUtils.isEmpty(startDate)) {
            return greaterThanOrEqualTo(field, startDate);
        } else if (!ObjectUtils.isEmpty(endDate)) {
            return lessThanOrEqualTo(field, endDate);
        }

        return empty();
    }

    default Specification<T> dateFilterWithFunction(String field, LocalDate startDate, LocalDate endDate) {
        if (!ObjectUtils.isEmpty(startDate) && !ObjectUtils.isEmpty(endDate)) {
            return betweenWithFunction(field, startDate, endDate);
        } else if (!ObjectUtils.isEmpty(startDate)) {
            return greaterThanOrEqualToWithFunction(field, startDate);
        } else if (!ObjectUtils.isEmpty(endDate)) {
            return lessThanOrEqualToWithFunction(field, endDate);
        }

        return empty();
    }

    default Specification<T> lessThanOrEqualToWithFunction(String field, LocalDate value) {
        if (ObjectUtils.isEmpty(value)) {
            return empty();
        }

        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(
                criteriaBuilder.function(DATE_FUNCTION_NAME, LocalDate.class, root.get(field)),
                value
        );
    }

    default Specification<T> greaterThanOrEqualToWithFunction(String field, LocalDate value) {
        if (ObjectUtils.isEmpty(value)) {
            return empty();
        }

        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(
                criteriaBuilder.function(DATE_FUNCTION_NAME, LocalDate.class, root.get(field)),
                value
        );
    }

    default Specification<T> betweenWithFunction(String field, LocalDate startDate, LocalDate endDate) {
        if (ObjectUtils.isEmpty(startDate) || ObjectUtils.isEmpty(endDate)) {
            return empty();
        }

        return (root, query, criteriaBuilder) -> criteriaBuilder.between(criteriaBuilder.function(DATE_FUNCTION_NAME, LocalDate.class, root.get(field)), startDate, endDate);
    }

    default Specification<T> isFieldNullable(String field, Boolean isNull) {
        if (ObjectUtils.isEmpty(isNull)) {
            return empty();
        }

        return isNull ? isFieldNull(field) : isFieldNotNull(field);
    }

    default Specification<T> isFieldNotNullable(String field, Boolean isNotNull) {
        if (ObjectUtils.isEmpty(isNotNull)) {
            return empty();
        }

        return isNotNull ? isFieldNotNull(field) : isFieldNull(field);
    }

    default Specification<T> like(
            String field,
            String value,
            LikeType likeType
    ) {
        if (!StringUtils.hasText(value)) {
            return empty();
        }

        String regexpValue = MessageFormat.format(
                likeType.getPattern(),
                value.toLowerCase(Locale.ROOT)
        );

        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get(field)),
                regexpValue
        );
    }

    default Specification<T> between(String field, Integer minValue, Integer maxValue) {
        if (ObjectUtils.isEmpty(minValue) || ObjectUtils.isEmpty(maxValue)) {
            return empty();
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get(field), minValue, maxValue);
    }

}

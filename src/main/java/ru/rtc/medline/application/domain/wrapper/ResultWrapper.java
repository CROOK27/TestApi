package ru.rtc.medline.application.domain.wrapper;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.ObjectUtils;

import java.util.Map;
import java.util.function.Supplier;

@Getter
@Setter
public class ResultWrapper<T> {

    private T result;

    private ResultStatus resultStatus;

    public ResultWrapper(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }

    public ResultWrapper() {
        this.resultStatus = ResultStatus.OK;
    }

    public ResultWrapper(T result) {
        this.result = result;
        this.resultStatus = ResultStatus.OK;
    }

    public boolean isSuccess() {
        return ResultStatus.OK.equals(resultStatus);
    }

    public boolean isNotSuccess() {
        return !isSuccess();
    }

    /**
     * Аналог метода из Optional
     */
    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (isSuccess()) {
            return result;
        } else {
            throw exceptionSupplier.get();
        }
    }

    /**
     * Аналог метода из Optional
     */
    public T orElseThrow(Map<ResultStatus, Supplier<? extends RuntimeException>> statusMapper, Supplier<? extends RuntimeException> exceptionSupplier) {
        if (isSuccess()) {
            return result;
        }

        Supplier<? extends RuntimeException> supplier = statusMapper.get(resultStatus);
        if (ObjectUtils.isEmpty(supplier)) {
            supplier = exceptionSupplier;
        }

        if (!ObjectUtils.isEmpty(supplier)) {
            throw supplier.get();
        }

        return result;
    }

    /**
     * Аналог метода из Optional
     */
    public T orElse(T other) {
        return isSuccess() ? result : other;
    }

    /**
     * Аналог метода из Optional
     */
    public static <T> ResultWrapper<T> ofNullable(T result, ResultStatus resultStatus) {
        if (ObjectUtils.isEmpty(result)) {
            return new ResultWrapper<>(resultStatus);
        }
        return new ResultWrapper<>(result);
    }

}

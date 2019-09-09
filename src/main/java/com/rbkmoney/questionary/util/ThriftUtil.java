package com.rbkmoney.questionary.util;

import com.rbkmoney.geck.common.util.TBaseUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.thrift.TBase;

import java.util.function.Consumer;
import java.util.function.Predicate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ThriftUtil {

    public static <T extends TBase> void setIfNotEmpty(T value, Consumer<T> consumer) {
        if (value != null && TBaseUtil.getSetFieldsCount(value) > 0) {
            consumer.accept(value);
        }
    }

    public static <T extends TBase> void setIfPredicate(T value, Predicate<T> predicate, Consumer<T> consumer) {
        if (predicate.test(value)) {
            consumer.accept(value);
        }
    }

}

package cn.stareye.opensource.enumeration.std.serialization;

import cn.stareye.opensource.enumeration.std.Enumeration;
import cn.stareye.opensource.enumeration.std.EnumerationAttribute;
import org.springframework.lang.NonNull;

/**
 * 枚举类序列化和容器化的策略, 主要有两种:
 * 1. 以{@link Enumeration#name()}方法作为序列化和反序列化以及容器化特化标准.
 * 2. 以{@link Enumeration#ordinal()}方法作为序列化和反序列化以及容器化特化标准.
 *
 * @author: wjf
 * @date: 2022/7/26
 */
public enum EnumerationJsonStrategy implements SerializationSupplier {

    /**
     * Enumeration#name().
     */
    NAME {
        @NonNull
        @Override
        public BaseEnumerationJsonSerializer serializer(@NonNull EnumerationAttribute enumerationAttribute) {
            return new EnumerationJsonNameObjectSerializer(enumerationAttribute);
        }

        @NonNull
        @Override
        @SuppressWarnings("rawtypes")
        public BaseEnumerationJsonDeserializer deserializer(@NonNull EnumerationAttribute enumerationAttribute) {
            return new EnumerationJsonNameObjectDeserializer(enumerationAttribute);
        }
    },
    /**
     * Enumeration#ordinal().
     */
    ORDINAL {
        @NonNull
        @Override
        public BaseEnumerationJsonSerializer serializer(@NonNull EnumerationAttribute enumerationAttribute) {
            return new EnumerationJsonOrdinalObjectSerializer(enumerationAttribute);
        }

        @NonNull
        @Override
        @SuppressWarnings("rawtypes")
        public BaseEnumerationJsonDeserializer deserializer(@NonNull EnumerationAttribute enumerationAttribute) {
            return new EnumerationJsonOrdinalObjectDeserializer(enumerationAttribute);
        }
    },

}

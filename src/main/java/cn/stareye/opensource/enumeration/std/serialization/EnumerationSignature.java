package cn.stareye.opensource.enumeration.std.serialization;

import cn.stareye.opensource.enumeration.std.Enumeration;
import cn.stareye.opensource.enumeration.std.EnumerationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * {@link EnumerationSerialization}标签, 一个枚举类只会有一个EnumerationSerializationSignature实例.
 *
 * @author: wjf
 * @date: 2022/7/26
 */
public class EnumerationSignature {

    private final Class<? extends Enumeration<?>> enumClass;

    private final Map<String, Enumeration<?>> enumerationNameMap;

    private final Map<Integer, Enumeration<?>> enumerationOrdinalMap;

    private final List<EnumerationPropertySignature> enumerationPropertySignatures;

    public static EnumerationSignature fromEnumeration(@NonNull Class<? extends Enumeration<?>> enumClass) {
        return new EnumerationSignature(enumClass);
    }

    private EnumerationSignature(@NonNull Class<? extends Enumeration<?>> enumClass) {
        this.enumClass = Objects.requireNonNull(enumClass);
        this.enumerationNameMap = initEnumerationNameMap();
        this.enumerationOrdinalMap = initEnumerationOrdinalMap();
        this.enumerationPropertySignatures = initEnumerationPropertySignatures();
    }

    private Map<String, Enumeration<?>> initEnumerationNameMap() {
        Enumeration<?>[] enumConstants = this.enumClass.getEnumConstants();
        return Arrays.stream(enumConstants)
                .collect(Collectors.toMap(Enumeration::name, enumeration -> enumeration));
    }

    protected Map<Integer, Enumeration<?>> initEnumerationOrdinalMap() {
        Enumeration<?>[] enumConstants = this.enumClass.getEnumConstants();
        return Arrays.stream(enumConstants)
                .collect(Collectors.toMap(Enumeration::ordinal, enumeration -> enumeration));
    }

    private List<EnumerationPropertySignature> initEnumerationPropertySignatures() {
        Field[] fields = this.enumClass.getDeclaredFields();
        return Arrays.stream(fields)
                .filter(field -> field.isAnnotationPresent(EnumerationSerialization.class))
                .map(EnumerationPropertySignature::fromProperty)
                .toList();
    }

    protected Map<String, Enumeration<?>> getEnumerationNameMap() {
        return enumerationNameMap;
    }

    protected Map<Integer, Enumeration<?>> getEnumerationOrdinalMap() {
        return enumerationOrdinalMap;
    }

    protected List<EnumerationPropertySignature> getEnumerationPropertySignatures() {
        return enumerationPropertySignatures;
    }

    public static class EnumerationPropertySignature {

        private final Field field;

        private final String fieldName;

        private final String fieldAlias;

        private final EnumerationSerialization enumerationSerialization;

        public static EnumerationPropertySignature fromProperty(@NonNull Field field) {
            return new EnumerationPropertySignature(field);
        }

        private EnumerationPropertySignature(@NonNull Field field) {
            this.field = Objects.requireNonNull(field);
            this.fieldName = this.field.getName();
            this.enumerationSerialization = this.field.getAnnotation(EnumerationSerialization.class);
            this.fieldAlias = alias();
        }

        private String alias() {
            String name = this.field.getName();
            if (StringUtils.isNotBlank(this.enumerationSerialization.value())) {
                name = this.enumerationSerialization.value();
            }
            if (StringUtils.isNotBlank(this.enumerationSerialization.alias())) {
                name = this.enumerationSerialization.alias();
            }
            return name.trim();
        }

        protected Object readValue(Enumeration<?> enumeration) {
            try {
                this.field.trySetAccessible();
                return this.field.get(enumeration);
            } catch (IllegalAccessException e) {
                throw EnumerationException.newEx(e.toString());
            }
        }

        protected String getFieldName() {
            return fieldName;
        }

        protected String getFieldAlias() {
            return fieldAlias;
        }
    }

}

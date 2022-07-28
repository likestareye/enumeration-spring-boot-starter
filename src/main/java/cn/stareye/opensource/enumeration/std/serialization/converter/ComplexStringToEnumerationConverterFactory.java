package cn.stareye.opensource.enumeration.std.serialization.converter;

import cn.stareye.opensource.enumeration.std.Enumeration;
import cn.stareye.opensource.enumeration.std.EnumerationAttribute;
import cn.stareye.opensource.enumeration.std.EnumerationException;
import cn.stareye.opensource.enumeration.std.serialization.EnumerationJsonStrategy;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Map;

/**
 * 字符串反序列化为枚举.
 *
 * @author: wjf
 * @date: 2022/7/28
 */
@SuppressWarnings("rawtypes")
public class ComplexStringToEnumerationConverterFactory implements ConverterFactory<String, Enumeration> {

    private final ObjectMapper objectMapper;

    private final EnumerationAttribute enumerationAttribute;

    private final PrimitiveStringToEnumerationConverterFactory primitiveStringToEnumerationConverterFactory;

    private final PrimitiveIntToEnumerationConverterFactory primitiveIntToEnumerationConverterFactory;


    public ComplexStringToEnumerationConverterFactory(
            @NonNull ObjectMapper objectMapper,
            @NonNull EnumerationAttribute enumerationAttribute,
            @NonNull PrimitiveStringToEnumerationConverterFactory primitiveStringToEnumerationConverterFactory,
            @NonNull PrimitiveIntToEnumerationConverterFactory primitiveIntToEnumerationConverterFactory
    ) {
        this.objectMapper = objectMapper;
        this.enumerationAttribute = enumerationAttribute;
        this.primitiveStringToEnumerationConverterFactory = primitiveStringToEnumerationConverterFactory;
        this.primitiveIntToEnumerationConverterFactory = primitiveIntToEnumerationConverterFactory;
    }

    @NonNull
    @Override
    @SuppressWarnings("all")
    public <T extends Enumeration> Converter<String, T> getConverter(@NonNull Class<T> targetType) {
        return new ComplexStringToEnumerationConverter(
                targetType,
                this.primitiveStringToEnumerationConverterFactory,
                this.primitiveIntToEnumerationConverterFactory,
                this.objectMapper,
                this.enumerationAttribute
        );
    }

    public static class ComplexStringToEnumerationConverter<E extends Enum<E> & Enumeration<E>> implements Converter<String, E> {

        private final Class<E> enumerationClass;

        private final PrimitiveStringToEnumerationConverterFactory primitiveStringToEnumerationConverterFactory;

        private final PrimitiveIntToEnumerationConverterFactory primitiveIntToEnumerationConverterFactory;

        private final ObjectMapper objectMapper;

        private final EnumerationAttribute enumerationAttribute;

        public ComplexStringToEnumerationConverter(
                @NonNull Class<E> enumerationClass,
                @NonNull PrimitiveStringToEnumerationConverterFactory primitiveStringToEnumerationConverterFactory,
                @NonNull PrimitiveIntToEnumerationConverterFactory primitiveIntToEnumerationConverterFactory,
                @NonNull ObjectMapper objectMapper,
                @NonNull EnumerationAttribute enumerationAttribute
                ) {
            this.enumerationClass = enumerationClass;
            this.primitiveStringToEnumerationConverterFactory = primitiveStringToEnumerationConverterFactory;
            this.primitiveIntToEnumerationConverterFactory = primitiveIntToEnumerationConverterFactory;
            this.objectMapper = objectMapper;
            this.enumerationAttribute = enumerationAttribute;
        }

        @Override
        @Nullable
        public E convert(@NonNull String source) {
            try {
                if (EnumerationJsonStrategy.NAME == this.enumerationAttribute.strategy()) {
                    return this.primitiveStringToEnumerationConverterFactory.getConverter(this.enumerationClass).convert(source);
                } else if (EnumerationJsonStrategy.ORDINAL == this.enumerationAttribute.strategy() && StringUtils.isNumeric(source)) {
                    return this.primitiveIntToEnumerationConverterFactory.getConverter(this.enumerationClass).convert(Integer.valueOf(source));
                } else {
                    throw EnumerationException.newEx("Unable to convert {} to Enumeration", source);
                }
            } catch (Exception exception) {
                try {
                    Map<String, Object> enumerationAttrMap = this.objectMapper.readValue(source, new TypeReference<Map<String, Object>>() {
                    });
                    if (EnumerationJsonStrategy.NAME == this.enumerationAttribute.strategy() && enumerationAttrMap.containsKey(Enumeration.NAME_FIELD)) {
                        return this.primitiveStringToEnumerationConverterFactory.getConverter(this.enumerationClass).convert((String) enumerationAttrMap.get(Enumeration.NAME_FIELD));
                    } else if (EnumerationJsonStrategy.ORDINAL == this.enumerationAttribute.strategy() && enumerationAttrMap.containsKey(Enumeration.ORDINAL_FIELD)) {
                        return this.primitiveIntToEnumerationConverterFactory.getConverter(this.enumerationClass).convert(Integer.valueOf(enumerationAttrMap.get(Enumeration.ORDINAL_FIELD).toString()));
                    } else {
                        throw EnumerationException.newEx("Unable to convert {} to Enumeration", source);
                    }
                } catch (Exception e) {
                    throw EnumerationException.newEx("Failed to convert enumeration, parameter: [{}], enumeration-json-strategy: [{}], {}", source, this.enumerationAttribute.strategy().name(), e.toString());
                }
            }
        }

    }

}

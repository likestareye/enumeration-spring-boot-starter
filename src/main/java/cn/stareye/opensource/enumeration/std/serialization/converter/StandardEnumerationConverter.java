package cn.stareye.opensource.enumeration.std.serialization.converter;

import cn.stareye.opensource.enumeration.std.Enumeration;
import cn.stareye.opensource.enumeration.std.EnumerationAttribute;
import cn.stareye.opensource.enumeration.std.EnumerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.lang.NonNull;

import java.util.Map;
import java.util.Set;

/**
 * 标准的反序列化枚举的工厂.
 *
 * @author: wjf
 * @date: 2022/7/28
 */
@SuppressWarnings("rawtypes")
public class StandardEnumerationConverter implements GenericConverter {

    private final ObjectMapper objectMapper;
    private final ComplexStringToEnumerationConverterFactory complexStringToEnumerationConverterFactory;
    private final PrimitiveIntToEnumerationConverterFactory primitiveIntToEnumerationConverterFactory;

    public StandardEnumerationConverter(
            ObjectMapper objectMapper,
            ComplexStringToEnumerationConverterFactory complexStringToEnumerationConverterFactory,
            PrimitiveIntToEnumerationConverterFactory primitiveIntToEnumerationConverterFactory
    ) {
        this.objectMapper = objectMapper;
        this.complexStringToEnumerationConverterFactory = complexStringToEnumerationConverterFactory;
        this.primitiveIntToEnumerationConverterFactory = primitiveIntToEnumerationConverterFactory;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Set.of(
                new ConvertiblePair(Integer.class, Enumeration.class),
                new ConvertiblePair(String.class, Enumeration.class),
                new ConvertiblePair(Map.class, Enumeration.class)
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object convert(Object source, @NonNull TypeDescriptor sourceType, @NonNull TypeDescriptor targetType) {
        try {
            if (source == null) {
                return null;
            } else if (Integer.class.isAssignableFrom(sourceType.getType())) {
                return this.primitiveIntToEnumerationConverterFactory.getConverter((Class<? extends Enumeration>) targetType.getType()).convert((Integer) source);
            } else if (String.class.isAssignableFrom(sourceType.getType())) {
                return this.complexStringToEnumerationConverterFactory.getConverter((Class<? extends Enumeration>) targetType.getType()).convert((String) source);
            } else if (Map.class.isAssignableFrom(sourceType.getType())) {
                return this.complexStringToEnumerationConverterFactory.getConverter((Class<? extends Enumeration>) targetType.getType()).convert(this.objectMapper.writeValueAsString(source));
            } else {
                throw EnumerationException.newEx("Unable to convert {} to Enumeration", this.objectMapper.writeValueAsString(source));
            }
        } catch (EnumerationException | JsonProcessingException exception) {
            throw EnumerationException.newEx(exception.toString());
        }
    }
}

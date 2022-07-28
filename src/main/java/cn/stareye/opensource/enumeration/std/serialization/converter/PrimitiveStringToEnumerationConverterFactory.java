package cn.stareye.opensource.enumeration.std.serialization.converter;

import cn.stareye.opensource.enumeration.std.Enumeration;
import cn.stareye.opensource.enumeration.std.EnumerationAttribute;
import cn.stareye.opensource.enumeration.std.EnumerationException;
import cn.stareye.opensource.enumeration.std.serialization.EnumerationJsonStrategy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 字符串基元反序列化为枚举的工厂.
 *
 * @author: wjf
 * @date: 2022/7/28
 */
@SuppressWarnings("rawtypes")
public class PrimitiveStringToEnumerationConverterFactory implements ConverterFactory<String, Enumeration> {

    @NonNull
    @Override
    @SuppressWarnings("all")
    public <T extends Enumeration> Converter<String, T> getConverter(@NonNull Class<T> targetType) {
        return new PrimitiveStringToEnumerationConverter(targetType);
    }

    public static class PrimitiveStringToEnumerationConverter<E extends Enum<E> & Enumeration<E>> implements Converter<String, E> {

        private final Class<E> enumerationClass;

        public PrimitiveStringToEnumerationConverter(@NonNull Class<E> enumerationClass) {
            this.enumerationClass = enumerationClass;
        }

        @Override
        @Nullable
        public E convert(@NonNull String source) {
            return source.isEmpty() ? null : Enumeration.valueForName(this.enumerationClass, source.trim());
        }

    }

}

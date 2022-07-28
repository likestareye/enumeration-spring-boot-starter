package cn.stareye.opensource.enumeration.std.serialization.converter;

import cn.stareye.opensource.enumeration.std.Enumeration;
import cn.stareye.opensource.enumeration.std.EnumerationAttribute;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 整数基元反序列化为枚举.
 *
 * @author: wjf
 * @date: 2022/7/28
 */
@SuppressWarnings("rawtypes")
public class PrimitiveIntToEnumerationConverterFactory implements ConverterFactory<Integer, Enumeration> {

    @NonNull
    @Override
    @SuppressWarnings("all")
    public <T extends Enumeration> Converter<Integer, T> getConverter(@NonNull Class<T> targetType) {
        return new IntToEnumerationConverter(targetType);
    }

    /**
     * 字符串反序列化为枚举.
     *
     * @author: wjf
     * @date: 2022/7/28
     */
    public static class IntToEnumerationConverter<E extends Enum<E> & Enumeration<E>> implements Converter<Integer, E> {

        private final Class<E> enumerationClass;

        public IntToEnumerationConverter(@NonNull Class<E> enumerationClass) {
            this.enumerationClass = enumerationClass;
        }

        @Override
        @Nullable
        public E convert(@NonNull Integer source) {
            return Enumeration.valueForOrdinal(this.enumerationClass, source);
        }

    }

}

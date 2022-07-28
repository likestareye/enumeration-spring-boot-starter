package cn.stareye.opensource.enumeration.std;

import cn.stareye.opensource.enumeration.std.serialization.StandardEnumerationDeserializer;
import cn.stareye.opensource.enumeration.std.serialization.StandardEnumerationSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.lang.NonNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * {@link Enum}枚举类需要实现的接口，本项目所有的枚举相关特化操作均基于此接口实现.
 *
 * @author: wjf
 * @date: 2022/7/25
 */
@JsonSerialize(using = StandardEnumerationSerializer.class)
@JsonDeserialize(using = StandardEnumerationDeserializer.class)
public interface Enumeration<E extends Enum<E> & Enumeration<E>> {

    /**
     * {@link Enum#name()}.
     */
    String NAME_FIELD = "name";

    /**
     * {@link Enum#ordinal()}.
     */
    String ORDINAL_FIELD = "ordinal";

    /**
     * 获取枚举的name.
     *
     * @return name.
     */
    @NonNull
    default String name() {
        return self().name();
    }

    /**
     * 获取枚举定义的顺序号.
     *
     * @return ordinal.
     */
    @NonNull
    default int ordinal() {
        return self().ordinal();
    }

    /**
     * 获取枚举的声明class.
     *
     * @return Class<? extends Enum < ?>>.
     */
    @NonNull
    default Class<E> declaringClass() {
        return self().getDeclaringClass();
    }

    /**
     * 返回自己，自己必须是一个枚举类.
     *
     * @return E.
     */
    @NonNull
    @SuppressWarnings("unchecked")
    default E self() {
        if (!(this instanceof Enum<?>)) {
            throw EnumerationException.newEx("The current implementing class is not an Enum, class:[{}]", this.getClass().getTypeName());
        }
        return (E) this;
    }

    /**
     * 根据名称获取枚举.
     * @param enumerationClass Class<E>.
     * @param name name.
     * @return E.
     * @param <E> E extends Enum<E> & Enumeration<E>.
     */
    static <E extends Enum<E> & Enumeration<E>> E valueForName(Class<E> enumerationClass, String name) {
        return Enum.valueOf(enumerationClass, name);
    }

    static <E extends Enum<E> & Enumeration<E>> E valueForOrdinal(Class<E> enumerationClass, int ordinal) {
        return enumerationClass.getEnumConstants()[ordinal];
    }

}

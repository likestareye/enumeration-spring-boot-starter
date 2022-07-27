package cn.stareye.opensource.enumeration.std.serialization;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 枚举json序列化注解, 作用在字段上, 序列化和反序列化将按照此方式进行,
 * 只有添加了此注解的字段才会被序列化(name和ordinal是默认会序列化/反序列化的),
 * {@link this#value()} 是序列化和反序列化所对应的别名, 没有填写则默认使用字段名称.
 * {@link this#alias()} 是序列化和反序列化所对应的别名, 没有填写则默认使用字段名称.
 * 当{@link this#value()}和{@link this#alias()}出现冲突时, 则默认使用{@link this#alias()},
 * 即{@link this#alias()}的优先级更高.
 *
 * @author: wjf
 * @date: 2022/7/26
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnumerationSerialization {

    @AliasFor("alias")
    String value() default "";

    @AliasFor("value")
    String alias() default "";

}

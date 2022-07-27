package cn.stareye.opensource.enumeration.std.serialization;

import cn.stareye.opensource.enumeration.std.EnumerationAttribute;
import org.springframework.lang.NonNull;

/**
 * 序列化/反序列化器提供者.
 *
 * @author: wjf
 * @date: 2022/7/27
 */
public sealed interface SerializationSupplier permits EnumerationJsonStrategy {

    /**
     * 提供序列化器.
     *
     * @return BaseEnumerationJsonSerializer.
     */
    @NonNull
    BaseEnumerationJsonSerializer serializer(@NonNull EnumerationAttribute enumerationAttribute);

    /**
     * 提供反序列化器.
     *
     * @return BaseEnumerationJsonDeserializer.
     */
    @NonNull
    @SuppressWarnings("rawtypes")
    BaseEnumerationJsonDeserializer deserializer(@NonNull EnumerationAttribute enumerationAttribute);

}

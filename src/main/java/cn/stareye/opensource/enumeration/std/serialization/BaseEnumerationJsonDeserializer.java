package cn.stareye.opensource.enumeration.std.serialization;

import cn.stareye.opensource.enumeration.std.Enumeration;
import cn.stareye.opensource.enumeration.std.EnumerationAttribute;
import cn.stareye.opensource.enumeration.std.EnumerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 基本的枚举反序列化器.
 *
 * @author: wjf
 * @date: 2022/7/27
 */
@SuppressWarnings("rawtypes")
public abstract class BaseEnumerationJsonDeserializer<K> extends JsonDeserializer<Enumeration> implements ContextualDeserializer {

    protected Map<K, Enumeration<?>> enumerationMap;

    protected final EnumerationAttribute enumerationAttribute;

    protected BaseEnumerationJsonDeserializer(@NonNull EnumerationAttribute enumerationAttribute) {
        this.enumerationAttribute = Objects.requireNonNull(enumerationAttribute);
    }

    /**
     * 特定策略的枚举反序列化特化操作, 此操作主要用于获取key的实例.
     *
     * @param jsonParser JsonParser.
     * @param context    DeserializationContext.
     * @return K.
     */
    @Nullable
    protected abstract K getKey(JsonParser jsonParser, DeserializationContext context) throws IOException;

    /**
     * 特定策略的枚举反序列化特化操作, 此操作主要用于设置key的实例.
     *
     * @param enumeration Enumeration.
     * @return K.
     */
    @NonNull
    protected abstract K setKey(Enumeration<?> enumeration);

    /**
     * 特定策略的枚举反序列化特化操作, 此操作主要用于获取当前特化的反序列器实例.
     *
     * @return BaseEnumerationJsonDeserializer.
     */
    @NonNull
    protected abstract BaseEnumerationJsonDeserializer<K> createDeserializer();

    @Override
    public Enumeration deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        return this.enumerationMap.get(this.getKey(jsonParser, context));
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext context, BeanProperty property) throws JsonMappingException {
        JavaType javaType = property.getType();
        Class<?> enumerationClass = javaType.getRawClass();

        if (!Enum.class.isAssignableFrom(enumerationClass) || !Enumeration.class.isAssignableFrom(enumerationClass)) {
            throw EnumerationException.newEx("Class {} must be an enumeration class and implement interface {}", enumerationClass.getTypeName(), Enumeration.class.getTypeName());
        }
        @SuppressWarnings("unchecked")
        Enum<?>[] enumConstants = ((Class<? extends Enum>) enumerationClass).getEnumConstants();

        if (enumConstants == null) {
            throw EnumerationException.newEx("enumeration does not exist, classname: [{}]", enumerationClass.getTypeName());
        }

        HashMap<K, Enumeration<?>> enumConstantMap = new HashMap<>(enumConstants.length);

        for (Enum<?> enumConstant : enumConstants) {
            enumConstantMap.put(this.setKey((Enumeration<?>) enumConstant), (Enumeration<?>) enumConstant);
        }

        BaseEnumerationJsonDeserializer<K> deserializer = this.createDeserializer();
        deserializer.setEnumerationMap(enumConstantMap);
        return deserializer;
    }

    protected Map<K, Enumeration<?>> getEnumerationMap() {
        return this.enumerationMap;
    }

    protected void setEnumerationMap(Map<K, Enumeration<?>> enumerationMap) {
        this.enumerationMap = enumerationMap;
    }
}



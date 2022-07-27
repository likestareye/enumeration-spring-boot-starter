package cn.stareye.opensource.enumeration.std.serialization;

import cn.stareye.opensource.enumeration.std.Enumeration;
import cn.stareye.opensource.enumeration.std.EnumerationAttribute;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.IOException;

/**
 * 标准的枚举反序列化器, 此处使用装饰器模式, 增强功能.
 *
 * @author: wjf
 * @date: 2022/7/27
 */
@JsonComponent
@SuppressWarnings("rawtypes")
public class StandardEnumerationDeserializer extends BaseEnumerationJsonDeserializer<Object> {

    @NonNull
    private final BaseEnumerationJsonDeserializer enumerationJsonDeserializer;

    @Autowired
    public StandardEnumerationDeserializer(
            @NonNull
            @Qualifier("stareye.enumeration.serialization-cn.stareye.opensource.enumeration.spring.boot.autoconfigure.EnumerationSerializationProperties")
            EnumerationAttribute enumerationAttribute
    ) {
        super(enumerationAttribute);
        this.enumerationJsonDeserializer = this.enumerationAttribute.strategy().deserializer(this.enumerationAttribute);
    }

    @Override
    @Nullable
    public Object getKey(JsonParser jsonParser, DeserializationContext context) throws IOException {
        return this.enumerationJsonDeserializer.getKey(jsonParser, context);
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public Object setKey(Enumeration<?> enumeration) {
        return this.enumerationJsonDeserializer.setKey(enumeration);
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public BaseEnumerationJsonDeserializer<Object> createDeserializer() {
        return this.enumerationJsonDeserializer.createDeserializer();
    }

}

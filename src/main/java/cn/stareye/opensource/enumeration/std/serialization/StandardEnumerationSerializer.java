package cn.stareye.opensource.enumeration.std.serialization;

import cn.stareye.opensource.enumeration.std.Enumeration;
import cn.stareye.opensource.enumeration.std.EnumerationAttribute;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.lang.NonNull;

import java.io.IOException;

/**
 * 标准的枚举序列化器, 此处使用装饰器模式, 增强功能.
 *
 * @author: wjf
 * @date: 2022/7/27
 */
@JsonComponent
public class StandardEnumerationSerializer extends BaseEnumerationJsonSerializer {

    @NonNull
    private final BaseEnumerationJsonSerializer enumerationJsonSerializer;

    @Autowired
    public StandardEnumerationSerializer(
            @NonNull
            @Qualifier("stareye.enumeration.serialization-cn.stareye.opensource.enumeration.spring.boot.autoconfigure.EnumerationSerializationProperties")
            EnumerationAttribute enumerationAttribute
    ) {
        super(enumerationAttribute, Enumeration.class);
        this.enumerationJsonSerializer = this.enumerationAttribute.strategy().serializer(this.enumerationAttribute);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void serializeEnumeration(Enumeration value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        this.enumerationJsonSerializer.serializeEnumeration(value, gen, provider);
    }

}

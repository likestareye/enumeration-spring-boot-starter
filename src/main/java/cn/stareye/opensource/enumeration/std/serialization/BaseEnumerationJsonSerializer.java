package cn.stareye.opensource.enumeration.std.serialization;

import cn.stareye.opensource.enumeration.std.Enumeration;
import cn.stareye.opensource.enumeration.std.EnumerationAttribute;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.Objects;

/**
 * 基本的枚举序列化器.
 *
 * @author: wjf
 * @date: 2022/7/27
 */
@SuppressWarnings("rawtypes")
public abstract class BaseEnumerationJsonSerializer extends StdSerializer<Enumeration> {

    protected final EnumerationAttribute enumerationAttribute;

    protected BaseEnumerationJsonSerializer(@NonNull EnumerationAttribute enumerationAttribute, @NonNull Class<Enumeration> enumerationClass) {
        super(Objects.requireNonNull(enumerationClass));
        this.enumerationAttribute = Objects.requireNonNull(enumerationAttribute);
    }

    /**
     * 特定策略的枚举序列化特化操作.
     *
     * @param value    Enumeration.
     * @param gen      JsonGenerator.
     * @param provider SerializerProvider.
     */
    protected abstract void serializeEnumeration(Enumeration value, JsonGenerator gen, SerializerProvider provider) throws IOException;

    @Override
    @SuppressWarnings("unchecked")
    public void serialize(Enumeration value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        this.serializeEnumeration(value, gen, provider);
        Class<? extends Enumeration<?>> valueClass = (Class<? extends Enumeration<?>>) value.getClass();
        EnumerationSignature signature = EnumerationSerializationAnnotatedResolver.fromEnumeration(valueClass);
        for (EnumerationSignature.EnumerationPropertySignature propertySignature : signature.getEnumerationPropertySignatures()) {
            gen.writeFieldName(propertySignature.getFieldAlias());
            gen.writePOJO(propertySignature.readValue(value));
        }
        gen.writeEndObject();
    }
}

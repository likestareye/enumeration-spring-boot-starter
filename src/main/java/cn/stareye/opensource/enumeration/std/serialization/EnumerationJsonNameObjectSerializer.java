package cn.stareye.opensource.enumeration.std.serialization;

import cn.stareye.opensource.enumeration.std.Enumeration;
import cn.stareye.opensource.enumeration.std.EnumerationAttribute;
import cn.stareye.opensource.enumeration.std.EnumerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.lang.NonNull;

import java.io.IOException;

/**
 * 依据{@link Enumeration#name()}的枚举序列化器.
 *
 * @author: wjf
 * @date: 2022/7/26
 */
public class EnumerationJsonNameObjectSerializer extends BaseEnumerationJsonSerializer {

    public EnumerationJsonNameObjectSerializer(@NonNull EnumerationAttribute enumerationAttribute) {
        super(enumerationAttribute, Enumeration.class);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void serializeEnumeration(Enumeration value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (EnumerationJsonStrategy.NAME != this.enumerationAttribute.strategy()) {
            throw EnumerationException.newEx("EnumerationJsonNameObjectSerializer init error, strategy:[{}]", this.enumerationAttribute.strategy().name());
        }
        gen.writeFieldName(Enumeration.NAME_FIELD);
        gen.writeString(value.name());
        gen.writeFieldName(Enumeration.ORDINAL_FIELD);
        gen.writeNumber(value.ordinal());
    }

}

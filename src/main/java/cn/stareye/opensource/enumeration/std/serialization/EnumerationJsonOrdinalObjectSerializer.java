package cn.stareye.opensource.enumeration.std.serialization;

import cn.stareye.opensource.enumeration.std.Enumeration;
import cn.stareye.opensource.enumeration.std.EnumerationAttribute;
import cn.stareye.opensource.enumeration.std.EnumerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.lang.NonNull;

import java.io.IOException;

/**
 * 依据{@link Enumeration#ordinal()}的枚举序列化器.
 *
 * @author: wjf
 * @date: 2022/7/27
 */
public class EnumerationJsonOrdinalObjectSerializer extends BaseEnumerationJsonSerializer {

    public EnumerationJsonOrdinalObjectSerializer(@NonNull EnumerationAttribute enumerationAttribute) {
        super(enumerationAttribute, Enumeration.class);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void serializeEnumeration(Enumeration value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (EnumerationJsonStrategy.ORDINAL != this.enumerationAttribute.strategy()) {
            throw EnumerationException.newEx("EnumerationJsonNameObjectSerializer init error, strategy:[{}]", this.enumerationAttribute.strategy().name());
        }
        gen.writeFieldName(Enumeration.ORDINAL_FIELD);
        gen.writeNumber(value.ordinal());
        gen.writeFieldName(Enumeration.NAME_FIELD);
        gen.writeString(value.name());
    }
}

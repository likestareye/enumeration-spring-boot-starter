package cn.stareye.opensource.enumeration.std.serialization;

import cn.stareye.opensource.enumeration.std.Enumeration;
import cn.stareye.opensource.enumeration.std.EnumerationAttribute;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.Map;

/**
 * 依据{@link Enumeration#ordinal()}的反枚举序列化器.
 *
 * @author: wjf
 * @date: 2022/7/27
 */
public class EnumerationJsonOrdinalObjectDeserializer extends BaseEnumerationJsonDeserializer<Integer> {

    public EnumerationJsonOrdinalObjectDeserializer(EnumerationAttribute enumerationAttribute) {
        super(enumerationAttribute);
    }

    @Override
    @Nullable
    public Integer getKey(JsonParser jsonParser, DeserializationContext context) throws IOException {
        JsonToken jsonToken = jsonParser.currentToken();
        Integer ordinal = null;
        if (jsonToken == JsonToken.START_OBJECT) {
            /*
             *  此处主要解决结构的json对象:
             *  {
             *     "sex": {
             *       "ordinal": 0,
             *       "name": "MALE",
             *       "disName": "男"
             *      }
             *   }
             */
            while (!jsonParser.isClosed() && jsonToken != JsonToken.END_OBJECT) {
                jsonToken = jsonParser.nextToken();
                if (jsonToken == JsonToken.FIELD_NAME
                        && Enumeration.ORDINAL_FIELD.equals(jsonParser.getCurrentName())) {
                    jsonParser.nextToken();
                    ordinal = resolveOrdinal(jsonParser);
                }
            }
        } else {
            /*
             *  此处主要解决普通的json键值对:
             *  {
             *      "sex": 0
             *  }
             */
            ordinal = resolveOrdinal(jsonParser);
        }
        return ordinal;
    }

    @NonNull
    @Override
    public Integer setKey(Enumeration<?> enumeration) {
        return enumeration.ordinal();
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public BaseEnumerationJsonDeserializer<Integer> createDeserializer() {
        return EnumerationJsonStrategy.ORDINAL.deserializer(this.enumerationAttribute);
    }

    private Integer resolveOrdinal(JsonParser jsonParser) throws IOException {
        String ordinal = jsonParser.getValueAsString();
        ObjectMapper codec = (ObjectMapper) jsonParser.getCodec();
        try {
            /*
             *  此处主要解决普通的json键值对的值是一个结构化的json对象字符串:
             *  {
             *      "sex": "{\"ordinal\": 0, \"name\":\"MALE\", \"disName\": \"男\"}"
             *  }
             */
            Map<String, Object> enumerationAttrMap = codec.readValue(ordinal, new TypeReference<Map<String, Object>>() {
            });
            if (enumerationAttrMap.containsKey(Enumeration.ORDINAL_FIELD)) {
                return Integer.valueOf(enumerationAttrMap.get(Enumeration.ORDINAL_FIELD).toString());
            } else {
                return Integer.valueOf(ordinal);
            }
        } catch (JsonProcessingException exception) {
            return Integer.valueOf(ordinal);
        }
    }

}

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
 * 依据{@link Enumeration#name()}的反枚举序列化器.
 *
 * @author: wjf
 * @date: 2022/7/27
 */
public class EnumerationJsonNameObjectDeserializer extends BaseEnumerationJsonDeserializer<String> {

    public EnumerationJsonNameObjectDeserializer(@NonNull EnumerationAttribute enumerationAttribute) {
        super(enumerationAttribute);
    }

    @Override
    @Nullable
    public String getKey(JsonParser jsonParser, DeserializationContext context) throws IOException {
        JsonToken jsonToken = jsonParser.currentToken();
        String name = null;
        if (jsonToken == JsonToken.START_OBJECT) {
            /*
             *  此处主要解决结构的json对象:
             *  {
             *     "sex": {
             *       "name": "MALE",
             *       "ordinal": 0,
             *       "disName": "男"
             *      }
             *   }
             */
            while (!jsonParser.isClosed() && jsonToken != JsonToken.END_OBJECT) {
                jsonToken = jsonParser.nextToken();
                if (jsonToken == JsonToken.FIELD_NAME
                        && Enumeration.NAME_FIELD.equals(jsonParser.getCurrentName())) {
                    jsonParser.nextToken();
                    name = this.resolveName(jsonParser);
                }
            }
        } else {
            /*
             *  此处主要解决普通的json键值对:
             *  {
             *      "sex": "MALE"
             *  }
             */
            name = this.resolveName(jsonParser);
        }
        return name;
    }

    @NonNull
    @Override
    public String setKey(Enumeration<?> enumeration) {
        return enumeration.name();
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public BaseEnumerationJsonDeserializer<String> createDeserializer() {
        return EnumerationJsonStrategy.NAME.deserializer(this.enumerationAttribute);
    }

    private String resolveName(JsonParser jsonParser) throws IOException {
        String name = jsonParser.getValueAsString();
        ObjectMapper codec = (ObjectMapper) jsonParser.getCodec();
        try {
            /*
             *  此处主要解决普通的json键值对的值是一个结构化的json对象字符串:
             *  {
             *      "sex": "{\"name\":\"MALE\", \"ordinal\": 0, \"disName\": \"男\"}"
             *  }
             */
            Map<String, Object> childMap = codec.readValue(name, new TypeReference<Map<String, Object>>() {
            });
            if (childMap.containsKey(Enumeration.NAME_FIELD)) {
                return childMap.get(Enumeration.NAME_FIELD).toString();
            } else {
                return name;
            }
        } catch (JsonProcessingException exception) {
            return name;
        }
    }

}

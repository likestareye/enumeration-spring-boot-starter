package cn.stareye.opensource.enumeration.std.serialization;

import cn.stareye.opensource.enumeration.std.Enumeration;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link EnumerationSerialization}的注解解析器.
 *
 * @author: wjf
 * @date: 2022/7/26
 */
public class EnumerationSerializationAnnotatedResolver {

    private EnumerationSerializationAnnotatedResolver() {
    }

    public static EnumerationSignature fromEnumeration(Class<? extends Enumeration<?>> enumClass) {
        if (EnumerationSerializationRepository.containsEnumeration(enumClass)) {
            return EnumerationSerializationRepository.get(enumClass);
        }
        EnumerationSignature enumerationSignature = EnumerationSignature.fromEnumeration(enumClass);
        EnumerationSerializationRepository.put(enumClass, enumerationSignature);
        return enumerationSignature;
    }

    private static class EnumerationSerializationRepository {

        private static final Map<Class<? extends Enumeration<?>>, EnumerationSignature> REPOSITORY = new HashMap<>();

        private static EnumerationSignature get(Class<? extends Enumeration<?>> enumClass) {
            return REPOSITORY.get(enumClass);
        }

        private static void put(Class<? extends Enumeration<?>> enumClass, EnumerationSignature enumerationSignature) {
            REPOSITORY.put(enumClass, enumerationSignature);
        }

        private static boolean containsEnumeration(Class<? extends Enumeration<?>> enumClass) {
            return REPOSITORY.containsKey(enumClass);
        }

    }

}

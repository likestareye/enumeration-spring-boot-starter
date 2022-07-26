package cn.stareye.opensource.enumeration.spring.boot.autoconfigure;

import cn.stareye.opensource.enumeration.std.EnumerationAttribute;
import cn.stareye.opensource.enumeration.std.container.DefaultEnumerationContainer;
import cn.stareye.opensource.enumeration.std.container.DefaultEnumerationContainerEngine;
import cn.stareye.opensource.enumeration.std.container.EnumerationContainer;
import cn.stareye.opensource.enumeration.std.container.EnumerationContainerEngine;
import cn.stareye.opensource.enumeration.std.serialization.StandardEnumerationDeserializer;
import cn.stareye.opensource.enumeration.std.serialization.StandardEnumerationSerializer;
import cn.stareye.opensource.enumeration.std.serialization.converter.ComplexStringToEnumerationConverterFactory;
import cn.stareye.opensource.enumeration.std.serialization.converter.PrimitiveIntToEnumerationConverterFactory;
import cn.stareye.opensource.enumeration.std.serialization.converter.PrimitiveStringToEnumerationConverterFactory;
import cn.stareye.opensource.enumeration.std.serialization.converter.StandardEnumerationConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.lang.NonNull;

/**
 * Enumeration自动配置类.
 *
 * @author: wjf
 * @date: 2022/7/26
 */
@AutoConfiguration
public class EnumerationAutoConfiguration {

    @Configuration
    @EnableConfigurationProperties(EnumerationSerializationProperties.class)
    @ConditionalOnProperty(prefix = EnumerationSerializationProperties.PREFIX, name = "enabled", havingValue = "true")
    @Import({StandardEnumerationSerializer.class, StandardEnumerationDeserializer.class})
    public static class SerializationAutoConfiguration {

        private final ConverterRegistry converterRegistry;

        private final ObjectMapper objectMapper;

        private final EnumerationAttribute enumerationAttribute;

        @Autowired
        public SerializationAutoConfiguration(
                @NonNull ConverterRegistry converterRegistry,
                @NonNull ObjectMapper objectMapper,
                @NonNull @Qualifier("stareye.enumeration.serialization-cn.stareye.opensource.enumeration.spring.boot.autoconfigure.EnumerationSerializationProperties") EnumerationAttribute enumerationAttribute
        ) {
            this.converterRegistry = converterRegistry;
            this.objectMapper = objectMapper;
            this.enumerationAttribute = enumerationAttribute;
        }

        private static final Logger logger = LoggerFactory.getLogger(SerializationAutoConfiguration.class);

        @PostConstruct
        private void init() {
            logger.info("SerializationAutoConfiguration init...");
            PrimitiveStringToEnumerationConverterFactory primitiveStringToEnumerationConverterFactory = new PrimitiveStringToEnumerationConverterFactory();
            PrimitiveIntToEnumerationConverterFactory primitiveIntToEnumerationConverterFactory = new PrimitiveIntToEnumerationConverterFactory();
            ComplexStringToEnumerationConverterFactory complexStringToEnumerationConverterFactory = new ComplexStringToEnumerationConverterFactory(
                    this.objectMapper,
                    this.enumerationAttribute,
                    primitiveStringToEnumerationConverterFactory,
                    primitiveIntToEnumerationConverterFactory
            );
            StandardEnumerationConverter converter = new StandardEnumerationConverter(
                    this.objectMapper,
                    complexStringToEnumerationConverterFactory,
                    primitiveIntToEnumerationConverterFactory
            );
            converterRegistry.addConverter(converter);
        }

        @PreDestroy
        private void destroy() {
            logger.info("SerializationAutoConfiguration destroy...");
        }

    }


    @Configuration
    @EnableConfigurationProperties(EnumerationContainerProperties.class)
    @ConditionalOnProperty(prefix = EnumerationContainerProperties.PREFIX, name = "enabled", havingValue = "true")
    public static class ContainerAutoConfiguration {

        private static final Logger logger = LoggerFactory.getLogger(ContainerAutoConfiguration.class);

        @PostConstruct
        private void init() {
            logger.info("ContainerAutoConfiguration init...");
        }

        @PreDestroy
        private void destroy() {
            logger.info("ContainerAutoConfiguration destroy...");
        }

        @Bean
        @ConditionalOnMissingBean(DefaultEnumerationContainer.class)
        public EnumerationContainer enumerationContainer(
                @Qualifier("stareye.enumeration.container-cn.stareye.opensource.enumeration.spring.boot.autoconfigure.EnumerationContainerProperties")
                @NonNull
                EnumerationAttribute enumerationAttribute
        ) {
            EnumerationContainerEngine engine = new DefaultEnumerationContainerEngine(enumerationAttribute);
            return new DefaultEnumerationContainer(engine);
        }

    }

}

package cn.stareye.opensource.enumeration.std.container;

import cn.stareye.opensource.enumeration.std.Enumeration;
import cn.stareye.opensource.enumeration.std.EnumerationAttribute;
import cn.stareye.opensource.enumeration.std.EnumerationException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.ReflectionsException;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.util.*;

/**
 * 默认的枚举容器引擎.
 *
 * @author: wjf
 * @date: 2022/7/27
 */
public class DefaultEnumerationContainerEngine implements EnumerationContainerEngine {

    private static final Logger logger = LoggerFactory.getLogger(DefaultEnumerationContainerEngine.class);

    private final EnumerationAttribute enumerationAttribute;

    private EnumerationContainer enumerationContainer;

    public DefaultEnumerationContainerEngine(@NonNull EnumerationAttribute enumerationAttribute) {
        this.enumerationAttribute = Objects.requireNonNull(enumerationAttribute);
    }

    @Override
    public void init() {
        this.scans(this.enumerationAttribute.scans()).forEach(reflections -> this.enumerationContainer.put(this.load(reflections)));
    }

    @Override
    public void bindContainer(@NonNull EnumerationContainer container) {
        if (this.enumerationContainer != null) {
            throw EnumerationException.newEx("The current engine has bound the container");
        }
        this.enumerationContainer = Objects.requireNonNull(container);
    }

    @Override
    public void dynamicLoad(String... packageNames) {
        if (ArrayUtils.isEmpty(packageNames)) {
            return;
        }
        this.scans(Arrays.asList(packageNames)).forEach(reflections -> this.enumerationContainer.put(this.load(reflections)));
    }

    private List<Reflections> scans(List<String> packageNames) {
        if (CollectionUtils.isEmpty(packageNames)) {
            return Collections.emptyList();
        }

        return packageNames.stream()
                .map(String::trim)
                .filter(StringUtils::isNotBlank)
                .map(pack -> {
                    logger.info("scanning-package: {}", pack);
                    logger.info("scanning-package-patten: {}", String.format("%s.*", pack));
                    return new Reflections(
                            new ConfigurationBuilder()
                                    .setUrls(ClasspathHelper.forPackage(pack))
                                    .filterInputsBy(new FilterBuilder().includePattern(String.format("%s.*", pack)))
                                    .setScanners(Scanners.SubTypes)

                    );
                }).toList();
    }

    @SuppressWarnings("rawtypes")
    private Map<String, List<Enumeration>> load(Reflections reflections) {
        Map<String, List<Enumeration>> enumerationMap = new HashMap<>();
        try {
            Set<Class<? extends Enumeration>> enumerationClasses = reflections.getSubTypesOf(Enumeration.class);

            if (CollectionUtils.isEmpty(enumerationClasses)) {
                return Collections.emptyMap();
            }

            enumerationClasses
                    .forEach(enumerationClass -> {
                        Enumeration[] enumConstants = enumerationClass.getEnumConstants();
                        enumerationMap.put(enumerationClass.getTypeName(), Arrays.asList(enumConstants));
                    });
        } catch (ReflectionsException e) {
            logger.warn("load Enumeration error: {}", e.toString());
            return Collections.emptyMap();
        }
        return enumerationMap;
    }

}

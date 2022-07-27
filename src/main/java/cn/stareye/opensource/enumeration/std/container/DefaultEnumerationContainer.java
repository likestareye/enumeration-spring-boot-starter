package cn.stareye.opensource.enumeration.std.container;

import cn.stareye.opensource.enumeration.std.Enumeration;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 默认的枚举容器.
 *
 * @author: wjf
 * @date: 2022/7/27
 */
public class DefaultEnumerationContainer implements EnumerationContainer {

    private static final Logger logger = LoggerFactory.getLogger(DefaultEnumerationContainer.class);

    private final EnumerationContainerEngine engine;

    @SuppressWarnings("rawtypes")
    private final Map<String, List<Enumeration>> enumerationMap = new HashMap<>();

    public DefaultEnumerationContainer(@NonNull EnumerationContainerEngine engine) {
        this.engine = Objects.requireNonNull(engine);
    }

    @PostConstruct
    private void init() {
        logger.info("DefaultEnumerationContainer init start...");
        this.engine.bindContainer(this);
        this.engine.init();
        logger.info("DefaultEnumerationContainer init finish...");
    }

    @Override
    public EnumerationContainerEngine getEngine() {
        return this.engine;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void put(Map<String, List<Enumeration>> enumerationMap) {
        this.enumerationMap.putAll(enumerationMap);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Map<String, List<Enumeration>> findAll() {
        return this.enumerationMap;
    }

}

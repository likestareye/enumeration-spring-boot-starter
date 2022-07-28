package cn.stareye.opensource.enumeration.spring.boot.autoconfigure;

import cn.stareye.opensource.enumeration.std.EnumerationAttribute;
import cn.stareye.opensource.enumeration.std.EnumerationException;
import cn.stareye.opensource.enumeration.std.serialization.EnumerationJsonStrategy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * Enumeration配置类.
 *
 * @author: wjf
 * @date: 2022/7/26
 */
@ConfigurationProperties(prefix = EnumerationSerializationProperties.PREFIX)
public class EnumerationSerializationProperties implements EnumerationAttribute {

    public static final String PREFIX = "stareye.enumeration.serialization";

    /**
     * 是否启用enumeration-serialization自动配置.
     */
    private boolean enabled = true;

    /**
     * enumeration的序列化/反序列化策略.
     */
    private EnumerationJsonStrategy strategy = EnumerationJsonStrategy.NAME;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public EnumerationJsonStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(EnumerationJsonStrategy strategy) {
        this.strategy = strategy;
    }


    @Override
    public boolean enabled() {
        return enabled;
    }

    @NonNull
    @Override
    public EnumerationJsonStrategy strategy() {
        return strategy;
    }

    @NonNull
    @Override
    public List<String> scans() {
        throw EnumerationException.newEx("unsupported operation");
    }
}

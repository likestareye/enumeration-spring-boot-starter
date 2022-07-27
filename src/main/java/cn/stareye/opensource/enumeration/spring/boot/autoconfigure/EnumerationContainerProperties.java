package cn.stareye.opensource.enumeration.spring.boot.autoconfigure;

import cn.stareye.opensource.enumeration.std.EnumerationAttribute;
import cn.stareye.opensource.enumeration.std.EnumerationException;
import cn.stareye.opensource.enumeration.std.serialization.EnumerationJsonStrategy;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;

/**
 * Enumeration配置类.
 *
 * @author: wjf
 * @date: 2022/7/26
 */
@ConfigurationProperties(prefix = EnumerationContainerProperties.PREFIX)
public class EnumerationContainerProperties implements EnumerationAttribute {

    public static final String PREFIX = "stareye.enumeration.container";

    /**
     * 是否启用enumeration-container自动配置.
     */
    private boolean enabled = true;

    /**
     * enumeration所处的包, 主要用于容器化配置.
     */
    private List<String> scans = Collections.emptyList();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getScans() {
        return scans;
    }

    public void setScans(List<String> scans) {
        this.scans = scans;
    }

    @Override
    public boolean enabled() {
        return enabled;
    }

    @Override
    public EnumerationJsonStrategy strategy() {
        throw EnumerationException.newEx("unsupported operation");
    }

    @Override
    public List<String> scans() {
        return scans;
    }
}

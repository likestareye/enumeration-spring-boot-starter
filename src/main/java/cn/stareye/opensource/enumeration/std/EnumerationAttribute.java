package cn.stareye.opensource.enumeration.std;

import cn.stareye.opensource.enumeration.std.serialization.EnumerationJsonStrategy;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * 枚举属性接口, 枚举所需要的属性配置接口.
 *
 * @author: wjf
 * @date: 2022/7/26
 */
public interface EnumerationAttribute {

    /**
     * 是否开启枚举序列化以及容器化.
     *
     * @return boolean.
     */
    @NonNull
    boolean enabled();

    /**
     * 枚举类序列化和容器化的策略.
     *
     * @return EnumerationJsonStrategy.
     */
    @NonNull
    EnumerationJsonStrategy strategy();

    /**
     * 需要扫描的包, 包中的所有实现{@link Enumeration}接口的枚举都将被序列化和容器化.
     *
     * @return List<String>.
     */
    @NonNull
    List<String> scans();

}

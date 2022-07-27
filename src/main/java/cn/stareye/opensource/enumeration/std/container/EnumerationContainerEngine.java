package cn.stareye.opensource.enumeration.std.container;

import org.springframework.lang.NonNull;

/**
 * 枚举容器引擎.
 *
 * @author: wjf
 * @date: 2022/7/27
 */
public interface EnumerationContainerEngine {

    /**
     * 初始化.
     */
    void init();

    /**
     * 绑定枚举容器, 一个引擎只能驱动一个容器, 多次绑定容器则抛出异常.
     *
     * @param container EnumerationContainer.
     */
    void bindContainer(@NonNull EnumerationContainer container);

    /**
     * 动态加载其他包下的枚举.
     *
     * @param packageNames 包名.
     */
    void dynamicLoad(String... packageNames);

}

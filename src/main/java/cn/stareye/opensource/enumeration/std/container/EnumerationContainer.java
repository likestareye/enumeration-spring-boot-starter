package cn.stareye.opensource.enumeration.std.container;

import cn.stareye.opensource.enumeration.std.Enumeration;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 枚举容器.
 *
 * @author: wjf
 * @date: 2022/7/26
 */
public interface EnumerationContainer {

    /**
     * 获取当前枚举容器的容器引擎.
     *
     * @return EnumerationContainerEngine.
     */
    EnumerationContainerEngine getEngine();

    /**
     * 此方法用于向枚举容器中存放枚举, 一般由容器引擎驱动.
     *
     * @param enumerationMap Map<String, List<Enumeration>>.
     */
    @SuppressWarnings("rawtypes")
    void put(Map<String, List<Enumeration>> enumerationMap);

    /**
     * 获取枚举容器中所有的枚举.
     *
     * @return Map<String, List < Enumeration>>.
     */
    @SuppressWarnings("rawtypes")
    Map<String, List<Enumeration>> findAll();

    @SuppressWarnings("rawtypes")
    default List<Enumeration> findOne(String className) {
        return findAll().getOrDefault(className, Collections.emptyList());
    }

    @SuppressWarnings("rawtypes")
    default List<Enumeration> findOne(Class<? extends Enumeration<?>> enumerationClass) {
        return findOne(enumerationClass.getTypeName());
    }

}

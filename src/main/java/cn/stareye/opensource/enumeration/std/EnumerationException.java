package cn.stareye.opensource.enumeration.std;

import org.slf4j.helpers.MessageFormatter;

/**
 * 特定于本项目的自定义异常.
 *
 * @author: wjf
 * @date: 2022/7/25
 */
public class EnumerationException extends RuntimeException {

    /**
     * 构造方法, 支持slf4j模板插值.
     *
     * @param messagePattern slf4j模板.
     * @param parameters     slf4j模板所需要插入的参数.
     */
    public EnumerationException(String messagePattern, Object... parameters) {
        super(MessageFormatter.arrayFormat(messagePattern, parameters).getMessage());
    }

    /**
     * 快捷创建异常的方式.
     *
     * @param messagePattern slf4j模板.
     * @param parameters     slf4j模板所需要插入的参数.
     * @return TatException.
     */
    public static EnumerationException newEx(String messagePattern, Object... parameters) {
        return new EnumerationException(messagePattern, parameters);
    }

    /**
     * 快捷抛出异常的方式.
     *
     * @param messagePattern slf4j模板.
     * @param parameters     slf4j模板所需要插入的参数.
     */
    public static void throwEx(String messagePattern, Object... parameters) {
        throw newEx(messagePattern, parameters);
    }


}

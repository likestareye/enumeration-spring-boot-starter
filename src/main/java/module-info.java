/**
 * @author: wjf
 * @date: 2022/7/26
 */
module enumeration.spring.boot.starter {

    requires java.base;
    requires org.slf4j;
    requires org.apache.commons.lang3;
    requires org.apache.commons.collections4;
    requires org.reflections;
    requires spring.boot.starter;
    requires spring.boot.starter.json;
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires spring.core;
    requires spring.context;
    requires spring.beans;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires jakarta.annotation;

    exports cn.stareye.opensource.enumeration.std;
    exports cn.stareye.opensource.enumeration.std.container;
    exports cn.stareye.opensource.enumeration.std.serialization;
    exports cn.stareye.opensource.enumeration.spring.boot.autoconfigure;
    exports cn.stareye.opensource.enumeration.std.serialization.converter;

}

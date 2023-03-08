package com.config;

import com.common.YamlPropertySourceFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


/**
 * <code>@PropertySource</code>
 * 默认只能读取.properties文件
 * 如需加载yaml类型文件，需自行实现工厂方法 {@link YamlPropertySourceFactory}
 */
@Component
@ConfigurationProperties(prefix="app-info")
@PropertySource(value = {"classpath:custom.yml"}, factory= YamlPropertySourceFactory.class)
public class AppInfo {

    private int id;

    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "appId=" + id +
                ", appName='" + name + '\'' +
                '}';
    }
}

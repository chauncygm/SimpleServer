package com.common;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import reactor.util.annotation.Nullable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Yaml文件类型的properties加载器
 */
public class YamlPropertySourceFactory implements PropertySourceFactory {

    @SuppressWarnings("NullableProblems")
    @Override
    public PropertySource<?> createPropertySource(@Nullable String name,
            @SuppressWarnings("NullableProblems") EncodedResource resource) throws IOException {
        try {
            YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
            factory.setResources(resource.getResource());
            factory.afterPropertiesSet();
            Properties properties = factory.getObject();
            String sourceName = Strings.isBlank(name) ? resource.getResource().getFilename() : name;
            if (sourceName != null && properties != null) {
                return new PropertiesPropertySource(sourceName, properties);
            }
            throw new FileNotFoundException("load yaml resource failed.");
        } catch (Exception e) {
            throw new FileNotFoundException(e.getMessage());
        }
    }
}

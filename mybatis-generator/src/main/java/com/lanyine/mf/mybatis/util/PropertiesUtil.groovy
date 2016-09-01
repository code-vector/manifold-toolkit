package com.lanyine.mf.mybatis.util

/**
 * Created by shadow on 2016/8/23.
 */
class PropertiesUtil {

    def static getProperties(String resourceFile) {
        Properties properties = new Properties();
        try {
            properties.load(PropertiesUtil.class.getClassLoader().getResourceAsStream(resourceFile))
            return properties;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    def static getValue(String resourceFile, String name) {
        if (resourceFile == null || name == null) {
            return null;
        }
        return getProperties(resourceFile).getProperty(name);
    }
}

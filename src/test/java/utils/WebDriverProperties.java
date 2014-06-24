package utils;

import java.io.IOException;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.isNotBlank;


public class WebDriverProperties {

    public static String getProperty(String propertyName) {
        if (isNotBlank(System.getProperty(propertyName))) {
            return System.getProperty(propertyName);
        } else {
            Properties properties = loadProperties();
            return properties.getProperty(propertyName);
        }
    }

    private static Properties loadProperties()  {
        Properties properties = new Properties();
        try {
            properties.load(WebDriverProperties.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}

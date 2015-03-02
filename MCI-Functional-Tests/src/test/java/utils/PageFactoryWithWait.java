package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import pages.Page;

import java.lang.reflect.Constructor;

public class PageFactoryWithWait {

    public static <T extends Page> T initialize(WebDriver webDriver, Class<T> pageClass)  {
        return initialize(webDriver, pageClass, true);
    }

    public static <T extends Page> T initialize(WebDriver webDriver, Class<T> pageClass, boolean waitForPageToLoad)  {
        Constructor<?> constructor = null;
        try {
            constructor = pageClass.getConstructor(WebDriver.class);
            T page = (T) constructor.newInstance(webDriver);
            if (waitForPageToLoad) {
                page.waitForPageToLoad();
                            }
            PageFactory.initElements(webDriver, page);
            return page;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}

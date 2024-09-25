package com.nesine;

import com.nesine.selector.Selector;
import com.nesine.selector.SelectorFactory;
import com.nesine.selector.SelectorType;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Objects;
public class HookImpl {


    private Logger logger = Logger.getLogger(getClass());
    protected static AppiumDriver<MobileElement> appiumDriver;
    protected static FluentWait<AppiumDriver<MobileElement>> appiumFluentWait;
    public static boolean localAndroid = true;
    protected static Selector selector;
    DesiredCapabilities capabilities;
    URL localUrl;

    String testPlatform = "local";
    String os = "android";


    @Before
    public void beforeScenario() {
        try {
            logger.info("************************************  BeforeScenario  ************************************");

            localUrl = new URL("http://127.0.0.1:4723");

            if (Objects.equals(testPlatform, "local")) {
                if (Objects.equals(os, "android")) {
                    logger.info("Local cihazda Android ortamında test ayağa kalkacak");
                    appiumDriver = new AndroidDriver<>(localUrl, androidCapabilities());
                }
            } else {
              logger.error("Unsupported system.");
            }

            selector = SelectorFactory
                    .createElementHelper(localAndroid ? SelectorType.ANDROID : SelectorType.IOS);
            appiumFluentWait = new FluentWait<AppiumDriver<MobileElement>>(appiumDriver);
            appiumFluentWait.withTimeout(Duration.ofSeconds(30))
                    .pollingEvery(Duration.ofMillis(250))
                    .ignoring(NoSuchElementException.class);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    @After
    public void afterScenario() {
        if (appiumDriver != null)
            appiumDriver.quit();
    }
    public DesiredCapabilities androidCapabilities() {
        capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
        capabilities.setCapability(MobileCapabilityType.FULL_RESET, false);
        capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.pordiva.nesine.android");
        capabilities.setCapability("platformVersion", "9.0");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.ANDROID_UIAUTOMATOR2);
        capabilities.setCapability(MobileCapabilityType.UDID, "emulator-5554");
        return capabilities;
    }
}

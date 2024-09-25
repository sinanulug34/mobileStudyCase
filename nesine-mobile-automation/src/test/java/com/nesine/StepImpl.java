package com.nesine;

import com.nesine.model.SelectorInfo;
import com.nesine.selector.Selector;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import javax.annotation.Nullable;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;

public class StepImpl {

    private Logger logger = Logger.getLogger(getClass());
    protected static AppiumDriver<MobileElement> appiumDriver;
    protected static FluentWait<AppiumDriver<MobileElement>> appiumFluentWait;
    protected static Selector selector;
    protected boolean localAndroid = true;

    public StepImpl() {
        this.appiumDriver = HookImpl.appiumDriver;
        this.appiumFluentWait = HookImpl.appiumFluentWait;
        this.selector = HookImpl.selector;
        this.localAndroid = true;

    }
    @And("Find element by key {string} and type the promotion code value")
    public void typePromotionCode(String key) {
        doesElementExistByKey(key, 5);
        findElementByKey(key).sendKeys(generatePromotionCode());

    }
    @And("Find element by key {string} and type the value {string}")
    public void sendKeysByKeyNotClear(String key, String text) {
        doesElementExistByKey(key, 5);
        findElementByKey(key).sendKeys(text);

    }
    @And("Find elements by key and type username {string}")
    public void sendUserUsernameCredential(String usernameKey) {
        String username = ConfigReader.getProperty("username");
        logger.info("username is :" + username);
        findElementByKey(usernameKey).sendKeys(username);
    }
    @And("Find elements by key and type password {string}")
    public void sendUserNameCredential(String passwordKey) {
        String password = ConfigReader.getProperty("password");
        findElementByKey(passwordKey).sendKeys(password);
    }
    @And("Check visibility of element {string} on the page")
    public void isElementExist(String key) {
        Assert.assertNotNull(findElementByKey(key));
    }
    @And("compare customer number values")
    public void CompareValues(){

    }
    @Then("Verify equality of text values for elements {string} and {string}")
    public void verifyTextsAreEqual(String key1, String key2) {
        String firstValue = findElementByKey(key1).getText();
        clickByKey("memberButton");
        String secondValue = findElementByKey(key2).getText();
        Assert.assertEquals("Element texts do not match", firstValue, secondValue);
    }

    @Then("Validate that text {string} is visible to the user")
    public void verifyTextIsDisplayed(String expectedText) {
        MobileElement element = findElementByText(expectedText);
        Assert.assertNotNull("Element with text '" + expectedText + "' not found", element);
        Assert.assertTrue("Element with text '" + expectedText + "' is not displayed", element.isDisplayed());
        String actualText = element.getText();
        assertEquals("Text mismatch", expectedText, actualText);
    }

    @And("Sleep for {int} seconds")
    public void waitBySecond(int s) {
        try {
            logger.info(s + " saniye bekleniyor.");
            TimeUnit.SECONDS.sleep(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Given("Start Nesine android application")
    public void startApp(){
        try {
            appiumDriver.terminateApp("com.pordiva.nesine.android");
        } catch (Exception e) {
            logger.info("Açık olan app kapatıldı");
        }
        appiumDriver.activateApp("com.pordiva.nesine.android");
        logger.info("uygulama başlatıldı");
        waitBySecond(5);

    }

    @And("Trigger click event for element {string}")
    public void clickByKey(String key) {
        doesElementExistByKey(key, 5);
        findElementByKey(key).click();
        logger.info(key + "elemente tıkladı");
    }
    @And("{string} li elementin yüklenmesini <time> saniye bekle")
    public boolean doesElementExistByKey(String key, int time) {
        SelectorInfo selectorInfo = selector.getSelectorInfo(key);
        try {
            WebDriverWait elementExist = new WebDriverWait(appiumDriver, time);
            elementExist.until(ExpectedConditions.visibilityOfElementLocated(selectorInfo.getBy()));
            return true;
        } catch (Exception e) {
            logger.info(key + " aranan elementi bulamadı");
            return false;
        }
    }

    @And("Swipe to end of the page")
    public void swipeDown() {
        Dimension size = appiumDriver.manage().window().getSize();
        int startX = size.width / 2;
        int startY = (int) (size.height * 0.8);
        int endY = (int) (size.height * 0.2);
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(new Pause(finger, Duration.ofMillis(200)));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.viewport(), startX, endY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        appiumDriver.perform(Collections.singletonList(swipe));
    }

    public List<MobileElement> findElements(By by) throws Exception {
        List<MobileElement> webElementList = null;
        try {
            webElementList = appiumFluentWait.until(new ExpectedCondition<List<MobileElement>>() {
                @Nullable
                @Override
                public List<MobileElement> apply(@Nullable WebDriver driver) {
                    List<MobileElement> elements = driver.findElements(by);
                    return !elements.isEmpty() ? elements : null;
                }
            });
            if (webElementList == null) {
                throw new NullPointerException(String.format("by = %s Web element list not found", by.toString()));
            }
        } catch (Exception e) {
            throw e;
        }
        return webElementList;
    }

    public MobileElement findElement(By by) throws Exception {
        MobileElement mobileElement;
        try {
            mobileElement = findElements(by).get(0);
        } catch (Exception e) {
            throw e;
        }
        return mobileElement;
    }

    public MobileElement findElementByKey(String key) {
        SelectorInfo selectorInfo = selector.getSelectorInfo(key);

        MobileElement mobileElement = null;
        try {
            mobileElement = selectorInfo.getIndex() > 0 ? findElements(selectorInfo.getBy())
                    .get(selectorInfo.getIndex()) : findElement(selectorInfo.getBy());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mobileElement;
    }

    public List<MobileElement> findElemenstByKey(String key) {
        SelectorInfo selectorInfo = selector.getSelectorInfo(key);
        List<MobileElement> mobileElements = null;
        try {
            mobileElements = findElements(selectorInfo.getBy());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mobileElements;
    }

    private MobileElement findElementByText(String text) {
        return appiumDriver.findElement(By.xpath("//*[contains(@text,'" + text + "')]"));
    }
    public static String generatePromotionCode() {
        Random random = new Random();
        StringBuilder codeBuilder = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            codeBuilder.append(random.nextInt(10));
        }
        return codeBuilder.toString();
    }


}


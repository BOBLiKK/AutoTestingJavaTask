package com.stv.factory.factorypages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SharedHostingPage extends BasePage {

    @FindBy(xpath = "//*[self::h1 or self::h2][contains(normalize-space(.),'Buy Shared Web Hosting Plans')]")
    private WebElement header;

    @FindBy(xpath =
            "//*[self::h2 or self::h3][normalize-space(.)='Power']" +
                    "/ancestor::*[self::div or self::section][1]"
    )
    private WebElement powerCard;

    @FindBy(xpath =
            "//*[self::h2 or self::h3][normalize-space(.)='Power']" +
                    "/ancestor::*[self::div or self::section][1]" +
                    "//*[self::a or self::button][contains(normalize-space(.),'Select')]"
    )
    private WebElement powerSelectBtn;

    public SharedHostingPage(WebDriver driver) {
        super(driver);
    }

    public void selectPowerPlan() {
        waitVisible(header);

        scrollIntoView(powerCard);

        try {
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -160);");
        } catch (Exception ignored) {}

        scrollIntoView(powerSelectBtn);

        try {
            click(powerSelectBtn);
            return;
        } catch (Exception ignored) {}

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", powerSelectBtn);
    }
}

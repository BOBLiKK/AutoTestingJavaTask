package com.stv.factory.factorypages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DomainPage extends BasePage {

    @FindBy(xpath = "//button[.//span[contains(normalize-space(.),'Choose my domain later')]]")
    private WebElement chooseMyDomainLaterBtn;

    public DomainPage(WebDriver driver) {
        super(driver);
    }

    public void chooseMyDomainLater() {
        waitVisible(chooseMyDomainLaterBtn);

        scrollIntoView(chooseMyDomainLaterBtn);

        try {
            click(chooseMyDomainLaterBtn);
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", chooseMyDomainLaterBtn);
        }
    }
}

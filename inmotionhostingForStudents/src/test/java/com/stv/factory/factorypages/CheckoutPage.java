package com.stv.factory.factorypages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

public class CheckoutPage extends BasePage {


    @FindBy(css = "select")
    private WebElement billingSelect;

    @FindBy(css = "select option")
    private List<WebElement> billingOptions;

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public List<String> getBillingOptionsText() {
        waitVisible(billingSelect);


        try { click(billingSelect); } catch (Exception ignored) {}

        wait.until(d -> billingOptions != null && !billingOptions.isEmpty());

        return billingOptions.stream()
                .map(e -> safeText(e).replaceAll("\\s+", " ").trim())
                .collect(Collectors.toList());
    }

    public boolean bugPriceFoundFor2yOr3y() {
        List<String> options = getBillingOptionsText();

        return options.stream().anyMatch(t -> t.contains("2 Years") && t.contains("$4.99"))
                || options.stream().anyMatch(t -> t.contains("3 Years") && t.contains("$4.99"));
    }
}

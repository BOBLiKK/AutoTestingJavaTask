package com.stv.factory.factorypages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class CheckoutPage extends BasePage {


    //Task 7

    private final By billingTermOptions = By.cssSelector("select option");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public boolean billingDropdownContains(String fragment) {
        List<String> texts = driver.findElements(billingTermOptions)
                .stream()
                .map(e -> e.getText().replaceAll("\\s+", " ").trim())
                .toList();

        return texts.stream().anyMatch(t -> t.contains(fragment));
    }
}

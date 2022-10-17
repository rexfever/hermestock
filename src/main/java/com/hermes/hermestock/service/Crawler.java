package com.hermes.hermestock.service;

import com.hermes.hermestock.domain.Buyer;
import com.hermes.hermestock.domain.Market;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Crawler {

    private WebDriver driver;
    private WebElement element;
    private WebElement dataElement;
    private WebElement selectElement;
    private WebElement selectElementButton;
    private WebElement selectElementCSVButton;

    public static String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static Path path = FileSystems.getDefault().getPath("");
    public static String directoryName = path.toAbsolutePath().toString();
    public static String WEB_DRIVER_PATH = directoryName + "/chromedriver";

    private String base_url = "http://data.krx.co.kr/contents/MDC/MDI/mdiLoader/index.cmd?menuId=MDC0201020303";

    public Crawler() {}

    public void getCSVfiles() {
        Common common = new Common();
        String formatedNow = common.getDate();
        //formatedNow = "20220203"; //테스트용

        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--headless");
        options.setCapability("ignoreProtectedModeSettings", true);

        driver = new ChromeDriver(options);

        driver.get(base_url);

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("cal-area")));

        element = driver.findElement(By.name("endDd"));
        element.clear();
        element.sendKeys(formatedNow);
        element = driver.findElement(By.name("strtDd"));
        element.clear();
        element.sendKeys(formatedNow);

        for(Market m : Market.values()) {
            element = driver.findElement(By.cssSelector("#MDCSTAT024_FORM > div.search_tb > div > table > tbody > tr:nth-child(1) > td > label:nth-child(" + m.getCodeOfMarket() + ")"));
            element.click();
            selectElement = driver.findElement(By.id("invstTpCd"));
            Select selectObject = new Select(selectElement);
            for(Buyer b : Buyer.values()) {
                selectObject.selectByValue(b.getCodeOfBuyer());
                selectElement = driver.findElement(By.id("jsSearchButton"));
                selectElement.click();
                dataElement = driver.findElement(By.className("CI-GRID-BODY-TABLE-TBODY"));
                if(!dataElement.getText().endsWith("데이터가 없습니다.")) {
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"jsMdiContent\"]/div/div[1]/div[1]/div[1]/div[2]/div/div/table/tbody/tr[1]")));
                    selectElementButton = driver.findElement(By.xpath("//*[@id=\"MDCSTAT024_FORM\"]/div[2]/div/p[2]/button[2]"));
                    selectElementButton.click();
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("CSV")));
                    selectElementCSVButton = driver.findElement(By.linkText("CSV"));
                    selectElementCSVButton.click();
                }else{
                    System.out.println("데이터가 없습니다.");
                    break;
                }
            }
        }
    }

}

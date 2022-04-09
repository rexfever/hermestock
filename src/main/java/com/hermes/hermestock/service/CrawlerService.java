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
import org.springframework.stereotype.Service;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Date;

@Service
public class CrawlerService {
    private WebDriver driver;
    private WebElement element;
    private WebElement dataElement;
    private WebElement selectElement;
    private WebElement selectElementButton;
    private WebElement selectElementCSVButton;

    private static String WEB_DRIVER_ID = "webdriver.chrome.driver";
    private static Path path = FileSystems.getDefault().getPath("");
    private static String directoryName = path.toAbsolutePath().toString();
    private static String WEB_DRIVER_PATH = directoryName + "/chromedriver";

    public CrawlerService() {
    }
    //그날의 주식 시세 가져오는 함수
    public void getStockConditionCsv(int period){

        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.setCapability("ignoreProtectedModeSettings", true);

        driver = new ChromeDriver(options);
        driver.get("http://data.krx.co.kr/contents/MDC/MDI/mdiLoader/index.cmd?menuId=MDC0201020102");

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("cal-area")));

        //날짜 셋팅

        element = driver.findElement(By.xpath("//*[@id=\"MDCSTAT016_FORM\"]/div[1]/div/table/tbody/tr[2]/td/div/div/button[" +period+ "]"));
        element.click();

        //시장 선택
        element = driver.findElement(By.cssSelector("#MDCSTAT016_FORM > div.search_tb > div > table > tbody > tr:nth-child(1) > td > label:nth-child(2)"));

        element.click();

        //조회 버튼 클릭
        selectElement = driver.findElement(By.id("jsSearchButton"));
        selectElement.click();

        //조회 결과
        dataElement = driver.findElement(By.className("CI-GRID-BODY-TABLE-TBODY"));

        if(!dataElement.getText().endsWith("데이터가 없습니다.")) {
            //조회된 첫 라인이 나올때 까지 대기
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"jsMdiContent\"]/div/div[1]/div[1]/div[1]/div[2]/div/div/table/tbody/tr[1]")));
            selectElementButton = driver.findElement(By.xpath("//*[@id=\"MDCSTAT016_FORM\"]/div[2]/div[1]/p[2]/button[2]"));
            selectElementButton.click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("CSV")));
            selectElementCSVButton = driver.findElement(By.linkText("CSV"));
            selectElementCSVButton.click();
        }else{
            System.out.println("데이터가 없습니다.");
        }

    }

    public void getTop5Csv(String formatedNow) {

        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.setCapability("ignoreProtectedModeSettings", true);

        driver = new ChromeDriver(options);

        driver.get("http://data.krx.co.kr/contents/MDC/MDI/mdiLoader/index.cmd?menuId=MDC0201020303");

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

package com.nesine;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RunWith(Cucumber.class)
    //Cucumber ayarlarinin yapildigi blok
    @CucumberOptions(
            features = "classpath:features/nesine.feature",
            glue = {"com.nesine.step"},
            plugin = {"pretty"},
            monochrome = true
    )


public class RunnerCucumberTest {
        Properties properties = new Properties();
        WebDriver driver;
        String url = "https://www.nesine.com/";
        ArrayList<Integer> oynanmaSayisi, kodNumaralari,playedCount,marketNo ;
       @Given("Chrome ayaga kaldirilir.")
        public void chromeHazirlik() {
           //Chrome özelliklerinin verilmesini, driveri baslatilmasini ve tam ekran gelmesini saglar.
            System.setProperty("webdriver.chrome.driver", "libs/chromedriver.exe");
            this.driver = new ChromeDriver();
            driver.manage().window().maximize();

       }

       @Given("Edge ayaga kaldirilir.")
        public void edgeHazirlik() {
           //Edge ozelliklerinin verilmesini, driveri baslatilmasini ve tam ekran gelmesini saglar.
           System.setProperty("webdriver.edge.driver", "libs/msedgedriver.exe");
           EdgeOptions edgeOptions = new EdgeOptions();
           driver = new EdgeDriver(edgeOptions);
           driver.manage().window().maximize();
        }

        @Given("PopUplar Kapatilir.")
        public void popUpKapat() {
           //PopUplar varsa ilk popupi tiklayip kapatir ikinci popup'i esc tusuna basarak kapatir.
            WebElement firstPopUp = driver.findElement(By.className("nsn-i-cancel-b"));
            WebElement secondPopUp = driver.findElement(By.cssSelector("[class='modal-content'] [id='popupNotice'] a"));
            if (firstPopUp.isDisplayed() && firstPopUp.isEnabled()) {
                firstPopUp.click();
            }
            if (secondPopUp.isDisplayed() && secondPopUp.isEnabled()) {
                secondPopUp.sendKeys(Keys.ESCAPE);
            }
            secondPopUp = driver.findElement(By.cssSelector("[id='c-p-bn']"));
            secondPopUp.click();

        }

        @Given("Nesine.com sayfasina gidilir.")
        public void nesineAnasayfa(){
            driver.get(url);
        }

        @Given("POM.xml den okunan tckn girilir.")
        // pom.xml iceresindeki properties tag'inin altinda username etiketi icesirinde tckn yazan yere tckn girilir.
        public void tcknGir() {
           //pom.xmlden username okuyarak ilgili alanlari doldurur.
            try (InputStream inputStream = RunnerCucumberTest.class.getClassLoader().getResourceAsStream("config.properties")) {
                properties.load(inputStream);
                String username = properties.getProperty("username");
                WebElement element = driver.findElement(By.id("txtUsername"));
                element.sendKeys(username);

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        @Given("POM.xml den okunan parola girilir.")
        // pom.xml iceresindeki properties tag'inin altinda password etiketi icesirinde sifre yazan yere sifre girilir.
        public void sifreGir() {
            //pom.xmlden sifre okuyarak ilgili alanlari doldurur.
            try (InputStream inputStream = RunnerCucumberTest.class.getClassLoader().getResourceAsStream("config.properties")) {
                properties.load(inputStream);
                String password = properties.getProperty("password");
                WebElement parola = driver.findElement(By.id("realpass"));
                parola.sendKeys(password);

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        @Given("Giris butonuna tiklanir.")
        public void loginButton() {
            WebElement button = driver.findElement(By.linkText("GİRİŞ"));
            if (button.isDisplayed() && button.isEnabled()) {
                button.click();
            }
        }

        @Given("Populer bahisler butonuna tiklanir.")
        public void populer()  {
            WebElement populer = driver.findElement(By.linkText("Popüler Bahisler"));
            if (populer.isDisplayed() && populer.isEnabled()) {
                populer.click();
            }
        }

        @Given("Populer bahisler sayfasinin linki kontrol edilir.")
        public void kontrol() {
            String URL = driver.getCurrentUrl();
            Assert.assertEquals(URL, "https://www.nesine.com/iddaa/populer-bahisler");
            System.out.println("Populer bahisler sayfasinin geldigi goruldu.");
        }

        @Given("Futbol tabi secilir.")
        public void futbolTabi() {
            WebElement futbol = driver.findElement(By.xpath("//div/nav/button[1]"));
            if (futbol.isDisplayed() && futbol.isEnabled()) {//ilgili tab gorunuyor ve aktif ise tiklar.
                futbol.click();
            }
        }
        @When("Oynanma sayisi listeye çekilir.")
        public void oynanmaSayisi() throws InterruptedException {
            TimeUnit.SECONDS.sleep(1); //Thread.sleep(1000);
            oynanmaSayisi = new  ArrayList<Integer>();
            List<WebElement> listElement = driver.findElements(By.className("playedCount"));
            for(int i =0;i<listElement.size();i++)
            {
                String x = listElement.get(i).getText();
                String y= x.replace(".","");
                oynanmaSayisi.add(Integer.parseInt(y));
            }
            System.out.println("Oynanma Sayisi:" + oynanmaSayisi);
        }
        @When("Kod numaralari listeye atilir.")
        public  void kod(){
            kodNumaralari =new ArrayList<Integer>();
            List<WebElement> listElement1 = driver.findElements(By.xpath("//div[@class='betLine']/div[@class='matchCode']//span"));
            for(int i =0;i<listElement1.size();i++) {
                String x = listElement1.get(i).getText();
                kodNumaralari.add(Integer.parseInt(x));
            }
            System.out.println("Kod Numaralari :" + kodNumaralari);
        }
        @When("Post methodu ile Oynanma sayisi ve Kod çekilir.")
        public void post() {
            String query_url = url+"/Iddaa/GetPopularBets";
            String json = "{\"eventType\":1,\"date\":null}";
            try {
                URL url = new URL(query_url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");
                OutputStream os = conn.getOutputStream();
                os.write(json.getBytes("UTF-8"));
                os.close();
                // Response'in okunmasi
                InputStream in = new BufferedInputStream(conn.getInputStream());
                String result = IOUtils.toString(in, "UTF-8");
                System.out.println(result);
                System.out.println("result after Reading JSON Response");
                JSONObject myResponse = new JSONObject(result);
                JSONArray response = myResponse.getJSONArray("PopularBetList");
                playedCount = new ArrayList<>();
                marketNo = new ArrayList<>();

                for(int i=0; i<response.length(); i++){
                    playedCount.add(response.getJSONObject(i).getInt("PlayedCount"));
                    marketNo.add(response.getJSONObject(i).getInt("MarketNo"));
                }

                System.out.println("Oynanma Sayisi:" + playedCount );
                System.out.println("Kod Numaraları:" + marketNo );
                in.close();
                conn.disconnect();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        @When("Oynanma sayisi ve Kod değerleri kontrol edilir.")
        public void liste() throws InterruptedException {
            Assertions.assertEquals(playedCount , oynanmaSayisi);
            System.out.println("playedCount kontrol edildi");
            Assertions.assertEquals(marketNo , kodNumaralari);
            System.out.println("marketNo kontrol edildi");

        }
        @Then("Hesabim'a tiklanir.")
        public void hesabim(){
            WebElement hesabim = driver.findElement(By.linkText("Hesabım"));
            if (hesabim.isDisplayed() && hesabim.isEnabled()) {
                hesabim.click();
            }
        }
        @Then("Cikis yapilir.")
        public void cikis(){
            WebElement cikis = driver.findElement(By.xpath("//a[contains(text(),'Çıkış')]"));
            if (cikis.isDisplayed() && cikis.isEnabled()) {
                cikis.click();
            }
        }

        @Then("Tarayici kapatilir.")
        public void tarayiciKapat () {
            driver.quit();
        }
    }

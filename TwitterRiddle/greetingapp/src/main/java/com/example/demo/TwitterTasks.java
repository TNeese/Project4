package com.example.demo;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Component
public class TwitterTasks {


    private static String consumerKeyStr = "8wVATS3n8Cwrl0lFvzTvEV1HQ";
    private static String consumerSecretStr = "K18NW16KzuIEKDvBiHhpInuR0phsKQe6HPsbmf5cPJTV6tQniW";
    private static String accessTokenStr = "1045396671633510400-z8iFRF74gvsYFIA3uGiop6X6I1M7u9";
    private static String accessTokenSecretStr = "1d34Hbc5oYdClyLkK2JfUCIUZdIXZji9h0BraB2TtRbmS";

    private static WebDriver driver;
    private final TwitterDao twitterDao;
    private int id = 56;

    @Autowired
    public TwitterTasks(TwitterDao twitterDao) {
        this.twitterDao = twitterDao;
    }

    private static void setUpChrome() {
        //makes selenium headless and able to run constantly in background
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);
        System.setProperty("webdriver.chrome.driver",
                "D:\\Fall18-TestingQA\\chromedriver_win32\\chromedriver.exe");
        driver = new ChromeDriver();
    }

    //schedule to run at 11:30 every morning
    //gives selenium time to find new riddle if exists
    @Scheduled(cron = "0 30 11 * * ?")
    private void getRiddleOfTheDay() {
        setUpChrome();
        driver.get("https://www.riddles.com/riddle-of-the-day");

        WebElement foundRiddle = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[1]/div[2]/div/div[1]/div[2]/div[1]/blockquote/p"));
        String words = foundRiddle.getText();

        WebElement showAnswer = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[1]/div[2]/div/div[1]/div[2]/a"));
        showAnswer.click();
        WebElement getAnswer = driver.findElement(By.xpath("//*[@id=\"collapse1261\"]/div[1]/div/blockquote/p"));
        String answer = getAnswer.getText();
        driver.close();

        Riddles riddle = new Riddles(0,words,answer);

        twitterDao.add(riddle);
        id ++;
    }

    //schedule to run once a day around noon?
    @Scheduled(cron = "0 0 12 * * ?")
    public void postRiddle() throws Exception {
        //grabs newest riddle from database
        Riddles gettingRiddle = twitterDao.getById(id);
        //grab riddle from yesterday to get answer
        Riddles yesterdayAnswer = twitterDao.getById(id-1);
        String riddle = gettingRiddle.getRiddle();
        System.out.println(gettingRiddle.getId());

        String almostPostableRiddle = URLEncoder.encode(riddle, "UTF-8");
        String postableRiddle = almostPostableRiddle.replace("+", "%20");

        String answer = yesterdayAnswer.getAnswer();

        String almostPostableAnswer = URLEncoder.encode(answer,"UTF-8");
        String postableAnswer = almostPostableAnswer.replace("+", "%20");

        OAuthConsumer oAuthConsumer = new CommonsHttpOAuthConsumer(consumerKeyStr, consumerSecretStr);
        oAuthConsumer.setTokenWithSecret(accessTokenStr, accessTokenSecretStr);
        String uri = "https://api.twitter.com/1.1/statuses/update.json?status=" + postableRiddle + "%0D%0A%0D%0AYesterday%27s%20Answer%3A%20" + postableAnswer;
        HttpPost httpPost = new HttpPost(uri);
        oAuthConsumer.sign(httpPost);
        HttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build()).build();
        HttpResponse httpResponse = httpClient.execute(httpPost);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        System.out.println(statusCode + ':' + httpResponse.getStatusLine().getReasonPhrase());
        System.out.println(IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8"));

    }






}

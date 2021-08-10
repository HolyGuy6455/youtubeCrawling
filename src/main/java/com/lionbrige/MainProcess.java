package com.lionbrige;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * MainProcess
 */
public class MainProcess {

    public static List<YTDComments> run(String URL) {
        WebCrawler crawler = WebCrawler.getInstance();
        WebDriverWait wait = crawler.getWait();
        int commentsCountValue = 0;
        crawler.getDriver().get(URL);
        Logger.log("wating for load...");
        WebElement firstResult = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("ytd-comments")));
        
        sleep(3000);
        Logger.log("offsetTop : "+firstResult.getAttribute("offsetTop"));
        crawler.getJsExecutor().executeScript("scroll(0,arguments[0])",firstResult.getAttribute("offsetTop"));
        if(Integer.parseInt(firstResult.getAttribute("offsetTop")) == 0){
            return new ArrayList<YTDComments>();
        }

        try {
            WebElement comments = crawler.getDriver().findElement(By.cssSelector("ytd-comments"));
            WebElement commentsCount = comments.findElement(By.cssSelector("h2#count"));
            List<WebElement> commentRenderers;
            String commentsCountStr = commentsCount.getText();            
            commentsCountValue = Integer.parseInt(commentsCountStr.substring(3, commentsCountStr.length()-1).replace(",", ""));
            if(commentsCountValue == 0){
                Logger.log("There are no Comments");
                return new ArrayList<YTDComments>();
            }
            Logger.log("commentsCountValue : " + commentsCountValue);
            commentRenderers = crawler.getDriver()
                    .findElements(By.cssSelector("ytd-comment-renderer"));
            Logger.log("commentsCountValue : " + commentsCountValue + "  \ncomments list size : " + commentRenderers.size());
            if(commentsCountValue == commentRenderers.size()){
                Logger.log("oh! already done!");
            }

            WebElement continuation = comments.findElement(By.cssSelector("#continuations"));
            WebElement continuationSpinner = continuation.findElement(By.cssSelector("#spinner"));
            WebElement scrollHeightElement = crawler.getDriver().findElement(By.cssSelector("#primary-inner"));
            
            int scrollHeight = 0;
            String scrollHeightStr;
            long timeBefore = System.currentTimeMillis();

            while ((System.currentTimeMillis() - timeBefore) < 100000) {

                scrollHeightStr = scrollHeightElement.getAttribute("scrollHeight");
                commentRenderers = crawler.getDriver().findElements(By.cssSelector("ytd-comment-renderer"));

                if(Integer.valueOf(scrollHeightStr) != scrollHeight){
                    scrollHeight = Integer.valueOf(scrollHeightStr);
                    timeBefore = System.currentTimeMillis();
                }

                crawler.getJsExecutor().executeScript("scroll(0,document.querySelector('#primary-inner').scrollHeight);");
                Logger.log("scroll..." + commentRenderers.size() + " / " + commentsCount.getText());
                try {
                    continuationSpinner.isEnabled();
                } catch (StaleElementReferenceException e) {
                    break;
                }
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }

        clickAllElements(crawler, crawler.getDriver().findElements(By.cssSelector("ytd-button-renderer#more-replies")));
        clickAllElements(crawler, crawler.getDriver().findElements(By.cssSelector("paper-button#more")));

        List<YTDComments> result = new ArrayList<YTDComments>();

        List<WebElement> commentThreadRenderers;
        commentThreadRenderers = crawler.getDriver()
                .findElements(By.cssSelector("ytd-comment-thread-renderer"));

        Logger.log("copy comments : 0/" + commentThreadRenderers.size());
        int count = 0;

        for (WebElement commentThreadRenderer : commentThreadRenderers) {
            List<WebElement> commentChildRenderers = commentThreadRenderer
                    .findElements(By.cssSelector("ytd-comment-renderer"));
            WebElement parentComment = commentChildRenderers.get(0);
            commentChildRenderers.remove(0);

            YTDComments ytdCommentParent = new YTDComments();
            ytdCommentParent.setFieldFromWebElement(parentComment);
            ytdCommentParent.setURL(URL);
            result.add(ytdCommentParent);
            count++;
            
            for (WebElement childRenderer : commentChildRenderers) {
                YTDComments ytdComment = new YTDComments();
                ytdComment.setFieldFromWebElement(childRenderer);
                ytdComment.setParent(ytdCommentParent);
                ytdComment.setURL(URL);
                result.add(ytdComment);
                Logger.log("copy comments : " + (count) + "/" + commentThreadRenderers.size() +"\n  "
                + "Result Size : " + result.size() + "/" + commentsCountValue);
            }
        }

        return result;
    }

    /*
     * 편의성을 위해 만든 sleep 함수
     */
    private static void sleep(int amount) {
        synchronized (WebCrawler.getInstance()) {
            try {
                Thread.sleep(amount);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * 주어진 WebElements들을 그냥 다 클릭해버리는 아주 무식한 매서드
     */
    private static void clickAllElements(WebCrawler crawler, List<WebElement> webElementList) {
        final int listSize = webElementList.size();
        int count = 0;
        for (WebElement webElement : webElementList) {
            count++;
            crawler.getJsExecutor().executeScript("arguments[0].click()", webElement);
            Logger.log("click : " + count + "/" + listSize);
        }
    }
}
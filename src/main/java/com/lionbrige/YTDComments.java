package com.lionbrige;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * YTDComments
 */
public class YTDComments {

    private String content;
    private String userName;
    private String strURL;
    private YTDComments parent;

    public void setFieldFromWebElement(WebElement element) {
        WebElement mainElement = element.findElement(By.cssSelector("#main"));
        WebElement userNameElement = mainElement.findElement(By.cssSelector("a#author-text"));
        WebElement contentElement = mainElement.findElement(By.cssSelector("#content"));
        userName = userNameElement.getText();
        content = contentElement.getText();
        if(userName.compareTo("") == 0){
            try {
                userNameElement = mainElement.findElement(By.cssSelector("ytd-channel-name#channel-name"));
                userName = userNameElement.getText();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(YTDComments parent) {
        this.parent = parent;
    }

    /**
     * @param URL the URL to set
     */
    public void setURL(String strURL) {
        this.strURL = strURL;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @return the parent
     */
    public YTDComments getParent() {
        return parent;
    }

    /**
     * @return the strURL
     */
    public String getStrURL() {
        return strURL;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return "[" + userName + "] : " + content;
    }
}
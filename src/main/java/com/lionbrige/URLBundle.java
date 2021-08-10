package com.lionbrige;

import java.util.ArrayList;
import java.util.List;

/**
 * URLBundle
 */
public class URLBundle {

    private String sheetName;
    private List<String> urlList;

    public URLBundle(String sheetName) {
        this.sheetName = sheetName;
        this.urlList = new ArrayList<String>();
    }

    /**
     * @return the sheetName
     */
    public String getSheetName() {
        return sheetName;
    }

    /**
     * @return the urlList
     */
    public List<String> getUrlList() {
        return urlList;
    }

}
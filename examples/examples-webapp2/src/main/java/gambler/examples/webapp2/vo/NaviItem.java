/*
 * @(#) MenuVO.java 2013-11-29
 * 
 * Copyright 2010 NetEase.com, Inc. All rights reserved.
 */
package gambler.examples.webapp2.vo;

/**
 * Class MenuVO
 *
 * @author Administrator
 * @version 2013-11-29
 */
public class NaviItem {

    private String name;

    private String url;

    private String target;

    /**
     * @param name
     * @param url
     */
    public NaviItem(String name, String url) {
        super();
        this.name = name;
        this.url = url;
    }

    public NaviItem(String name, String url, String target) {
        super();
        this.name = name;
        this.url = url;
        this.target = target;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}

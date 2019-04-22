package com.media.spiders.producers;

/**
 * @desc 需要的数据对象，目前只需要链接，LinkTypeData.java
 */
public class LinkTypeData {

    private int id;
    /**
     * 链接的地址
     */
    private String linkHref;
    /**
     * 链接的标题
     */
    private String linkText;
    /**
     * 摘要
     */
    private String summary;
    /**
     * 内容
     */
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLinkHref() {
        return linkHref;
    }

    public void setLinkHref(String linkHref) {
        this.linkHref = linkHref;
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
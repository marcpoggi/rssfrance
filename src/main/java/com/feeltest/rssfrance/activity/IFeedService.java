package com.feeltest.rssfrance.activity;


import com.feeltest.rssfrance.model.Base;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;

/**
 * Created by mpoggi on 12/10/2014.
 */
public interface IFeedService {
    public Boolean getAllNews(String url);
    public SyndFeed getAllNewsfromCache(String url);
    Base getXml(int i);
}

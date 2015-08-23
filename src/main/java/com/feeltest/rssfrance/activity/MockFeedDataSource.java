package com.feeltest.rssfrance.activity;

import com.feeltest.rssfrance.activity.IFeedService;
import com.feeltest.rssfrance.model.Base;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;

/**
 * Created by mpoggi on 12/10/2014.
 */
public class MockFeedDataSource implements IFeedService {
    @Override
    public Boolean getAllNews(String url) {
        return null;
    }

    @Override
    public SyndFeed getAllNewsfromCache(String url) {
        return null;
    }

    @Override
    public Base getXml(int i) {

        return null;
    }
}

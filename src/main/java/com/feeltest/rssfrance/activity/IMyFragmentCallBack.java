package com.feeltest.rssfrance.activity;

import java.util.HashMap;

/**
 * Created by mpoggi on 15/10/2014.
 */
public interface IMyFragmentCallBack {
    public void onItemSelected(HashMap<String, String> data);
    public void InitFragmentFavoriAndTitle();
}

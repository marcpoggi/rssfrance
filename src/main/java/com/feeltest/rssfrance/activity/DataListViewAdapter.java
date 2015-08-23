package com.feeltest.rssfrance.activity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mpoggi on 15/10/2014.
 */
public class DataListViewAdapter implements Serializable {
   // private static final long serialVersionUID = 4466821913603037341L;
    private List<HashMap<String, String>> list;
    public List<HashMap<String, String>> getList() {
        return list;
    }

    public void setList(List<HashMap<String, String>> list) {
        this.list = list;
    }
}

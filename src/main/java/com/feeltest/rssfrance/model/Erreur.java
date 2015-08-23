package com.feeltest.rssfrance.model;

import org.simpleframework.xml.Element;

/**
 * Created by mpoggi on 07/11/2014.
 */


public class Erreur {
    @Element(required = false)
    private String erreur;

    public String getErreur() {
        return erreur;
    }


}

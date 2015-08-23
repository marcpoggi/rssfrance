package com.feeltest.rssfrance.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mpoggi on 07/11/2014.
 */


public class Flux {

    @Element(required = false)
    private String source;
    @Element(required = false)
    private String adresse;
    @Element(required = false)
    private String categorie;
    @Element(required = false)
    private String geo;
    @ElementList(required = false, inline = true)
    private List<Erreur> list = new ArrayList<Erreur>();
    @Element(required = false)
    private String frequence;


    public String getFrequence() {
        return frequence;
    }

    public String getSource() {
        return source;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getCategorie() {
        return categorie;
    }

    public String getGeo() {
        return geo;
    }

    public List<Erreur> getErreur() {
        return list;
    }


}

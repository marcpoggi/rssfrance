package com.feeltest.rssfrance.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mpoggi on 07/11/2014.
 */

@Root(strict=false)
public class Base {
    @ElementList(inline = true)
    public List<Flux> list = new ArrayList<Flux>();

    public List<Flux> getMesflux() {
        return list;
    }
}

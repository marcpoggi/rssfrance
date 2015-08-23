package com.feeltest.rssfrance.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mpoggi on 11/11/2014.
 */
public class MesFavoris {
    private static ArrayList<HashMap<String, String>> mesfavoris;
    private static Type montype = new TypeToken<ArrayList<HashMap<String, String>>>() {
    }.getType();
    private static SharedPreferences pref;
    private static Gson gson;
    private static String json;


    public static ArrayList<HashMap<String, String>> LoadFavoris(Context ctx) {
        pref = ctx.getSharedPreferences("mesfavoris", ctx.MODE_PRIVATE);
        gson= new Gson();
        json = pref.getString("mesfavoris", "");
        mesfavoris = gson.fromJson(json, montype);
        return mesfavoris;
    }

    public static Boolean AddFavoris(Context ctx, String source, String adresse) {
        mesfavoris = LoadFavoris(ctx);
        if (mesfavoris ==null)
            mesfavoris  = new ArrayList<HashMap<String, String>>();

        Boolean existe = SourceExiste(mesfavoris,source);

        if (existe == false) {
            if (mesfavoris == null) mesfavoris = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> unfavori = new HashMap<String, String>();
            unfavori.put("source", source);
            unfavori.put("link", adresse);
            mesfavoris.add(unfavori);
            json = gson.toJson(mesfavoris, montype);
            pref.edit().putString("mesfavoris", json).apply();
            return true;
        }
        return false;
    }

    private static Boolean SourceExiste(ArrayList<HashMap<String, String>> obj, String source) {
        if (obj != null) {
            for (int i = 0; i < obj.size(); i++) {
                if (obj.get(i).get("source").equals(source))
                {
                    return true;

                }
            }
            return false;
        }
        return false;
    }

    public static  ArrayList<HashMap<String, String>> DeleteFavoris(Context ctx, Integer position) {
        mesfavoris = LoadFavoris(ctx);
        mesfavoris.remove(mesfavoris.get(position));
        json = gson.toJson(mesfavoris, montype);
        pref.edit().putString("mesfavoris", json).commit();
        return mesfavoris;

    }
}

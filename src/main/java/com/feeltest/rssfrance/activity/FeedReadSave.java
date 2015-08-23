package com.feeltest.rssfrance.activity;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

import com.feeltest.rssfrance.R;
import com.feeltest.rssfrance.model.Base;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEnclosureImpl;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.FeedException;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.SyndFeedInput;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.XmlReader;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class FeedReadSave implements IFeedService {

    private Context ctx;

    public FeedReadSave(Context ctx)
    {
        this.ctx= ctx;

    }

    @Override
    public Boolean getAllNews(String url)  {
        SyndFeed feed;

        try {

            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(url);
            HttpResponse response = client.execute(get);
            byte[] fluxxml = EntityUtils.toByteArray(response.getEntity());

                                    /* Push Xml to cache */

            File file;
            String fileName = Uri.parse(url).getPath();
            fileName=fileName.replaceAll("-","");
//     la methode de creation mkdirs ne marche pas sur internalstorage directement
            // on cree ou retrouve une instance de dossier avec getdir
            // apres mkdirs fonctionne sur les sous-dossiers
            File mydir = ctx.getDir("files", Context.MODE_PRIVATE);

            try {
                file = new File(mydir, fileName.substring(0, fileName.lastIndexOf(File.separator)));
                if (!file.exists())    file.mkdirs();
                file = new File(mydir,fileName);

                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
                out.write(fluxxml);
                out.close();
                //Log.i("Action : ", "cache sauvé");
                feed= getAllNewsfromCache(url);
                sauveimage(feed);
             }
            catch (IOException e) {
                //Log.i("Action : ", "cache PAS sauvé");
                //e.printStackTrace();
                return false;
            }

            //Log.i("lieu : ", "return feed;");

        }
        catch (IOException e)
            { //Log.i("Exception : ", "GET Exception");
                return false;
            }

        return true;

    }

    private void sauveimage(SyndFeed feed) {
        if (feed!=null) {
            for (SyndEntry entry : (List<SyndEntry>) feed.getEntries()) {
                try {
                    String monurl = new URL(((SyndEnclosureImpl) entry.getEnclosures().get(0)).getUrl()).toString();
                    Picasso.with(ctx).load(monurl).fetch();
                } catch (MalformedURLException e) {
                    //e.printStackTrace();
                } catch (IndexOutOfBoundsException e) {//e.printStackTrace();
                }
            }
        }

    }

    @Override
    public SyndFeed getAllNewsfromCache(String monurl) {

        String path = "file:"+ ctx.getDir("files",Context.MODE_PRIVATE).getAbsolutePath()+"/"+ (Uri.parse(monurl).getPath()).replaceAll("-","");
        //Log.i("path : ",path);
        URL uri;
        SyndFeed feed;
        SyndFeedInput input = new SyndFeedInput();
        try {
            uri = new URL(path);
            //Log.i("lieu : ", "avant Xml reader");
            String encodestring="UTF-8";
            XmlReader.setDefaultEncoding(encodestring);
            feed = input.build(new XmlReader(uri));
            return feed ;
            }
        catch (MalformedURLException e) {
            //Log.i("exception : ","malform path");
        //    e.printStackTrace();
            feed=null;
            }
        catch (FeedException e) {
             feed=null;
        //    e.printStackTrace();
            }
        catch (IOException e) {
            feed=null;
         //   e.printStackTrace();
            }
        return feed;
    }


@Override
    public Base getXml(int i)
    {
        Serializer serial = new Persister();
        int ressources=0;
        switch (i)
        {case 1 : ressources= R.raw.atlas_des_flux_fra_sport; break;
            case 2 : ressources = R.raw.atlas_des_flux_fra_presse ; break;
            case 3 : ressources = R.raw.atlas_des_flux_fra_culture ; break;
            case 4 : ressources = R.raw.atlas_des_flux_fra_economie ; break;
            case 5 : ressources = R.raw.atlas_des_flux_fra_technologie ; break;

        }
        try {
            Base baseRead = serial.read(Base.class, ctx.getResources().openRawResource(ressources),false);
            //Log.i("Message", "Objects in Level: " + baseRead.getMesflux().size());
            return baseRead;
        } catch (Resources.NotFoundException e) {
           // e.printStackTrace();
        } catch (Exception e) {
          //  e.printStackTrace();
        }
        return null;
    }
}
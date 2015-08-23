package com.feeltest.rssfrance.activity;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.feeltest.rssfrance.R;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEnclosureImpl;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeedImpl;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class syndFeedAdapter extends BaseAdapter implements Filterable{

    private final SyndFeed origsyndFeed;
    private SyndFeed syndFeed;
    private Filter syndFeedFilter;
    private Context ctx;
    private final LayoutInflater layoutInflater;

    public syndFeedAdapter(Context context, SyndFeed feed) {
        this.syndFeed = feed;
        this.origsyndFeed =feed;
        this.ctx =context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return syndFeed != null ? syndFeed.getEntries().size() : 0;
    }

    public void resetData() {
        syndFeed = origsyndFeed;
    }
    public SyndEntry getItem(int position) {
        return (SyndEntry) syndFeed.getEntries().get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        SyndEntry syndEntry = getItem(position);

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.synd_feed_list_item, parent, false);
        }

        if (syndEntry != null) {
            TextView textView;
            ImageView imageView;
            if (syndEntry.getTitle() != null) {
                textView = (TextView) convertView.findViewById(R.id.synd_feed_title);
                textView.setText(syndEntry.getTitle());
            }
            if (syndEntry.getPublishedDate() != null) {
                textView = (TextView) convertView.findViewById(R.id.synd_feed_date);
                textView.setText(syndEntry.getPublishedDate().toString());
            }
            if (syndEntry.getEnclosures() != null) {
                imageView = (ImageView) convertView.findViewById(R.id.imageView);
                try {
                    URL monurl = new URL(((SyndEnclosureImpl)syndEntry.getEnclosures().get(0)).getUrl());
                    String urlpath= monurl.toString();
                    imageView = (ImageView) convertView.findViewById(R.id.imageView);
                    Picasso.with(ctx).load(urlpath).into(imageView);
                } catch (MalformedURLException e) {
                    //e.printStackTrace();
                } catch (Exception e)
                {   //e.printStackTrace();
                    imageView.setImageDrawable(null);
                }


            }

        }

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (syndFeedFilter == null)
            syndFeedFilter = new syndFeedFilter();

        return syndFeedFilter;
    }

    public class syndFeedFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list
                results.values = origsyndFeed.getEntries();
                results.count = origsyndFeed.getEntries().size();
            }
            else {
                // We perform filtering operation
                List<SyndEntry> nSyndEntryList = new ArrayList<SyndEntry>();
                List<SyndEntry> feeds=origsyndFeed.getEntries();
                for (SyndEntry s :feeds) {
                    if (s.getTitle().toUpperCase().contains(constraint.toString().toUpperCase()))
                        nSyndEntryList.add(s);
                }

                results.values = nSyndEntryList;
                results.count = nSyndEntryList.size();

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                syndFeed=new SyndFeedImpl();
                syndFeed.setFeedType(origsyndFeed.getFeedType());
                syndFeed.setTitle(origsyndFeed.getTitle());
                syndFeed.setLink(origsyndFeed.getLink());
                syndFeed.setDescription(origsyndFeed.getDescription());
                syndFeed.setEntries((List) results.values);
                notifyDataSetChanged();
            }

        }
    }
}
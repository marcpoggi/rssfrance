package com.feeltest.rssfrance.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feeltest.rssfrance.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class DetailNewsFragment extends Fragment {
    private Context ctx;
    private TextView view_titre,view_auteur, view_description,view_date,view_link;
    private ImageView view_image;

    public DetailNewsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myfragment_layout2, container, false);
     /*   view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1));*/
        view_titre = (TextView)view.findViewById(R.id.synd_feed_title);
        view_auteur = (TextView)view.findViewById(R.id.synd_feed_author);
        view_description = (TextView)view.findViewById(R.id.synd_feed_description);
        view_date = (TextView)view.findViewById(R.id.synd_feed_date);
        view_link = (TextView)view.findViewById(R.id.synd_feed_link);
        view_image = (ImageView)view.findViewById(R.id.imageView);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        //Log.w("MyFragment2", "onAttach called");
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        ctx = getActivity();
        super.onActivityCreated(savedInstanceState);
        if (getArguments()!=null)
        {
            HashMap<String, String> data = (HashMap<String, String>) getArguments().get("data");
            updateDetail(data);
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    public void updateDetail(final HashMap<String, String> data)
    {

        view_titre.setText(data.get("titre"));
        view_date.setText(data.get("date"));
        view_description.setText(Html.fromHtml(data.get("description")));
        view_auteur.setText(data.get("createur"));
        view_link.setText((data.get("link")));
        Picasso.with(ctx).load(data.get("lienimage")).into(view_image);
    }
}

package com.feeltest.rssfrance.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.feeltest.rssfrance.R;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEnclosureImpl;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;


public class MyFragment extends Fragment {
    private Context ctx ;
    private Boolean init=true;
    private  int iurl;
    private ArrayList<HashMap<String, String>> favoris;
    private  int iliste;
    private syndFeedAdapter monadapter;


    private IMyFragmentCallBack parent; // interface qui renvoie à l'activity
    private ListView list;
    private SwipeRefreshLayout swipeLayout;
    private String url;

    public MyFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true); // hyper important cela permet de conserver les class members
        //notamment l'adapter de la listeview que l'on aura pris soin de sauver sur onstop
        if ( url==null) // ouverture de l'application ou on revient des abonnements
        {
            SharedPreferences mesprefs = ctx.getSharedPreferences("mesprefs", Context.MODE_PRIVATE);
            if (mesprefs!=null)
            { // application déjà créée, on met a jour url et
                //  on fait une lecture du cache
                iurl=mesprefs.getInt("lasturlindice",0);
                iliste = mesprefs.getInt("lastliste",1);
                init = true ;


            }
            else {// premiere ouverture de l'application
                // on force la premiere dans la liste
                url=getResources().getStringArray(R.array.weburlchoice)[0];
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Log.w("MyFragment", "onCreateView called");
        View view= inflater.inflate(R.layout.myfragment_layout, container, false);
        list = (ListView) view.findViewById(R.id.list);
        list.setAdapter(monadapter);
        // dans on createview on recupere les elements graphiques
        setHasOptionsMenu(true);


 //       view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT, 1));
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);

        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
//        btngetdata = (Button) view.findViewById(R.id.getdata);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        //Log.w("MyFragment", "onAttach called");
        //Utiliser cette méthode pour lier votre fragment avec son callback
        parent = (IMyFragmentCallBack) activity;
        ctx = getActivity();
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Log.w("MyFragment", "onActivityCreated called");
        // onrecupere le contexte et on met a jour le fragment depuis un etat anterieur

        parent.InitFragmentFavoriAndTitle();



        swipeLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                new XMLParse().execute();
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                onItemSelected(position); // methode du fragment
                // permet de sortir du listener pour prendre en compte une action vers le parent = activity
            }
        });
        if (init) { OndrawerSelection(iliste,iurl); init = false;};
         // init = false quand on revient du fragment detail
         // true quand on revient de abonnement ou à l'ouverture

    }



    @Override
    public void onStop() {
        super.onStop();
        if (list.getAdapter().getClass().getName().contains("syndFeedAdapter")) {
            monadapter = (syndFeedAdapter) list.getAdapter();
        }

        // dernière méthode appelée quand on passe au fragment suivant
        // car setretaininstance = true !!
        // ici on sauve l'adapter pour le réutiliser quand le fragment réapparait par un back (methode oncreateview)
    }

    @Override
    public void onDestroy() {
        if (list.getAdapter().getClass().getName().contains("syndFeedAdapter")) {
        monadapter = (syndFeedAdapter) list.getAdapter();
        SharedPreferences pref;
        pref = ctx.getSharedPreferences("mesprefs", ctx.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("lastliste",iliste);
        editor.putInt("lasturlindice",iurl);
            editor.commit();
        }
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
/*
        // Attention suivant les versions android onCreateOptions est appelé avant on OnActivityCreated
        // ou bien plus tard ...(4.2+)  ....
        // Aussi il faut instancier les actionview  plutot ici ( comme ca on est sur qu'ils sont créés.
        // et leurs adapters  également ici ( mais avec des ressources, ou des objets créés avant OnActivity Created
        // pour les listeners c'est une fonction de callback , donc moins de soucis
        Log.w("MyFragment", "onCreateOptionsMenu called"); */
        inflater.inflate(R.menu.menufragment, menu);
        MenuItem menusearchView = menu.findItem(R.id.search);
        SearchView masearchView = (SearchView) menusearchView.getActionView();
        masearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (list.getAdapter()!=null &&  monadapter!=null && list.getAdapter().getClass().getName().matches(monadapter.getClass().getName())) {
  // car j'utilise aussi un autre adapter quand la liste est vide pour indiquer le scroll vers le bas
                    ((syndFeedAdapter) list.getAdapter()).getFilter().filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (list.getAdapter()!=null && monadapter!=null && list.getAdapter().getClass().getName().matches(monadapter.getClass().getName())) {
                    if (!newText.isEmpty()) {

                    } else {
                        if (list.getAdapter() != null)
                            ((syndFeedAdapter) list.getAdapter()).resetData();
                    }
                }
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        //Log.w("MyFragment", "onPrepareOptionsMenu called");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
     /*   if (item.getItemId() == R.id.readxml)
        { feedService.getXml();}
*/
        return super.onOptionsItemSelected(item);
    }



    private class XMLParse extends AsyncTask<Void, Void,SyndFeed> {
        private ProgressDialog pDialog;
        private SyndFeed feed;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ctx);
            pDialog.setMessage(getResources().getString(R.string.actualisation));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected SyndFeed doInBackground(Void... params) {
            //Log.i("lieu : ", "doinB");
            IFeedService feedService;
            if (Config.isTestMode) {
                feedService = new MockFeedDataSource();
            } else {
                feedService = new FeedReadSave(ctx);
            }
            Boolean ok_read= feedService.getAllNews(url);
            SyndFeed newfeed;
            if (ok_read)
            {
                newfeed = feedService.getAllNewsfromCache(url);
            }
            else newfeed=null;
            feedService = null;
            return newfeed;
        }

        @Override
        protected void onPostExecute(SyndFeed newfeed) {
            pDialog.dismiss();
            swipeLayout.setRefreshing(false);
            if (newfeed != null) {
              //  setFeed(newfeed);
                monadapter = new syndFeedAdapter(ctx, newfeed);
                list.setAdapter(monadapter);
                SyndEntry data = (SyndEntry) newfeed.getEntries().get(0);
                //Log.i("titre",data.getTitle());
            } else Toast.makeText(ctx,getResources().getString(R.string.pasdeconnexion), Toast.LENGTH_SHORT).show();
        }
    }

    private class XMLParseFromCache extends AsyncTask<Void, Void,SyndFeed> {
            private ProgressDialog pDialog;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(ctx);
                pDialog.setMessage(getResources().getString(R.string.gettingdata));
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }

            @Override
            protected SyndFeed doInBackground(Void... params) {
                IFeedService feedService;
                if (Config.isTestMode) {
                    feedService = new MockFeedDataSource();
                } else {
                    feedService = new FeedReadSave(ctx);
                }
                //Log.i("lieu : ", "doinBCache / url :" + url);
                //publishProgress(values); si besoin d'appeler on progrees update
                SyndFeed feed = feedService.getAllNewsfromCache(url);
                feedService = null;
                return feed;
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }


            @Override
            protected void onPostExecute(SyndFeed feed) {
                pDialog.dismiss();

                if (feed!=null)
                {
                    monadapter = new syndFeedAdapter(ctx,feed);
                    list.setAdapter(monadapter);
                }
                else {
                    Toast.makeText(ctx, getResources().getString(R.string.pasdedonnees), Toast.LENGTH_SHORT).show();
                    List<Map<String,String>> blanklist = new ArrayList<Map<String,String>>(Arrays.asList(new HashMap<String,String>() {
                        {
                            put("valeur", "0");
                        }
                    }));

                    list.setAdapter(new SimpleAdapter(ctx,blanklist,R.layout.blank_list_item,new String[] {"valeur"}, new int[] {android.R.id.text1}));
                }
            }
    }

    private void onItemSelected(int position) {
        if (monadapter != null && list.getAdapter().getClass().getName().matches(monadapter.getClass().getName())) {
            // on prend les donnees a partir de l'adapter car la liste peut etre plus courte a cause du filtre
            SyndEntry data = (SyndEntry) list.getAdapter().getItem(position);
            HashMap<String, String> details = new HashMap<String, String>();
            details.put("titre", data.getTitle());
            //Log.i("titre", data.getTitle());
            details.put("date", data.getPublishedDate().toString());
            details.put("description", data.getDescription().getValue());
            details.put("link", data.getLink());
            details.put("createur", data.getAuthor());
            URL monurl = null;
            try {
                monurl = new URL(((SyndEnclosureImpl) data.getEnclosures().get(0)).getUrl());
                String urlpath = monurl.toString();
                details.put("lienimage", urlpath);
                //Log.i("lienimage : ", urlpath);

            } catch (MalformedURLException e) {
                //e.printStackTrace();
            } catch (IndexOutOfBoundsException e) {
                //e.printStackTrace();
            }

            parent.onItemSelected(details);
            // fait une action en direction du parent (activity)
        }
    }
    public void OndrawerSelection(int whatliste, int position)
    {   SetTitle(whatliste, position);
        iliste=whatliste;
        if (whatliste==1)
        {
        String[] weburl = getResources().getStringArray(R.array.weburlchoice);
        iurl = position;
        url = weburl[position];
        new XMLParseFromCache().execute();
        }
        if (whatliste==2)
        {
            iurl = position;
            url = favoris.get(position).get("link");
            new XMLParseFromCache().execute();
        }
    }
    public void SetFavoris(ArrayList<HashMap<String, String>> favoris) {
        this.favoris=favoris;
    }

    public void SetTitle(int whatliste, int position) {
        if (whatliste==1)
        {
            getActivity().getActionBar().setTitle(getResources().getStringArray(R.array.urlchoice)[position]);

        }
        if (whatliste==2)
        {
            getActivity().getActionBar().setTitle(favoris.get(position).get("source"));
        }
    }

}

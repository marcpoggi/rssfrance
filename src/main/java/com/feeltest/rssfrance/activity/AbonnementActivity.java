package com.feeltest.rssfrance.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.feeltest.rssfrance.model.Base;
import com.feeltest.rssfrance.model.Flux;
import com.feeltest.rssfrance.model.MesFavoris;
import com.feeltest.rssfrance.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class AbonnementActivity extends Activity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abonnement);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_abonnement, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "SPORT";
                case 1:
                    return "PRESSE";
                case 2:
                    return "CULTURE";
                case 3:
                    return "ECONOMIE";
                case 4:
                    return "TECHNO";
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private Context ctx;
        private ListView list;
        private WeakReference<AbonnementParse> asyncTaskWeakRef;
        private customAdapter adapter;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            return fragment;
        }

        private void setAdapter(customAdapter adapter) {
            this.adapter = adapter;
        }

        public PlaceholderFragment() {
            super();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_abonnement, container, false);
            super.onActivityCreated(savedInstanceState);

            list = (ListView) rootView.findViewById(R.id.listeflux);
            setHasOptionsMenu(true);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            ctx = getActivity();
            super.onAttach(activity);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

                AbonnementParse asyncTask = new AbonnementParse(ctx, this);
                this.asyncTaskWeakRef = new WeakReference<AbonnementParse>(asyncTask);
                asyncTask.execute(getArguments().getInt(ARG_SECTION_NUMBER));
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        Flux flux = ((customAdapter) parent.getAdapter()).getItem(position);
                        String sflux = flux.getSource();
                        Boolean result = MesFavoris.AddFavoris(ctx,
                                ((customAdapter) parent.getAdapter()).getItem(position).getSource(),
                                ((customAdapter) parent.getAdapter()).getItem(position).getAdresse());
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result",result);
                        getActivity().setResult(RESULT_OK,returnIntent);
                        getActivity().finish();


                    }
             });

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
                    ((customAdapter)list.getAdapter()).getFilter().filter(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (!newText.isEmpty()){

                    } else {
                        if (list.getAdapter()!=null)
                            ((customAdapter)list.getAdapter()).resetData();
                    }

                    return false;
                }
            });

            super.onCreateOptionsMenu(menu, inflater);

        }

        private static class AbonnementParse extends AsyncTask< Integer , Void, Base> {
            private final IFeedService feedService;
            private ProgressDialog pDialog;
            private Context ctx;
            private WeakReference<PlaceholderFragment> fragmentWeakRef; // sert de reference au fragment appelant

            // weakreference permet un couplage faible et la possibilité de garbage collecter Asynctask

            private AbonnementParse(Context ctx, PlaceholderFragment fragment) {
                super();
                this.ctx = ctx;
                feedService = new FeedReadSave(ctx);
                fragmentWeakRef = new WeakReference<PlaceholderFragment>(fragment);

            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(ctx);
                pDialog.setMessage(ctx.getResources().getString(R.string.gettingdata));
                pDialog.setIndeterminate(true);
                pDialog.setCancelable(true);
                pDialog.show();
            }

            @Override
            protected Base doInBackground(Integer... params) {
                //Log.i("lieu : ", "doinB  Base");
                int i = params[0];
                Base allflux = feedService.getXml(i);

                return allflux;
            }

            @Override
            protected void onPostExecute(Base allflux) {
                pDialog.dismiss();

                if (allflux != null) {
                    customAdapter adapter = new customAdapter(ctx, allflux.getMesflux());
                    //       fragmentWeakRef.get().setAdapter(adapter);
                    fragmentWeakRef.get().list.setAdapter(adapter);
                    //       adapter.notifyDataSetChanged();
                }
            }


        }

        static class customAdapter extends ArrayAdapter<Flux> implements Filterable {
            private List<Flux>   origflux;
            private List<Flux> flux;
            private Filter fluxFilter;


            //  private LayoutInflater inflater ; // pour le garder en cache
            public customAdapter(Context context, List<Flux> objects) {
                super(context, android.R.layout.simple_list_item_2, android.R.id.text1, objects);
                flux = objects;
                origflux = objects;
            //    this.ctx =context;
            //    this.layoutInflater = LayoutInflater.from(context);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                text1.setText(flux.get(position).getSource());
                text2.setText(flux.get(position).getAdresse());
                return view;
            }

            @Override
            public int getCount() {
                return flux != null ? flux.size() : 0;
            }

            public void resetData() {
                flux = origflux;
            }

            @Override
            public Flux getItem(int position)
            {
                return flux.get(position);
            }

            public Filter getFilter() {
                if (fluxFilter == null)
                    fluxFilter = new FluxFilter();

                return fluxFilter;
            }
            public class FluxFilter extends Filter {

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();
                    // We implement here the filter logic
                    if (constraint == null || constraint.length() == 0) {
                        // No filter implemented we return all the list
                        results.values = origflux;
                        results.count = origflux.size();
                    }
                    else {
                        // We perform filtering operation
                        List<Flux> nFluxList = new ArrayList<Flux>();

                        for (Flux f :origflux) {
                            if (f.getSource().toUpperCase().contains(constraint.toString().toUpperCase()))
                                nFluxList.add(f);
                        }

                        results.values = nFluxList;
                        results.count = nFluxList.size();

                    }
                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results.count == 0)
                        notifyDataSetInvalidated();
                    else {
                        flux = (List<Flux>)results.values;
                        notifyDataSetChanged();
                    }

                }
            }
        }
    }
}


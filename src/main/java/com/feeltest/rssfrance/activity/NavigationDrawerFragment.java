package com.feeltest.rssfrance.activity;


import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.feeltest.rssfrance.model.MesFavoris;
import com.feeltest.rssfrance.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private static final String STATE_SELECTED_LISTE="selected_navigation_drawer_selected_list";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<HashMap<String,String>> mesfavoris;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private ListView mDrawerListViewFavori;
    private View mFragmentContainerView;
    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;
    private ScrollView mView;
    private int mCurrentListe;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mCurrentListe = savedInstanceState.getInt(STATE_SELECTED_LISTE);
            mFromSavedInstanceState = true;
        }

        // problème cela appelle une mise a jour des fragments avant qu'il ne soit créé
        // Select either the default item (0) or the last selected item.
       // selectItem(mCurrentSelectedPosition);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = (ScrollView)inflater.inflate(
                R.layout.fragment_navigation_drawer, container, false);

        mDrawerListView = (ListView) mView.findViewById(R.id.listeflux);

        ImageView mDrawerAddFavori = (ImageView)mView.findViewById(R.id.addFavoriView);
        mDrawerAddFavori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.openFavorisListe();
            }
        });
        mDrawerListViewFavori = (ListView) mView.findViewById(R.id.listefavori);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentListe = 1;
                selectItem(mCurrentListe,position);
            }
        });
        mDrawerListView.setAdapter(new ArrayAdapter<String>(
                getActionBar().getThemedContext(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                getResources().getStringArray(R.array.urlchoice)));
        if (mCurrentListe == 1) mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        final Context ctx = (Context)getActivity();

        mesfavoris = MesFavoris.LoadFavoris(ctx);

        AdapterFavori adapterfavori = new AdapterFavori(ctx,R.layout.favoris_list_item, mesfavoris);
        mDrawerListViewFavori.setAdapter(adapterfavori);
        if (mCurrentListe == 2) mDrawerListViewFavori.setItemChecked(mCurrentSelectedPosition, true);
        mDrawerListViewFavori.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentListe = 2;
                selectItem(mCurrentListe, position);
            }
        });

        return mView; // bien retourner mView et pas le mDrawer comme dans la version de base
        //  sinon pb de hierarchie parent enfant des vues
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */


    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);


        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                mCallbacks.ActionBarSetTitle(mCurrentListe, mCurrentSelectedPosition);
                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    public void OpenDrawer()
    {
        mDrawerLayout.openDrawer(mFragmentContainerView);
    }

    public void selectItem(int liste, int position) {
        mCurrentSelectedPosition = position;
        mCurrentListe = liste;
        if (mDrawerListView != null && liste==1) {
            mDrawerListView.setItemChecked(position, true);
            if (mDrawerListViewFavori != null)
                mDrawerListViewFavori.clearChoices();

        }
        if (mDrawerListViewFavori != null && liste==2) {
            mDrawerListViewFavori.setItemChecked(position, true);
            if (mDrawerListView != null)
                mDrawerListView.clearChoices();

        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(liste,position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
        outState.putInt(STATE_SELECTED_LISTE, mCurrentListe);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.main, menu);
            showGlobalContextActionBar();
        }
        if (mDrawerLayout != null && !isDrawerOpen()) {
            // rajouté anciennement mais déplace sur le ondrawerClosed... plus logique
   //     getActivity().getActionBar().setTitle(getResources().getStringArray(R.array.urlchoice)[mCurrentSelectedPosition]);
        // force à remettre le choix en titre apres fermeture du drawer sans selection
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

     /*   if (item.getItemId() == R.id.action_example) {
            Toast.makeText(getActivity(), "Example action.", Toast.LENGTH_SHORT).show();
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
    }
// permet d'actualiser le drawer une fois que le fragment principale est chargé
//
    public void InitAfterMainFragmentCreated()
    { // a jour les favoris, et commande la mise a jour de l'actionbar et initialise le check sur uneliste
        mCallbacks.setFavorisUrl(mesfavoris);
        mCallbacks.ActionBarSetTitle(mCurrentListe,mCurrentSelectedPosition);
    }

// permet de forcer une réactualisation de la liste des favoris apres ajout

    public void InitFavorisAfterAdd()
    {mesfavoris = MesFavoris.LoadFavoris(getActivity());
 //    ((ArrayAdapter<HashMap<String, String>>)mDrawerListViewFavori.getAdapter()).notifyDataSetChanged();
        // avant non car il faut utiliser réactualiser mes favoris  au sein de l'adapter ( variable d'instance )
        ((AdapterFavori)mDrawerListViewFavori.getAdapter()).refresh(mesfavoris);
    }

    private ActionBar getActionBar() {
        return getActivity().getActionBar();
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int whatliste, int position);
        void setFavorisUrl(ArrayList<HashMap<String, String>> favoris);
        void ActionBarSetTitle(int whatliste , int position);
        void openFavorisListe();
    }

    class AdapterFavori extends ArrayAdapter<HashMap<String, String>>
    {
        private ArrayList<HashMap<String, String>> lesfavoris;
        private   LayoutInflater inflater;
        private Context ctx;
        public AdapterFavori(Context ctxClass,int textViewResourceId, ArrayList<HashMap<String, String>> lesfavoris)
            {
               super(ctxClass,textViewResourceId, lesfavoris);
                this.lesfavoris=lesfavoris;
                this.inflater = (LayoutInflater) ctxClass.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                this.ctx = ctxClass;
            }

        public void refresh(ArrayList<HashMap<String, String>> items)
        { // cette methode est obligatoire car  comme le customadapter a sa propre instance des données
          // lesfavoris , un notifydatasetchanged sur un field  mesfavoris n'a pas d'influence
          // du coup cette methode permet un appel depuis l'exterieur en reactualisant directement lesfavoris
            this.lesfavoris = items;
            notifyDataSetChanged();
        }
        @Override
            public View getView(int position, View convertView,  final ViewGroup parent) {
                //Log.i("Mes favoris size dans getView : ", String.valueOf(lesfavoris.size()));
                //    View view = super.getView(position, convertView, parent);
                View view = convertView;
                if (view == null) {
                view = inflater.inflate(R.layout.favoris_list_item, parent, false);
                }
                TextView text1 = (TextView) view.findViewById(R.id.favoris_title);
                ImageView imageview = (ImageView) view.findViewById(R.id.imageView);
                imageview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer position = (Integer) v.getTag();
                        lesfavoris = MesFavoris.DeleteFavoris(ctx, position);
                        notifyDataSetChanged();
                        if (mCurrentListe==2 && mCurrentSelectedPosition==position)
                        {   mCurrentListe = 1;
                            mCurrentSelectedPosition = 0;
                            mCallbacks.onNavigationDrawerItemSelected(mCurrentListe,mCurrentSelectedPosition);
                            mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
                        }
                       // callAdapterchange();

                    }
                });
                imageview.setFocusable(false);
                imageview.setTag(position);

                HashMap<String, String> unflux = lesfavoris.get(position);
                text1.setText(unflux.get("source"));

                // imageview.s
                return view;
            }

            @Override
            public int getCount() {
//                Log.i("Mes favoris size dans getCount : ", String.valueOf(lesfavoris.size()));
                return lesfavoris==null ? 0 : lesfavoris.size();
            }
        }
}


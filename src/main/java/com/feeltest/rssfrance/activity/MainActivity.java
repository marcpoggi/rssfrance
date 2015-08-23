package com.feeltest.rssfrance.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import com.feeltest.rssfrance.R;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, IMyFragmentCallBack {
// le chemin implements Navigation.... est complet car l'interface est incluse dans la classe
    boolean twoPane;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;


    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        twoPane = getResources().getBoolean(R.bool.twoPane);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction;
            MyFragment fragment = (MyFragment)fragmentManager.findFragmentByTag("frag1");
            if ( fragment == null) {
                fragment = new MyFragment();
            }

            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.item_list_container, fragment, "frag1");
            fragmentTransaction.commit();

        }
            // Set up the drawer.... placé hors du if pour que ce soit systématique sinon

            mNavigationDrawerFragment.setUp(
                    R.id.navigation_drawer,
                    (DrawerLayout) findViewById(R.id.drawer_layout));

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    protected void onStart() {
        super.onStart();


    }

// Ci dessous les 3 méthodes de l'interface NavigationDrawerFragment.NavigationDrawerCallbacks
    @Override
    public void onNavigationDrawerItemSelected(int whatliste , int position) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            MyFragment fragment = (MyFragment)fragmentManager.findFragmentByTag("frag1");
            fragment.OndrawerSelection(whatliste,position);
    }

    @Override
    public void setFavorisUrl(ArrayList<HashMap<String, String>> favoris) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        MyFragment fragment = (MyFragment)fragmentManager.findFragmentByTag("frag1");
        fragment.SetFavoris(favoris);
    }

    @Override
    public void ActionBarSetTitle(int whatliste, int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        MyFragment fragment = (MyFragment)fragmentManager.findFragmentByTag("frag1");
        fragment.SetTitle(whatliste, position);
    }

    @Override
    public void openFavorisListe() {
        Intent abonnement = new Intent(this, AbonnementActivity.class);
        startActivityForResult(abonnement, 1);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    this);

            // set title
            alertDialogBuilder.setTitle(R.string.app_name);

            // set dialog message
            alertDialogBuilder
                    .setMessage(R.string.info)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return true;
        }
        if (id == R.id.readxml) {
            openFavorisListe();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    // ci dessous les 2 implémentations des callback du fragment principal
    @Override
    public void InitFragmentFavoriAndTitle() {
        mNavigationDrawerFragment.InitAfterMainFragmentCreated();
    }

    @Override
    public void onItemSelected(HashMap<String, String> data) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            DetailNewsFragment fragment2;
             fragment2 = (DetailNewsFragment) fragmentManager.findFragmentByTag("frag2");

            if (twoPane == false) {
                fragment2 = new DetailNewsFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                Bundle mdata= new Bundle();
                mdata.putSerializable("data",data);
                fragment2.setArguments(mdata);
                fragmentTransaction.replace(R.id.item_list_container, fragment2, "frag2");
                fragmentTransaction.addToBackStack("frag1");
                fragmentTransaction.commit();

            }
        else {
                if (fragment2 == null) {
                    fragment2 = new DetailNewsFragment();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    Bundle mdata = new Bundle();
                    mdata.putSerializable("data", data);
                    fragment2.setArguments(mdata);
                    fragmentTransaction.add(R.id.item_detail_container, fragment2, "frag2");
                    fragmentTransaction.commit();
                } else
                    fragment2.updateDetail(data);
            }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                if (mNavigationDrawerFragment!=null) {
                    mNavigationDrawerFragment.InitFavorisAfterAdd(); // met a jour les favoris dans le drawer
                    mNavigationDrawerFragment.InitAfterMainFragmentCreated(); // met a jour les favoris dans fragment ( via le drawer )
                    mNavigationDrawerFragment.OpenDrawer();

                }
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }


}

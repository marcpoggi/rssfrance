package com.feeltest.rssfrance.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.feeltest.rssfrance.R;

import java.util.HashMap;

/**
 * Created by mpoggi on 29/10/2014.
 */
public class ItemDetailActivity extends FragmentActivity {

    private DetailNewsFragment fragment2;
    boolean twoPane;
    Bundle extra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extra=getIntent().getExtras();
        setContentView(R.layout.detail_activity_one_pane);
        twoPane = false;
        fragment2 = (DetailNewsFragment)
                    getSupportFragmentManager().findFragmentById(R.id.myfragment2);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (extra != null) {

            HashMap<String, String> data = (HashMap<String, String>) extra.getSerializable("datadetail");
            fragment2.updateDetail(data);
            // and get whatever type user account id is
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

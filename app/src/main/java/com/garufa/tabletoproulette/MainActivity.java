package com.garufa.tabletoproulette;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends ActionBarActivity {

    private DrawerLayout drawer_layout;
    private ListView drawer_list;
    private ActionBarDrawerToggle drawer_toggle;

    private CharSequence drawer_title;
    private CharSequence title;
    private String[] menu_selections;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = drawer_title = getTitle();
        menu_selections = getResources().getStringArray(R.array.menu_array);
        drawer_layout   = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer_list     = (ListView) findViewById(R.id.left_drawer);

        drawer_list.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, menu_selections));


//        setupDrawer();
    }

    private void setupDrawer(){
        menu_selections = getResources().getStringArray(R.array.menu_array);
        drawer_layout   = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer_list     = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        drawer_list.setAdapter(new ArrayAdapter<String>(
                this,
                R.layout.drawer_list_item,
                menu_selections
        ));

        // Set the list's click listener
//        drawer_list.setOnItemClickListener(new DrawerItemClickListener());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}

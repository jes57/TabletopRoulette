package com.garufa.tabletoproulette;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Jason on 2/17/2015.
 */
public class CollectionListView extends Activity {

    private DrawerLayout drawer_layout;
    private ListView drawer_list;
    private ActionBarDrawerToggle drawer_toggle;

    private CharSequence drawer_title;
    private CharSequence title;
    private String[] menu_selections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_layout);

        // Set up the Slideout Drawer
        setupDrawer();



        // Get the array for the listView
        Resources res = getResources();
        String[] gamesArray = res.getStringArray(R.array.games_array);

        // Assign the array to the listView
        final ListAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.adapter_layout, gamesArray);

        // Assign ArrayList
        ArrayList<String> gamesArrayList = new ArrayList<String>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>( this,
                R.layout.adapter_layout, R.id.textViewAdapter, gamesArrayList);

        for (int i = 0; i < gamesArray.length; i++){
            gamesArrayList.add(gamesArray[i]);
        }

        // Set the ListView
        ListView memoryTestListView = (ListView) findViewById(R.id.collectionListView);
        memoryTestListView.setAdapter(arrayAdapter);


        // Set the onClick event
        memoryTestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String gamePicked = "You selected " +
                        String.valueOf(parent.getItemAtPosition(position));

                Toast.makeText(CollectionListView.this, gamePicked, Toast.LENGTH_SHORT).show();

//                Intent gameIntent = new Intent(CollectionListView.this, GameInfo.class);
                Intent gameIntent = new Intent(CollectionListView.this, MainActivity.class);

//                gameIntent.putExtra("game", position);
                startActivity(gameIntent);
//                if (position == 0){
//                    Intent test1_intent = new Intent(MainActivity.this, QuizImage1.class);
//                    startActivity(test1_intent);
//                }
            }
        });
    }

    private void setupDrawer(){
        title = drawer_title = getTitle();
        menu_selections = getResources().getStringArray(R.array.menu_array);
        drawer_layout   = (DrawerLayout) findViewById(R.id.collection_drawer_layout);
        drawer_list     = (ListView) findViewById(R.id.collection_left_drawer);

        drawer_list.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, menu_selections));

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
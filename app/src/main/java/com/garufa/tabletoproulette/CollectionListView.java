package com.garufa.tabletoproulette;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
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
public class CollectionListView extends ActionBarActivity {
    public final static int COLLECTION_SCREEN = 0, GAME_PICKER = 1, HELP = 2, SOURCES = 3,
            ABOUT = 4;

    private DrawerLayout drawer_layout;
    private ListView drawer_list, memoryTestListView;
    private ActionBarDrawerToggle drawer_toggle;

    private CharSequence drawer_title;
    private CharSequence title;
    private String[] menu_selections, gamesArray;

    private ListAdapter adapter;
    private ArrayList<String> gamesArrayList;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_layout);

        setupDrawer();
        setupListView();
    }

    private void setupListView() {
        // Get the array for the listView
        Resources res = getResources();
        gamesArray = res.getStringArray(R.array.games_array);

        // Assign the array to the listView
        adapter = new ArrayAdapter<String>(this,
                R.layout.adapter_layout, gamesArray);

        // Assign ArrayList
        gamesArrayList = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>( this,
                R.layout.adapter_layout, R.id.textViewAdapter, gamesArrayList);

        for (int i = 0; i < gamesArray.length; i++){
            gamesArrayList.add(gamesArray[i]);
        }

        // Set the ListView
        memoryTestListView = (ListView) findViewById(R.id.collectionListView);
        memoryTestListView.setAdapter(arrayAdapter);


        // Set the onClick event
        memoryTestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String gamePicked = "You selected " +
                        String.valueOf(parent.getItemAtPosition(position));

                Toast.makeText(CollectionListView.this, gamePicked, Toast.LENGTH_SHORT).show();

                Intent gameIntent = new Intent(CollectionListView.this, GameInfo.class);

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
        drawer_toggle   = new ActionBarDrawerToggle(this, drawer_layout,
                R.string.drawer_open, R.string.drawer_close){

            public void onDrawerClosed(View view){
                super.onDrawerClosed(view);
                getActionBar().setTitle(title);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(drawer_title);
                invalidateOptionsMenu();
            }
        };

        drawer_list     = (ListView) findViewById(R.id.collection_left_drawer);

        drawer_list.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, menu_selections));

        // Set the list's click listener
        drawer_list.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
//        getActionBar().setDisplayHomeAsUpEnabled(true);
//        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the proper interactions
        // between the sliding drawer and the action bar app icon

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent gameIntent;

            String gamePicked = "You selected " +
                    String.valueOf(parent.getItemAtPosition(position));

            Toast.makeText(CollectionListView.this, gamePicked, Toast.LENGTH_SHORT).show();
            switch (position){
                case COLLECTION_SCREEN:
                    gameIntent = new Intent(CollectionListView.this, CollectionListView.class);
                    drawer_layout.closeDrawer(drawer_list);
                    break;
                case GAME_PICKER:
                    gameIntent = new Intent(CollectionListView.this, CollectionListView.class);
                    drawer_layout.closeDrawer(drawer_list);
                    break;
                case HELP:
                    gameIntent = new Intent(CollectionListView.this, CollectionListView.class);
                    drawer_layout.closeDrawer(drawer_list);
                    break;
                case SOURCES:
                    gameIntent = new Intent(CollectionListView.this, CollectionListView.class);
                    drawer_layout.closeDrawer(drawer_list);
                    break;
                case ABOUT:
                    gameIntent = new Intent(CollectionListView.this, CollectionListView.class);
                    drawer_layout.closeDrawer(drawer_list);
                    break;
                default:
                    gameIntent = new Intent(CollectionListView.this, CollectionListView.class);
                    drawer_layout.closeDrawer(drawer_list);
                    break;
            }

//                gameIntent.putExtra("game", position);
            startActivity(gameIntent);
            CollectionListView.this.finish();
        }
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
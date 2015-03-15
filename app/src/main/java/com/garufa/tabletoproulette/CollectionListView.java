package com.garufa.tabletoproulette;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Jason on 2/17/2015.
 */
public class CollectionListView extends ActionBarActivity {

    private Intent intent;

    String  GAME_ID = "148228",
            GAME_NAME = "Splendor",
            name, game_id;

    String[] gamesArray;
    ListView collectionListView;
    ArrayList<String> gamesArrayList;
    ArrayList<Game> gameObjectsArrayList;
    DBHandler dbHandler;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_layout);

        initialize();
    }

    private void initialize() {
        dbHandler = new DBHandler(this, null, null, 1);
        cursor = dbHandler.getAllGames();

        // Set the ListView
        collectionListView = (ListView) findViewById(R.id.collectionListView);
        GameCursorAdapter cursorAdapter = new GameCursorAdapter(this, cursor);
        collectionListView.setAdapter(cursorAdapter);


//        // Get the array for the listView
//        Resources res = getResources();
//        gamesArray = res.getStringArray(R.array.games_array);
//
//        // Assign the array to the listView
//        final ListAdapter adapter = new ArrayAdapter<String>(this,
//                R.layout.adapter_layout, gamesArray);
//
//        // Assign ArrayList
//        gamesArrayList = new ArrayList<String>();
//        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>( this,
//                R.layout.adapter_layout, R.id.textViewAdapter, gamesArrayList);
//
//        for (int i = 0; i < gamesArray.length; i++){
//            gamesArrayList.add(gamesArray[i]);
//        }
//
//        // Set the ListView
//        collectionListView = (ListView) findViewById(R.id.collectionListView);
//        collectionListView.setAdapter(arrayAdapter);
//
//
        // Set the onClick event
        collectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (cursor.moveToPosition(position)) {
                    name = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_GAME_NAME));
                    game_id = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_BGG_ID));
                }
                String gamePicked = "You selected " + name;

                Toast.makeText(CollectionListView.this, gamePicked, Toast.LENGTH_SHORT).show();

                Intent gameIntent = new Intent(CollectionListView.this, GameInfo.class);

                gameIntent.putExtra(Constants.EXTRAS_ID, GAME_ID);
                gameIntent.putExtra(Constants.EXTRAS_NAME, name);
                startActivity(gameIntent);
            }
        });
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
//        if (id == R.id.action_settings) {
//            return true;
//        }

        switch (id){
            case R.id.action_settings: return true;
            case R.id.action_collection:
                intent = new Intent(CollectionListView.this, CollectionListView.class);
                startActivity(intent); break;
            case R.id.action_search:
                intent = new Intent(CollectionListView.this, SearchListView.class);
                startActivity(intent); break;
            case R.id.action_addGame:
                intent = new Intent(CollectionListView.this, AddGame.class);
                startActivity(intent); break;
            case R.id.action_mainActivity:
                intent = new Intent(CollectionListView.this, MainActivity.class);
                startActivity(intent); break;

        }

        return super.onOptionsItemSelected(item);
    }
}
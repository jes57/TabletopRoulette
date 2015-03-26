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
 * Created by Jason on 3/26/2015.
 */
public class QueryListview extends ActionBarActivity {
    private final static String TAG = "QueryListView...";

    private Intent intent, intent_extras;

    String  name, bgg_id, game_id/*, players, time, rating, mechanic*/;

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
//        cursor = dbHandler.getAllGames();

        // Get the extras
        intent_extras = getIntent();
        Bundle bundle = intent_extras.getExtras();
        if (bundle != null) {
            String players = bundle.getString(Constants.EXTRAS_PLAYERS);
            String time    = bundle.getString(Constants.EXTRAS_TIME);
            String rating  = bundle.getString(Constants.EXTRAS_RATING);
            String mechanic= bundle.getString(Constants.EXTRAS_MECHANIC);
            cursor = dbHandler.getGames(players, time, rating, mechanic);
        }
        // Set the ListView
        collectionListView = (ListView) findViewById(R.id.collectionListView);
        GameCursorAdapter cursorAdapter = new GameCursorAdapter(this, cursor);
        collectionListView.setAdapter(cursorAdapter);
        // Set the onClick event
        collectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (cursor.moveToPosition(position)) {
                    name = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_GAME_NAME));
                    bgg_id = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_BGG_ID));
                    game_id = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_ID));
                }
                String gamePicked = "You selected " + name;

                Toast.makeText(QueryListview.this, gamePicked, Toast.LENGTH_SHORT).show();

                Intent gameIntent = new Intent(QueryListview.this, GameInfo.class);

                gameIntent.putExtra(Constants.EXTRAS_BGG_ID, bgg_id);
                gameIntent.putExtra(Constants.EXTRAS_ID, game_id);
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
                intent = new Intent(QueryListview.this, CollectionListView.class);
                startActivity(intent); break;
            case R.id.action_new_game:
                intent = new Intent(QueryListview.this, SearchListView.class);
                startActivity(intent); break;
            case R.id.action_query:
                intent = new Intent(QueryListview.this, QueryGames.class);
                startActivity(intent); break;
        }

        return super.onOptionsItemSelected(item);
    }
}

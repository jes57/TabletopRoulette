package com.garufa.tabletoproulette;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Jason on 2/17/2015.
 */
public class CollectionListView extends ActionBarActivity {
    private final static String TAG = "CollectionListView...";

    private Intent intent, intent_extras;

    String  GAME_ID = "148228",
            GAME_NAME = "Splendor",
            name, bgg_id, game_id, game_to_delete;

    String players, time, rating, mechanic;

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

        if (savedInstanceState != null) {
            players = savedInstanceState.getString(Constants.EXTRAS_PLAYERS);
            time = savedInstanceState.getString(Constants.EXTRAS_TIME);
            rating = savedInstanceState.getString(Constants.EXTRAS_RATING);
            mechanic = savedInstanceState.getString(Constants.EXTRAS_MECHANIC);
        } else {
            players = rating = time = mechanic = "";
        }

        initialize();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the users filter
        outState.putString(Constants.EXTRAS_PLAYERS, players);
        outState.putString(Constants.EXTRAS_TIME, time);
        outState.putString(Constants.EXTRAS_RATING, rating);
        outState.putString(Constants.EXTRAS_MECHANIC, mechanic);

        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onResume() {
        super.onResume();
        setCollectionListView(players, time, rating, mechanic);
    }

    private void setCollectionListView(String players, String time, String rating, String mechanic) {
        dbHandler = new DBHandler(this, null, null, 1);
        if (players.equals("") && time.equals("") && rating.equals("") && mechanic.equals("")){
            cursor = dbHandler.getAllGames();
        } else {
            cursor = dbHandler.getGames(players, time, rating, mechanic);
        }

        // Set the ListView
        collectionListView = (ListView) findViewById(R.id.collectionListView);
        GameCursorAdapter cursorAdapter = new GameCursorAdapter(this, cursor);
        collectionListView.setAdapter(cursorAdapter);
    }

    private void initialize() {
        setCollectionListView(players, time, rating, mechanic);
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

                Toast.makeText(CollectionListView.this, gamePicked, Toast.LENGTH_SHORT).show();

                Intent gameIntent = new Intent(CollectionListView.this, GameInfo.class);

                gameIntent.putExtra(Constants.EXTRAS_BGG_ID, bgg_id);
                gameIntent.putExtra(Constants.EXTRAS_ID, game_id);
                gameIntent.putExtra(Constants.EXTRAS_NAME, name);
                startActivity(gameIntent);
            }
        });
        collectionListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return onLongListItemClick(view, position, id);
            }
        });
    }

    // Display the alert dialog to filter
    private void displayFilterDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(CollectionListView.this);
        View promptView = layoutInflater.inflate(R.layout.filter_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(CollectionListView.this);
        builder.setView(promptView);
        builder.setTitle("Filter");
        builder.setIcon(R.drawable.ic_action_filter);
        final EditText playersEditText = (EditText) promptView.findViewById(R.id.filter_players_EditText);
        final EditText timeEditText = (EditText) promptView.findViewById(R.id.filter_time_EditText);
        final EditText ratingEditText = (EditText) promptView.findViewById(R.id.filter_rating_EditText);
        builder.setCancelable(false).setPositiveButton("Filter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                players = playersEditText.getText().toString();
                time = timeEditText.getText().toString();
                rating = ratingEditText.getText().toString();
                setCollectionListView(players, time, rating, mechanic);
            }
        }).setNegativeButton("Clear Filter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                players = time = rating = mechanic = "";
                setCollectionListView(players, time, rating, mechanic);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean onLongListItemClick(View view, int position, long id) {
        if (cursor.moveToPosition(position)) {
            game_to_delete = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_GAME_NAME));
        } else {
            return false;
        }

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CollectionListView.this)
                .setTitle("Delete")
                .setMessage("Delete " + game_to_delete + "?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbHandler.deleteGame(game_to_delete);
                setCollectionListView(players, time, rating, mechanic);
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create();
        alertDialog.show();

        return true;
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
                startActivity(intent); finish(); break;
            case R.id.action_new_game:
                intent = new Intent(CollectionListView.this, SearchListView.class);
                startActivity(intent); break;
            case R.id.action_filter:
                displayFilterDialog(); break;
//                intent = new Intent(CollectionListView.this, QueryGames.class);
//                startActivity(intent); break;
            case R.id.action_random_game:
                displayFilterDialog(); break;
        }

        return super.onOptionsItemSelected(item);
    }


}
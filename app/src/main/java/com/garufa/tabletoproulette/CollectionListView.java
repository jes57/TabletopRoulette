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
public class CollectionListView extends BaseActivity {
    private final static String TAG = "CollectionListView...";

    private Intent intent, intent_extras;

    String  name, bgg_id, game_id, game_to_delete;

    String players, time, rating, mechanic;

    ListView collectionListView;
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
        dbHandler = new DBHandler(this, null, null, DBHandler.DATABASE_VERSION);
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
        intent_extras = getIntent();
        Bundle bundle = intent_extras.getExtras();
        if (bundle != null) {
            players = bundle.getString(Constants.EXTRAS_PLAYERS);
            time = bundle.getString(Constants.EXTRAS_TIME);
            rating = bundle.getString(Constants.EXTRAS_RATING);
            mechanic = bundle.getString(Constants.EXTRAS_MECHANIC);
        } else {
            players = time = rating = mechanic = "";
        }
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
//    @Override
//    protected void displayFilterDialog() {
//        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
//        View promptView = layoutInflater.inflate(R.layout.filter_dialog, null);
//        final EditText playersEditText = (EditText) promptView.findViewById(R.id.filter_players_EditText);
//        final EditText timeEditText = (EditText) promptView.findViewById(R.id.filter_time_EditText);
//        final EditText ratingEditText = (EditText) promptView.findViewById(R.id.filter_rating_EditText);
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setView(promptView)
//                .setTitle("Filter")
//                .setIcon(R.drawable.ic_action_filter)
//                .setCancelable(false).setPositiveButton("Filter", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Intent intent = new Intent(getApplicationContext(), CollectionListView.class);
//                intent.putExtra(Constants.EXTRAS_PLAYERS, playersEditText.getText().toString());
//                intent.putExtra(Constants.EXTRAS_TIME, timeEditText.getText().toString());
//                intent.putExtra(Constants.EXTRAS_RATING, ratingEditText.getText().toString());
//                intent.putExtra(Constants.EXTRAS_MECHANIC, "");
//
//                startActivity(intent);
//            }
//        }).setNegativeButton("Clear Filter", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                startActivity(new Intent(getApplicationContext(), CollectionListView.class));
//            }
//        }).create().show();
//    }
//    // Display the alert dialog to filter
//    protected void displayRandomDialog() {
//        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
//        View promptView = layoutInflater.inflate(R.layout.filter_dialog, null);
//        final EditText playersEditText = (EditText) promptView.findViewById(R.id.filter_players_EditText);
//        final EditText timeEditText = (EditText) promptView.findViewById(R.id.filter_time_EditText);
//        final EditText ratingEditText = (EditText) promptView.findViewById(R.id.filter_rating_EditText);
//        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
//        builder.setView(promptView)
//                .setTitle("Random")
//                .setIcon(R.drawable.ic_action_filter)
//                .setCancelable(false).setPositiveButton("Roll", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Intent intent = new Intent(getApplicationContext(), QueryRandom.class);
//                intent.putExtra(Constants.EXTRAS_PLAYERS, playersEditText.getText().toString());
//                intent.putExtra(Constants.EXTRAS_TIME, timeEditText.getText().toString());
//                intent.putExtra(Constants.EXTRAS_RATING, ratingEditText.getText().toString());
//                intent.putExtra(Constants.EXTRAS_MECHANIC, "");
//
//                startActivity(intent);
//            }
//        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        }).create().show();
//    }


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
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.removeItem(R.id.action_collection);
        return super.onPrepareOptionsMenu(menu);
    }
}
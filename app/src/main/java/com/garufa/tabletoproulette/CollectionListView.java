package com.garufa.tabletoproulette;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by Jason on 2/17/2015.
 */
public class CollectionListView extends ActionBarActivity {
    private final static String TAG = "CollectionListView...";

    private Intent intent;

    String  GAME_ID = "148228",
            GAME_NAME = "Splendor",
            name, game_id, game_to_delete;

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

    @Override
    protected void onResume() {
        super.onResume();
        setCollectionListView();
    }

    private void setCollectionListView() {
        dbHandler = new DBHandler(this, null, null, 1);
        cursor = dbHandler.getAllGames();

        // Set the ListView
        collectionListView = (ListView) findViewById(R.id.collectionListView);
        GameCursorAdapter cursorAdapter = new GameCursorAdapter(this, cursor);
        collectionListView.setAdapter(cursorAdapter);
    }

    private void initialize() {
        setCollectionListView();
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

                Intent gameIntent = new Intent(CollectionListView.this, SearchDetails.class);

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
                setCollectionListView();
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

//    @Override
//    public boolean onMenuOpened(int featureId, Menu menu) {
//        if(featureId == Window.FEATURE_ACTION_BAR && menu != null){
//            if(menu.getClass().getSimpleName().equals("MenuBuilder")){
//                try{
//                    Method m = menu.getClass().getDeclaredMethod(
//                            "setOptionalIconsVisible", Boolean.TYPE);
//                    m.setAccessible(true);
//                    m.invoke(menu, true);
//                }
//                catch(NoSuchMethodException e){
//                    Log.e(TAG, "onMenuOpened", e);
//                }
//                catch(Exception e){
//                    throw new RuntimeException(e);
//                }
//            }
//        }
//        return super.onMenuOpened(featureId, menu);
//    }
}
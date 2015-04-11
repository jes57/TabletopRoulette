package com.garufa.tabletoproulette;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
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
        registerForContextMenu(collectionListView);
        // Set the onClick event
        collectionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (cursor.moveToPosition(position)) {
                    name = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_GAME_NAME));
//            bgg_id = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_BGG_ID));
//            game_id = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_ID));
                }
                goToGameInfo(name);
            }
        });
//        collectionListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
//
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                return onLongListItemClick(view, position, id);
//            }
//        });
    }

    private void goToGameInfo(String game_name) {
        String gamePicked = "You selected " + game_name;

        Toast.makeText(CollectionListView.this, gamePicked, Toast.LENGTH_SHORT).show();

        Intent gameIntent = new Intent(CollectionListView.this, GameInfo.class);
        gameIntent.putExtra(Constants.EXTRAS_NAME, game_name);
        startActivity(gameIntent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.collectionListView) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderIcon(R.drawable.tabletoproulet);
            if (cursor.moveToPosition(info.position)) {
                menu.setHeaderTitle(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_GAME_NAME)));
            }
            String[] menuItems = getResources().getStringArray(R.array.context_menu);
            for ( int i = 0; i < menuItems.length; i++){
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final int VIEW = 0, DELETE = 1, IMAGE = 2;
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String[] menuItems = getResources().getStringArray(R.array.context_menu);
        String actionSelected = menuItems[menuItemIndex];
        String gameSelected_name = cursor.getString(
                cursor.getColumnIndexOrThrow(Constants.COLUMN_GAME_NAME));
        String gameSelected_url = cursor.getString(
                cursor.getColumnIndexOrThrow(Constants.COLUMN_IMAGE_URL));
        String gameSelected_bggId = cursor.getString(
                cursor.getColumnIndexOrThrow(Constants.COLUMN_BGG_ID));


        switch (menuItemIndex) {
            case VIEW: goToGameInfo(gameSelected_name); break;
            case DELETE: delete_game(gameSelected_name); break;
            case IMAGE: downloadImage(gameSelected_url, gameSelected_bggId); break;
        }
        return true;
    }

    /**
     * Save the image given, locally according to the bgg_id given
     * @param image
     * @param bgg_id
     * @return
     */
    private boolean saveImageInternal(Bitmap image, String bgg_id) {
        String file_name = bgg_id + Constants.FILE_TYPE;
        try {
            // Compress the image to write to OutputStream
            FileOutputStream outputStream = openFileOutput(file_name, Context.MODE_PRIVATE);

            // Write the bitmap to the output stream
            image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();

            return true;
        } catch (Exception e) {
            Log.e("saveImageInteral()", e.getMessage());
            return false;
        }
    }


    /**
     * Download the image from the url stored on the game.
     * @param url
     * @param bgg_id
     */
    private void downloadImage(String url, final String bgg_id) {
        showProgress("Downloading image...");
        new ImageLoadTask(url, new AsyncResponse() {
            @Override
            public void processFinish(Bitmap output) {
                Bitmap image = output;
                saveImageInternal(image, bgg_id);
                setCollectionListView(players, time, rating, mechanic);
                dismissProgress();
            }
        }).execute();
    }

    // Remove the game from the database and the ListView
    private void delete_game(String game_name) {
        dbHandler.deleteGame(game_name);
        showToast(String.format("%s deleted.", game_name));
        setCollectionListView(players, time, rating, mechanic);
    }

    //    private boolean onLongListItemClick(View view, int position, long id) {
//        if (cursor.moveToPosition(position)) {
//            game_to_delete = cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_GAME_NAME));
//        } else {
//            return false;
//        }
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CollectionListView.this)
//                .setTitle("Delete")
//                .setMessage("Delete " + game_to_delete + "?");
//        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dbHandler.deleteGame(game_to_delete);
//                setCollectionListView(players, time, rating, mechanic);
//            }
//        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        }).create();
//        alertDialog.show();
//
//        return true;
//    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.removeItem(R.id.action_collection);
        return super.onPrepareOptionsMenu(menu);
    }
}
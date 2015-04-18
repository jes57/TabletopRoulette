package com.garufa.tabletoproulette;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Jason on 4/13/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private Context mContext;
    private static DatabaseHelper mInstance = null;
    private SQLiteDatabase mDb;
    private static String TAG = "DatabaseHelper";

    public static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "collectionDB.db";
    private static final String TABLE_GAMES   = "games";
    private static final String SPACE = " ";
    private static final String AND = " and ";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_GAME_NAME = "game_name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_MIN_PLAYERS = "min_players";
    public static final String COLUMN_MAX_PLAYERS = "max_players";
    public static final String COLUMN_MIN_PLAY_TIME = "min_play_time";
    public static final String COLUMN_MAX_PLAY_TIME = "max_play_time";
    public static final String COLUMN_IMAGE_URL     = "image_url";
    public static final String COLUMN_GAME_MECHANIC = "game_mechanic";
    public static final String COLUMN_BGG_ID = "bgg_id";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_YEAR = "year";


    public static final String CREATE_GAMES_TABLE = "CREATE TABLE " + TABLE_GAMES + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_GAME_NAME + " TEXT UNIQUE,"
            + COLUMN_BGG_ID + " INTEGER UNIQUE,"
            + COLUMN_MIN_PLAYERS + " INTEGER,"
            + COLUMN_MAX_PLAYERS + " INTEGER,"
            + COLUMN_MIN_PLAY_TIME + " INTEGER,"
            + COLUMN_MAX_PLAY_TIME + " INTEGER,"
            + COLUMN_DESCRIPTION + " TEXT,"
            + COLUMN_GAME_MECHANIC + " TEXT,"
            + COLUMN_RATING + " DECIMAL(3,2),"
            + COLUMN_YEAR + " INTEGER,"
            + COLUMN_IMAGE_URL + " TEXT" + ")";


    public static DatabaseHelper getInstance(Context context){
        // Use the application context, which will ensure that you don't accidentally
        // leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    private DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // I'm not sure but this will probably give me issues because the context
        // might not be current
        mContext = context;
    }

    public DatabaseHelper(Context context, String name,
                          SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        try {
            database.execSQL(CREATE_GAMES_TABLE);
        } catch (SQLiteException e) {
            Log.e(TAG, "Error occurred: " + e);
        }
    }

    public void closeDB(){
        if(mDb != null) {
            mDb.close();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        try {
            database.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMES);
            onCreate(database);
        } catch (SQLiteException exception) {
            Log.e(TAG, "Error occurred: " + exception);
        }
    }

//    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
//        super(context, name, factory, version, errorHandler);
//    }

    public boolean addGame(Game game){
        ContentValues values = new ContentValues();
        values.put(COLUMN_GAME_NAME, game.get_name());
        values.put(COLUMN_BGG_ID, game.get_bgg_id());
        values.put(COLUMN_DESCRIPTION, game.get_description());
        values.put(COLUMN_MIN_PLAYERS, game.get_min_players());
        values.put(COLUMN_MAX_PLAYERS, game.get_max_players());
        values.put(COLUMN_MIN_PLAY_TIME, game.get_min_play_time());
        values.put(COLUMN_MAX_PLAY_TIME, game.get_max_play_time());
        values.put(COLUMN_GAME_MECHANIC, game.get_game_mechanic());
        values.put(COLUMN_RATING, game.get_rating());
        values.put(COLUMN_YEAR, game.get_year());
        values.put(COLUMN_IMAGE_URL, game.get_image_url());

        mDb = this.getWritableDatabase();
        long success = mDb.insert(TABLE_GAMES, null, values);
        if (success > 0){
            closeDB();
            return true;
        } else {
            closeDB();
            return false;
        }
    }

    public void addGameBulk(List<Game> games, boolean clearPrevious) {
        if (clearPrevious) deleteAll();

        mDb = this.getWritableDatabase();
        String query = "INSERT INTO " + TABLE_GAMES + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?);";
        SQLiteStatement statement = mDb.compileStatement(query);
        mDb.beginTransaction();
        for (Game g : new ArrayList<Game>(games)) {
            Game currentGame = g;
            statement.clearBindings();
            // For each column index, bind the data to that index
            statement.bindString(2, currentGame.get_name());
            statement.bindLong(3, currentGame.get_bgg_id());
            statement.bindLong(4, currentGame.get_min_players());
            statement.bindLong(5, currentGame.get_max_players());
            statement.bindLong(6, currentGame.get_min_play_time());
            statement.bindLong(7, currentGame.get_max_play_time());
            statement.bindString(8, currentGame.get_description());
            statement.bindString(9, currentGame.get_game_mechanic());
            statement.bindDouble(10, currentGame.get_rating());
            statement.bindLong(11, currentGame.get_year());
            statement.bindString(12, currentGame.get_image_url());

            statement.execute();
        }
        mDb.setTransactionSuccessful();
        mDb.endTransaction();
    }

    public Game findGame(String game_name){
        String query = "Select * FROM " + TABLE_GAMES + " WHERE " + COLUMN_GAME_NAME
                + " =  \"" + game_name + "\"";

        mDb = this.getWritableDatabase();

        Cursor cursor = mDb.rawQuery(query, null);

        Game game = new Game();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            game.set_id(Integer.parseInt(cursor.getString(0)));
            game.set_name(cursor.getString(1));
            game.set_bgg_id(Integer.parseInt(cursor.getString(2)));
            game.set_min_players(Integer.parseInt(cursor.getString(3)));
            game.set_max_players(Integer.parseInt(cursor.getString(4)));
            game.set_min_play_time(Integer.parseInt(cursor.getString(5)));
            game.set_max_play_time(Integer.parseInt(cursor.getString(6)));
            game.set_description(cursor.getString(7));
            game.set_game_mechanic(cursor.getString(8));
            game.set_rating(Double.parseDouble(cursor.getString(9)));
            game.set_year(Integer.parseInt(cursor.getString(10)));
            game.set_image_url(cursor.getString(11));
            cursor.close();
        } else {
            game = null;
        }
        closeDB();
        cursor.close();
        return game;

    }

    public Game findGameByID(int id){
        String query = "Select * FROM " + TABLE_GAMES + " WHERE " + COLUMN_ID
                + " =  \"" + id + "\"";

        mDb = this.getWritableDatabase();

        Cursor cursor = mDb.rawQuery(query, null);

        Game game = new Game();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            game.set_id(Integer.parseInt(cursor.getString(0)));
            game.set_name(cursor.getString(1));
            game.set_bgg_id(Integer.parseInt(cursor.getString(2)));
            game.set_min_players(Integer.parseInt(cursor.getString(3)));
            game.set_max_players(Integer.parseInt(cursor.getString(4)));
            game.set_min_play_time(Integer.parseInt(cursor.getString(5)));
            game.set_max_play_time(Integer.parseInt(cursor.getString(6)));
            game.set_description(cursor.getString(7));
            game.set_game_mechanic(cursor.getString(8));
            game.set_rating(Double.parseDouble(cursor.getString(9)));
            game.set_year(Integer.parseInt(cursor.getString(10)));
            game.set_image_url(cursor.getString(11));
            cursor.close();
        } else {
            game = null;
        }
        closeDB();
        cursor.close();
        return game;

    }

    public boolean deleteAll() {
        mDb = this.getWritableDatabase();
        return mDb.delete(TABLE_GAMES, null, null) > 0;
    }

    public boolean deleteGame(String game_name){
        mDb = this.getWritableDatabase();
        String whereClause = COLUMN_GAME_NAME + "=?";
        String[] whereArgs = new String[] { game_name };
        return mDb.delete(TABLE_GAMES, whereClause, whereArgs) > 0;
    }

    public int getGameCount() {
        mDb = getReadableDatabase();
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + TABLE_GAMES, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public Cursor getAllGames() {
        mDb = getReadableDatabase();
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + TABLE_GAMES + " ORDER BY "
                + COLUMN_GAME_NAME, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        closeDB();
        return cursor;
    }

    public List<Game> getList() {
        Cursor cursor = getAllGames();
        List<Game> games = new ArrayList<Game>();
        while (!cursor.isAfterLast()){
            Game game = new Game();
            game.set_id(Integer.parseInt(cursor.getString(0)));
            game.set_name(cursor.getString(1));
            game.set_bgg_id(Integer.parseInt(cursor.getString(2)));
            game.set_min_players(Integer.parseInt(cursor.getString(3)));
            game.set_max_players(Integer.parseInt(cursor.getString(4)));
            game.set_min_play_time(Integer.parseInt(cursor.getString(5)));
            game.set_max_play_time(Integer.parseInt(cursor.getString(6)));
            game.set_description(cursor.getString(7));
            game.set_game_mechanic(cursor.getString(8));
            game.set_rating(Double.parseDouble(cursor.getString(9)));
            game.set_year(Integer.parseInt(cursor.getString(10)));
            game.set_image_url(cursor.getString(11));
            games.add(game);
            cursor.moveToNext();
        }
        cursor.close();
        return games;
    }

    public Game getRandomGame(String players, String time, String rating, String mechanic) {
        Cursor cursor = getGames(players, time, rating, mechanic);
        Game game = new Game();
        Random random = new Random();
        int rand = random.nextInt(cursor.getCount());
        if (cursor.moveToPosition(rand)) {
            game.set_id(Integer.parseInt(cursor.getString(0)));
            game.set_name(cursor.getString(1));
            game.set_bgg_id(Integer.parseInt(cursor.getString(2)));
            game.set_min_players(Integer.parseInt(cursor.getString(3)));
            game.set_max_players(Integer.parseInt(cursor.getString(4)));
            game.set_min_play_time(Integer.parseInt(cursor.getString(5)));
            game.set_max_play_time(Integer.parseInt(cursor.getString(6)));
            game.set_description(cursor.getString(7));
            game.set_game_mechanic(cursor.getString(8));
            game.set_rating(Double.parseDouble(cursor.getString(9)));
            game.set_year(Integer.parseInt(cursor.getString(10)));
            game.set_image_url(cursor.getString(11));
            cursor.close();
        } else {
            game = null;
        }
        cursor.close();
        return game;
    }

    public Cursor getGames(String players, String time, String rating, String mechanic) {
        Cursor cursor;
        if (players.isEmpty() && time.isEmpty() && rating.isEmpty() && mechanic.isEmpty()){
            cursor = getAllGames();
        } else {
            mDb = getReadableDatabase();

            String _players_min, _players_max, _time, _rating;
            _players_min = (players.isEmpty()) ? "0" : players;
            _players_max = (players.isEmpty()) ? "100" : players;
            _time = (time.isEmpty()) ? "1000" : time;
            _rating = (rating.isEmpty()) ? "0" : rating;

            String[] selectionArgs = {_players_min, _players_max, _time, _rating};
            cursor = mDb.query(TABLE_GAMES, null,
                    COLUMN_MAX_PLAYERS + " >=? AND " +
                            COLUMN_MIN_PLAYERS + " <=? AND " +
                            COLUMN_MAX_PLAY_TIME + " <=? AND " +
                            COLUMN_RATING + " >=?",
                    selectionArgs, null, null, COLUMN_RATING + " ASC");
        }
        closeDB();
        return cursor;
    }

    public boolean gameExists(String fieldName, String value) {
        String query = "Select 1 FROM " + TABLE_GAMES + " WHERE " + fieldName
                + " = \"" + value + "\"";

        mDb = this.getReadableDatabase();

        Cursor cursor = mDb.rawQuery(query, null);
        boolean exists = (cursor.getCount() > 0);
        closeDB();
        cursor.close();
        return exists;
    }


}

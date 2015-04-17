package com.garufa.tabletoproulette;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Jason on 4/13/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

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

    private static DatabaseHelper mInstance = null;
    private SQLiteDatabase db;

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
    }

    public DatabaseHelper(Context context, String name,
                          SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_GAMES_TABLE);
    }

    public void closeDB(){
        if(db != null) {
            db.close();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMES);
        onCreate(database);
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

        db = this.getWritableDatabase();
        long success = db.insert(TABLE_GAMES, null, values);
        if (success > 0){
            closeDB();
            return true;
        } else {
            closeDB();
            return false;
        }
    }

    public void addGameBulk(List<Game> games) {
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();
            for (Game g : new ArrayList<Game>(games)) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_GAME_NAME, g.get_name());
                values.put(COLUMN_BGG_ID, g.get_bgg_id());
                values.put(COLUMN_DESCRIPTION, g.get_description());
                values.put(COLUMN_MIN_PLAYERS, g.get_min_players());
                values.put(COLUMN_MAX_PLAYERS, g.get_max_players());
                values.put(COLUMN_MIN_PLAY_TIME, g.get_min_play_time());
                values.put(COLUMN_MAX_PLAY_TIME, g.get_max_play_time());
                values.put(COLUMN_GAME_MECHANIC, g.get_game_mechanic());
                values.put(COLUMN_RATING, g.get_rating());
                values.put(COLUMN_YEAR, g.get_year());
                values.put(COLUMN_IMAGE_URL, g.get_image_url());

                db.insert(TABLE_GAMES, null, values);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {

        } finally {
            db.endTransaction();
        }

    }

    public Game findGame(String game_name){
        String query = "Select * FROM " + TABLE_GAMES + " WHERE " + COLUMN_GAME_NAME
                + " =  \"" + game_name + "\"";

        db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

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

        db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

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
        boolean result = true;

        String query = "Select * FROM " + TABLE_GAMES;

        Cursor cursor = getAllGames();

        while (!cursor.isAfterLast()){
            String name = cursor.getString(1);
            if (!deleteGame(name)){
                result = false;
            }
            cursor.moveToNext();
        }
        cursor.close();
        return result;
    }

    public boolean deleteGame(String game_name){
        boolean result = false;

        String query = "Select * FROM " + TABLE_GAMES + " WHERE " + COLUMN_GAME_NAME
                + " = \"" + game_name + "\"";

        db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Game game = new Game();

        if (cursor.moveToFirst()){
            game.set_id(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_GAMES, COLUMN_ID + " = ?",
                    new String[] { String.valueOf(game.get_id()) });
            cursor.close();
            result = true;
        }
        closeDB();
        cursor.close();
        return result;
    }

    public int getGameCount() {
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_GAMES, null);

        return cursor.getCount();
    }

    public Cursor getAllGames() {
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_GAMES + " ORDER BY "
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
            db = getReadableDatabase();

            String _players_min, _players_max, _time, _rating;
            _players_min = (players.isEmpty()) ? "0" : players;
            _players_max = (players.isEmpty()) ? "100" : players;
            _time = (time.isEmpty()) ? "1000" : time;
            _rating = (rating.isEmpty()) ? "0" : rating;

            String[] selectionArgs = {_players_min, _players_max, _time, _rating};
            cursor = db.query(TABLE_GAMES, null,
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

        db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        boolean exists = (cursor.getCount() > 0);
        closeDB();
        cursor.close();
        return exists;
    }


}

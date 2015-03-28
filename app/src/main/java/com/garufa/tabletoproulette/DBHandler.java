package com.garufa.tabletoproulette;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * Created by Jason on 3/7/2015.
 */
public class DBHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 4;
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

    public DBHandler(Context context, String name,
                     SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_GAMES_TABLE = "CREATE TABLE " + TABLE_GAMES + "("
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
                + COLUMN_IMAGE_URL + " TEXT" + ")";
        db.execSQL(CREATE_GAMES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMES);
        onCreate(db);
    }

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
        values.put(COLUMN_IMAGE_URL, game.get_image_url());

        SQLiteDatabase db = this.getWritableDatabase();
        long success = db.insert(TABLE_GAMES, null, values);
        if (success > 0){
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }
    }

    public Game findGame(String game_name){
        String query = "Select * FROM " + TABLE_GAMES + " WHERE " + COLUMN_GAME_NAME
                + " =  \"" + game_name + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

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
            game.set_image_url(cursor.getString(10));
            cursor.close();
        } else {
            game = null;
        }
        db.close();
        return game;

    }

    public Game findGameByID(int id){
        String query = "Select * FROM " + TABLE_GAMES + " WHERE " + COLUMN_ID
                + " =  \"" + id + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

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
            game.set_image_url(cursor.getString(10));
            cursor.close();
        } else {
            game = null;
        }
        db.close();
        return game;

    }

    public boolean deleteGame(String game_name){
        boolean result = false;

        String query = "Select * FROM " + TABLE_GAMES + " WHERE " + COLUMN_GAME_NAME
                + " = \"" + game_name + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Game game = new Game();

        if (cursor.moveToFirst()){
            game.set_id(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_GAMES, COLUMN_ID + " = ?",
                    new String[] { String.valueOf(game.get_id()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    public int getGameCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_GAMES, null);
        cursor.close();

        return cursor.getCount();
    }

    public Cursor getAllGames() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_GAMES + " ORDER BY "
                + COLUMN_GAME_NAME, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
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
            game.set_image_url(cursor.getString(10));
            cursor.close();
        } else {
            game = null;
        }
        return game;
    }

    public Cursor getGames(String players, String time, String rating, String mechanic) {
        Cursor cursor;
        if (players.isEmpty() && time.isEmpty() && rating.isEmpty() && mechanic.isEmpty()){
            cursor = getAllGames();
        } else {
            SQLiteDatabase db = getReadableDatabase();

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


        return cursor;
    }
}

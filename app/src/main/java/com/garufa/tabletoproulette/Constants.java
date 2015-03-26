package com.garufa.tabletoproulette;

/**
 * Created by Jason on 3/14/2015.
 */
public final class Constants {

    // Intent passing constants
    public static final String
            EXTRAS_BUNDLE   = "bundle",
            EXTRAS_NAME     = "game_name",
            EXTRAS_ID       = "bgg_id",
            EXTRAS_BGG_ID   = "bgg_id",
            EXTRAS_PLAYERS  = "players",
            EXTRAS_TIME     = "time",
            EXTRAS_MECHANIC = "mechanic",
            EXTRAS_RATING  = "rating";

    // BoardGameGeek Searching constants
    public static final String
            URL_BGG_ID_SEARCH = "http://www.boardgamegeek.com/xmlapi/boardgame/",
            URL_BGG_NAME_SEARCH = "http://www.boardgamegeek.com/xmlapi/search?search=",
            URL_STATS = "?stats=1";

    // Database specific constants
    public static final String
            DATABASE_NAME = "collectionDB.db",
            TABLE_GAMES   = "games",
            COLUMN_ID = "_id",
            COLUMN_GAME_NAME = "game_name",
            COLUMN_DESCRIPTION = "description",
            COLUMN_MIN_PLAYERS = "min_players",
            COLUMN_MAX_PLAYERS = "max_players",
            COLUMN_MIN_PLAY_TIME = "min_play_time",
            COLUMN_MAX_PLAY_TIME = "max_play_time",
            COLUMN_IMAGE_URL     = "image_url",
            COLUMN_GAME_MECHANIC = "game_mechanic",
            COLUMN_BGG_ID = "bgg_id",
            COLUMN_RATING = "rating";

    // Other Constants
    public static final String FILE_TYPE = ".png";

    // Prevent this class from being instantiated
    private Constants(){
        throw new AssertionError();
    }

}

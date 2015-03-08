package com.garufa.tabletoproulette;

import java.net.URL;

/**
 * Created by Jason on 3/7/2015.
 */
public class Game {

    private int _id, _min_players, _max_players, _min_play_time, _max_play_time;
    private String _name, _description, _game_mechanic, _image_url;

    public Game(){}

    public Game(String name, String desc){
        this._name          = name;
        this._min_players   = 0;
        this._max_players   = 0;
        this._min_play_time = 0;
        this._max_play_time = 0;
        this._description   = desc;
        this._game_mechanic = "";
        this._image_url     = "";
    }
    public Game(int id, String name, int min_players, int max_players, int min_play_time,
                int max_play_time, String desc, String game_mechanic, String url){
        this._id            = id;
        this._name          = name;
        this._min_players   = min_players;
        this._max_players   = max_players;
        this._min_play_time = min_play_time;
        this._max_play_time = max_play_time;
        this._description   = desc;
        this._game_mechanic = game_mechanic;
        this._image_url     = url;
    }
    public Game(String name, int min_players, int max_players, int min_play_time,
                int max_play_time, String desc, String game_mechanic, String url){
        this._name          = name;
        this._min_players   = min_players;
        this._max_players   = max_players;
        this._min_play_time = min_play_time;
        this._max_play_time = max_play_time;
        this._description   = desc;
        this._game_mechanic = game_mechanic;
        this._image_url     = url;
    }

    public int      get_id()            { return _id; }
    public String   get_description()   { return _description; }
    public String   get_name()          { return _name; }
    public String   get_image_url()     { return _image_url; }
    public String   get_game_mechanic() { return _game_mechanic; }
    public int      get_min_players()   { return _min_players; }
    public int      get_max_players()   { return _max_players; }
    public int      get_min_play_time() { return _min_play_time; }
    public int      get_max_play_time() { return _max_play_time; }

    public void set_id(int _id)                         { this._id = _id; }
    public void set_name(String _name)                  { this._name = _name; }
    public void set_description(String _description)    { this._description = _description; }
    public void set_max_play_time(int _max_play_time)   { this._max_play_time = _max_play_time; }
    public void set_game_mechanic(String _game_mechanic){ this._game_mechanic = _game_mechanic; }
    public void set_max_players(int _max_players)       { this._max_players = _max_players; }
    public void set_min_players(int _min_players)       { this._min_players = _min_players; }
    public void set_image_url(String _image_url)        { this._image_url = _image_url; }
    public void set_min_play_time(int _min_play_time)   { this._min_play_time = _min_play_time; }




}

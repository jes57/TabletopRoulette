package com.garufa.tabletoproulette;

/**
 * Created by Jason on 3/7/2015.
 */
public class Game {

    private int _id;
    private String _name, _description;

    public Game(){}

    public Game(int id, String name, String desc){
        this._id            = id;
        this._name          = name;
        this._description   = desc;
    }
    public Game(String name, String desc){
        this._name          = name;
        this._description   = desc;
    }

    public int get_id()             { return this._id; }
    public String get_description() { return this._description; }
    public String get_name()        { return this._name; }

    public void set_id(int _id)                         { this._id = _id; }
    public void set_name(String _name)                  { this._name = _name; }
    public void set_description(String _description)    { this._description = _description; }




}
